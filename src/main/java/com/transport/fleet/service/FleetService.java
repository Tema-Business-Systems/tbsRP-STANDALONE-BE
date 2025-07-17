package com.transport.fleet.service;

import com.transport.fleet.model.*;
import com.transport.fleet.repository.*;
import com.transport.fleet.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Base64;



@Service
@Transactional
public class FleetService {

    @Autowired
    private FleetVehicleRepository vehicleRepository;

    @Autowired
    private TrailerRepository trailerRepository;

    @Autowired
    private VehicleClassRepository vehicleClassRepository;

    @Autowired
    private FleetDriverRepository driverRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @Autowired
    private TrailerTypeRepository trailerTypeRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private EntityManager entityManager;

    @Value("${db.schema}")
    private String dbSchema;

    public void createVehicle(FleetVehicleVO vehicleVO) throws NoSuchFieldException, IllegalAccessException {
        FleetVehicle vehicle = getVehicleModelForCreationOrUpdate(vehicleVO, true);
        vehicleRepository.save(vehicle);
    }

    private FleetVehicle getVehicleModelForCreationOrUpdate(FleetVehicleVO vehicleVO, Boolean isCreate) throws IllegalAccessException, NoSuchFieldException {
        FleetVehicle vehicle = new FleetVehicle();

        vehicle.setRowId(isCreate?null: vehicleVO.getRowId());
        vehicle.setCodeyve(vehicleVO.getCode());
        vehicle.setName(vehicleVO.getRegistration());
        vehicle.setFcy(vehicleVO.getSite());
        vehicle.setXacvflg(vehicleVO.getActiveFlag());
        vehicle.setOwnerShip(vehicleVO.getOwnership());
        vehicle.setCategory(vehicleVO.getVehicleClass());
        vehicle.setTrailer(vehicleVO.getTrailer());
        vehicle.setBptNum(vehicleVO.getCarrier());
        vehicle.setStartdepotn(vehicleVO.getStartDepotName());
        vehicle.setEnddepotname(vehicleVO.getEndDepotName());
        vehicle.setBrand(vehicleVO.getBrand());
        vehicle.setModel(vehicleVO.getModel());
        vehicle.setColor(vehicleVO.getColor());
        vehicle.setFuelType(vehicleVO.getFuelType());
        vehicle.setEngicc(vehicleVO.getEngineCC());
        vehicle.setChasnum(vehicleVO.getChasisNum());
        vehicle.setXyearofman(vehicleVO.getYearOfManufacture());
        vehicle.setXper(vehicleVO.getPerformance());
        vehicle.setInsYear(vehicleVO.getInsuranceAmountYearly());
        vehicle.setRoaYear(vehicleVO.getRoadTaxAmountYearly());
        vehicle.setEmptmass(vehicleVO.getEmptyVehicleMass());
        vehicle.setGromass(vehicleVO.getGrossVehicleMass());
        vehicle.setXtolerance(vehicleVO.getTolerance());
        vehicle.setX10cskillcri(vehicleVO.getSkillCriteria());
        vehicle.setStartdepots(vehicleVO.getLoadingTime());
        vehicle.setEnddepotserv(vehicleVO.getOffloadingTime());
        vehicle.setEarliestStart(vehicleVO.getEarliestStartTime());
        vehicle.setLatestStart(vehicleVO.getLatestStartTime());
        vehicle.setOvertTimeStart(vehicleVO.getAvailableHours());
        vehicle.setCostPerUnitO(vehicleVO.getCostPerUnitOverTime());
        vehicle.setCostPerUnitD(vehicleVO.getCostPerUnitDistance());
        vehicle.setCostPerUnitT(vehicleVO.getCostPerUnitTime());
        vehicle.setFixedCost(vehicleVO.getFixedCost());
        vehicle.setMaxTotalDist(vehicleVO.getTotalMaxDistance());
        vehicle.setMaxTotalTime(vehicleVO.getMaxTotalTime());
        vehicle.setMaxTotalTrav(vehicleVO.getMaxTotalTravelTime());
        vehicle.setXmaxspeed(vehicleVO.getMaxSpeed());
        vehicle.setX1coverhrs(vehicleVO.getOverTimeHrs());
        vehicle.setCapacities(vehicleVO.getMaxAllowedWeight());
        vehicle.setVol(vehicleVO.getMaxAllowedVolume());
        vehicle.setQty(vehicleVO.getQuantity());
        vehicle.setMaxOrderCOU(vehicleVO.getMaxOrderCount());
        vehicle.setXnbpallet(vehicleVO.getNoOfPallets());
        vehicle.setXbathght(vehicleVO.getStackHeight());
        vehicle.setXgndocc(vehicleVO.getSurfaceSol());
        vehicle.setXvfcap(vehicleVO.getVehicleFuelCapacity());
        vehicle.setXvfcu(vehicleVO.getVehicleFuelUnits());
        vehicle.setXcuralldri(vehicleVO.getCurrentDriver());
        vehicle.setXdate(vehicleVO.getDate());
        vehicle.setXtime(vehicleVO.getTime());
        vehicle.setCo2em(vehicleVO.getCo2Coef());
        vehicle.setUvycod(vehicleVO.getUnavailable());
        vehicle.setStyzon(vehicleVO.getStyle());
        vehicle.setXcodometer(vehicleVO.getCurrentOdometerReading());
        vehicle.setXlasttime(vehicleVO.getLastUpdateTime());
        vehicle.setXlastdate(vehicleVO.getLastUpdateDate());
        vehicle.setReference(vehicleVO.getReference());
        vehicle.setLastinsp(vehicleVO.getLastInsp());
        vehicle.setInspexp(vehicleVO.getExpiryInsp());
        vehicle.setXdelinspec(vehicleVO.getVehicleAllocationInsp());
        vehicle.setXretinspec(vehicleVO.getReturnVehicleInsp());
        vehicle.setGpsId(vehicleVO.getGpsTrackerId());
        vehicle.setMobtrac(vehicleVO.getRefGMSmobile());
        //todo
        //vehicle.setXx10ct(vehicleVO.getTrackingWebServices());
        vehicle.setMobrad(vehicleVO.getMobileRadio());
        vehicle.setFireExit(vehicleVO.getFireExtinguisher());
        vehicle.setEquipnot(vehicleVO.getEquipmentNotes());
        vehicle.setAasref(vehicleVO.getAsset());
        vehicle.setLicref(vehicleVO.getReference());
        vehicle.setLicexp(vehicleVO.getExpiration());
        vehicle.setLicnot(vehicleVO.getNote());
        vehicle.setVendor(vehicleVO.getSupplier());
        vehicle.setInsexp(vehicleVO.getInsuranceExpiration());
        vehicle.setInsref(vehicleVO.getReference());
        vehicle.setInsnot(vehicleVO.getInsuranceNote());

        if(isCreate && vehicleVO.getImage()!=null){
            String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                    "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                    "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                    "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                    "VALUES (0, 'XX10CVEH', :codblb, 'IMG1', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                    ":currentDate, :currentDate, :auuid, :image)";

            Query query = entityManager.createNativeQuery(insertImage);
            query.setParameter("codblb", vehicleVO.getCode());
            query.setParameter("currentDate", new Date());
            query.setParameter("auuid", new byte[]{0});
            query.setParameter("image", vehicleVO.getImage());
            query.executeUpdate();
        } else if(vehicleVO.getImage()!=null){
            String getImage = "select IDENT1_0, BLOB_0 from "+dbSchema+".CBLOB where CODBLB_0='XX10CVEH' " +
                    "AND IDENT1_0='"+vehicleVO.getCode()+"' AND IDENT2_0='IMG1' ";
            List<Object[]> imageRes = entityManager.createNativeQuery(getImage).getResultList();
            if(!imageRes.isEmpty()){
                String updateImage = "UPDATE " + dbSchema + ".CBLOB  " +
                        "    SET BLOB_0 = :image, UPDDAT_0 = :currentDate, UPDDATTIM_0 = :currentDate  \n" +
                        "    WHERE CODBLB_0 = 'XX10CVEH' AND IDENT1_0 = :ident1 AND IDENT2_0 = 'IMG1' ";
                Query query = entityManager.createNativeQuery(updateImage);
                query.setParameter("image", vehicleVO.getImage());
                query.setParameter("currentDate", new Date());
                query.setParameter("ident1", vehicleVO.getCode());
                query.executeUpdate();
            }else{
                String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                        "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                        "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                        "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                        "VALUES (0, 'XX10CVEH', :codblb, 'IMG1', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                        ":currentDate, :currentDate, :auuid, :image)";
                Query query = entityManager.createNativeQuery(insertImage);
                query.setParameter("codblb", vehicleVO.getCode());
                query.setParameter("currentDate", new Date());
                query.setParameter("auuid", new byte[]{0});
                query.setParameter("image", vehicleVO.getImage());
                query.executeUpdate();
            }
        } else if(!isCreate && vehicleVO.getImage()==null){
            String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CVEH' " +
                    "and IDENT2_0 = 'IMG1' and IDENT1_0 = :ident1";
            Query query = entityManager.createNativeQuery(deleteImage);
            query.setParameter("ident1", vehicleVO.getCode());
            query.executeUpdate();
        }

        int routeRenewalCount=0;
        List<RouteRenewal> routeRenewals=vehicleVO.getRouteRenewalsList();
        if(routeRenewals != null) {
            for (RouteRenewal routeRenewal : routeRenewals) {
                try {
                    String depotName = "depotName" + routeRenewalCount;
                    String serviceTime = "serviceTime" + routeRenewalCount++;
                    Field depotNameField = vehicle.getClass().getDeclaredField(depotName);
                    Field serviceTimeField = vehicle.getClass().getDeclaredField(serviceTime);
                    depotNameField.setAccessible(true);
                    depotNameField.set(vehicle, routeRenewal.getSite());
                    serviceTimeField.setAccessible(true);
                    serviceTimeField.set(vehicle, routeRenewal.getServiceTime());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }
        vehicle.setAllDriver(vehicleVO.getAllDriverFlag());

        if(vehicleVO.getAllDriverFlag()!=2 && vehicleVO.getDriverIds() != null) {
            int driversCount = 0;
            for (VehicleTable driver : vehicleVO.getDriverIds()) {
                try {
                    String driverId = "driverId" + driversCount++;
                    Field driverIdField = vehicle.getClass().getDeclaredField(driverId);
                    driverIdField.setAccessible(true);
                    driverIdField.set(vehicle, driver.getId());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }

        vehicle.setAllBPCNum(vehicleVO.getAllCustomerFlag());

        if(vehicleVO.getAllCustomerFlag()!=2 && vehicleVO.getCustomerIds() != null) {
            int customerCount = 0;
            for (VehicleTable customer : vehicleVO.getCustomerIds()) {
                try {
                    String customerId = "bpcNum" + customerCount++;
                    Field customerIdField = vehicle.getClass().getDeclaredField(customerId);
                    customerIdField.setAccessible(true);
                    customerIdField.set(vehicle, customer.getId());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }

        vehicle.setAllTclCod(vehicleVO.getAllCategoryFlag());

        if(vehicleVO.getAllCategoryFlag()!=2 && vehicleVO.getCategoryIds() != null) {
            int categoryCount = 0;
            for (VehicleTable category : vehicleVO.getCategoryIds()) {
                try {
                    String categoryId = "tclcod" + categoryCount++;
                    Field categoryIdField = vehicle.getClass().getDeclaredField(categoryId);
                    categoryIdField.setAccessible(true);
                    categoryIdField.set(vehicle, category.getId());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }

        vehicle.setXallrutcds(vehicleVO.getXallrutcds());

        if(vehicleVO.getXallrutcds()!=2 && vehicleVO.getRoutesList() != null) {
            int routeCount = 0;
            for (Integer route : vehicleVO.getRoutesList()) {
                try {
                    String routeds = "xrutcds" + routeCount++;
                    Field routeField = vehicle.getClass().getDeclaredField(routeds);
                    routeField.setAccessible(true);
                    routeField.set(vehicle, route);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }

        int inspCount = 0;
        List<TechnicalInspection> inspectionList = vehicleVO.getTechnicalInspectionList();
        if(inspectionList != null) {
            for (TechnicalInspection inspection : inspectionList) {
                try {
                    String insType = "xinsptyp" + inspCount;
                    String lastCheck = "xlstchk" + inspCount;
                    String periodicity = "xperiodicity" + inspCount;
                    String nextVisit = "xnextvisit" + inspCount;
                    String type = "xtypein" + inspCount;
                    Field insTypeField = vehicle.getClass().getDeclaredField(insType);
                    Field lastCheckField = vehicle.getClass().getDeclaredField(lastCheck);
                    Field periodicityField = vehicle.getClass().getDeclaredField(periodicity);
                    Field nextVisitField = vehicle.getClass().getDeclaredField(nextVisit);
                    Field typeField = vehicle.getClass().getDeclaredField(type);

                    insTypeField.setAccessible(true);
                    lastCheckField.setAccessible(true);
                    periodicityField.setAccessible(true);
                    nextVisitField.setAccessible(true);
                    typeField.setAccessible(true);

                    insTypeField.set(vehicle, inspection.getInspectionType());
                    lastCheckField.set(vehicle, inspection.getLastCheck());
                    periodicityField.set(vehicle, inspection.getPeriodicity());
                    nextVisitField.set(vehicle, inspection.getNextVisit());
                    typeField.set(vehicle, inspection.getType());

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }
        return vehicle;
    }

    public List<FleetVehicleVO> getAllVehicles() throws IllegalAccessException {
        List<FleetVehicleVO> vehicleVOList = new ArrayList<>();
        long start=System.currentTimeMillis();
        List<FleetVehicle> vehicles = vehicleRepository.findAll();
        System.out.println("Time to fetch DB records: " + (System.currentTimeMillis() - start) + " ms");
        start = System.currentTimeMillis();
        for(FleetVehicle vehicle: vehicles){
            vehicleVOList.add(getVehicleResponse(vehicle));
        }
        System.out.println("Time to map VO objects: " + (System.currentTimeMillis() - start) + " ms");
        return vehicleVOList;
    }

    public FleetVehicleVO getVehicleByCodeyve(String codeyve) throws IllegalAccessException {
        FleetVehicle vehicle = vehicleRepository.findByCodeyve(codeyve);
        if (vehicle == null) {
            return null;
        }
        Map<String, String> siteMap = commonRepository.getSiteList().stream()
                .collect(Collectors.toMap(
                        d -> String.valueOf(d.getValue()),
                        DropdownData::getLabel,
                        (existing, replacement) -> existing
                ));
        Map<String, String> driverMap = commonRepository.getDriverList().stream()
                .collect(Collectors.toMap(
                        d -> String.valueOf(d.getValue()),
                        DropdownData::getLabel,
                        (existing, replacement) -> existing
                ));
        Map<String, String> customerMap = commonRepository.getCustomerList().stream()
                .collect(Collectors.toMap(
                        d -> String.valueOf(d.getValue()),
                        DropdownData::getLabel,
                        (existing, replacement) -> existing
                ));

        Map<String, String> categoryMap = commonRepository.getCategoryList().stream()
                .collect(Collectors.toMap(
                        d -> String.valueOf(d.getValue()),
                        DropdownData::getLabel,
                        (existing, replacement) -> existing
                ));
        return getVehicleResponse1(vehicle, siteMap, driverMap, customerMap, categoryMap);
    }

    public List<FleetVehicleVO> getAllVehicles1() throws IllegalAccessException {
        List<FleetVehicle> vehicles = vehicleRepository.findAll();

        Map<String, String> siteMap = commonRepository.getSiteList().stream()
                .collect(Collectors.toMap(d -> (String) d.getValue(), DropdownData::getLabel));

        Map<String, String> driverMap = commonRepository.getDriverList().stream()
                .collect(Collectors.toMap(d -> (String) d.getValue(), DropdownData::getLabel));

        Map<String, String> customerMap = commonRepository.getCustomerList().stream()
                .collect(Collectors.toMap(d -> (String) d.getValue(), DropdownData::getLabel));

        Map<String, String> categoryMap = commonRepository.getCategoryList().stream()
                .collect(Collectors.toMap(d -> (String) d.getValue(), DropdownData::getLabel));

        List<String> vehicleCodes = vehicles.stream()
                .map(FleetVehicle::getCodeyve)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<String, byte[]> imageMap = getVehicleImageMap(vehicleCodes);

        List<FleetVehicleVO> vehicleVOList = new ArrayList<>();
        for (FleetVehicle vehicle : vehicles) {
            FleetVehicleVO vo = getVehicleResponse1(vehicle, siteMap, driverMap, customerMap, categoryMap);
            byte[] imageBytes = imageMap.get(vehicle.getCodeyve());
            vo.setImage(imageBytes);
            vehicleVOList.add(vo);
        }


        return vehicleVOList;
    }
    private FleetVehicleVO getVehicleResponse1(
            FleetVehicle vehicle,
            Map<String, String> siteMap,
            Map<String, String> driverMap,
            Map<String, String> customerMap,
            Map<String, String> categoryMap
    ) throws IllegalAccessException {
        FleetVehicleVO vehicleVO = new FleetVehicleVO();
        if(vehicle==null){
            return null;
        }

        vehicleVO.setRowId(vehicle.getRowId());
        vehicleVO.setCode(vehicle.getCodeyve());
        vehicleVO.setRegistration(vehicle.getName());
        vehicleVO.setSite(vehicle.getFcy());
        vehicleVO.setVehicleClass(vehicle.getCategory());
        vehicleVO.setActiveFlag(vehicle.getXacvflg());
        vehicleVO.setOwnership(vehicle.getOwnerShip());
        vehicleVO.setTrailer(vehicle.getTrailer());
        vehicleVO.setCarrier(vehicle.getBptNum());
        vehicleVO.setStartDepotName(vehicle.getStartdepotn());
        vehicleVO.setEndDepotName(vehicle.getEnddepotname());
        vehicleVO.setBrand(vehicle.getBrand());
        vehicleVO.setModel(vehicle.getModel());
        vehicleVO.setColor(vehicle.getColor());
        vehicleVO.setFuelType(vehicle.getFuelType());
        vehicleVO.setLocation(vehicle.getCodeyve());
        vehicleVO.setEngineCC(vehicle.getEngicc());
        vehicleVO.setChasisNum(vehicle.getChasnum());
        vehicleVO.setYearOfManufacture(vehicle.getXyearofman());
        vehicleVO.setPerformance(vehicle.getXper());
        vehicleVO.setInsuranceAmountYearly(vehicle.getInsYear());
        vehicleVO.setRoadTaxAmountYearly(vehicle.getRoaYear());
        vehicleVO.setEmptyVehicleMass(vehicle.getEmptmass());
        vehicleVO.setGrossVehicleMass(vehicle.getGromass());
        vehicleVO.setTolerance(vehicle.getXtolerance());
        vehicleVO.setSkillCriteria(vehicle.getX10cskillcri());
        vehicleVO.setLoadingTime(vehicle.getStartdepots());
        vehicleVO.setOffloadingTime(vehicle.getEnddepotserv());
        vehicleVO.setEarliestStartTime(vehicle.getEarliestStart());
        vehicleVO.setLatestStartTime(vehicle.getLatestStart());
        vehicleVO.setAvailableHours(vehicle.getOvertTimeStart());
        vehicleVO.setCostPerUnitOverTime(vehicle.getCostPerUnitO());
        vehicleVO.setCostPerUnitDistance(vehicle.getCostPerUnitD());
        vehicleVO.setCostPerUnitTime(vehicle.getCostPerUnitT());
        vehicleVO.setFixedCost(vehicle.getFixedCost());
        vehicleVO.setTotalMaxDistance(vehicle.getMaxTotalDist());
        vehicleVO.setMaxTotalTime(vehicle.getMaxTotalTime());
        vehicleVO.setMaxTotalTravelTime(vehicle.getMaxTotalTrav());
        vehicleVO.setMaxSpeed(vehicle.getXmaxspeed());
        vehicleVO.setOverTimeHrs(vehicle.getX1coverhrs());
        vehicleVO.setMaxAllowedWeight(vehicle.getCapacities());
        vehicleVO.setMaxAllowedVolume(vehicle.getVol());
        vehicleVO.setQuantity(vehicle.getQty());
        vehicleVO.setMaxOrderCount(vehicle.getMaxOrderCOU());
        vehicleVO.setNoOfPallets(vehicle.getXnbpallet());
        vehicleVO.setStackHeight(vehicle.getXbathght());
        vehicleVO.setSurfaceSol(vehicle.getXgndocc());
        vehicleVO.setVehicleFuelCapacity(vehicle.getXvfcap());
        vehicleVO.setVehicleFuelUnits(vehicle.getXvfcu());
        vehicleVO.setCurrentDriver(vehicle.getXcuralldri());
        vehicleVO.setDate(vehicle.getXdate());
        vehicleVO.setTime(vehicle.getXtime());
        vehicleVO.setCo2Coef(vehicle.getCo2em());
        vehicleVO.setUnavailable(vehicle.getUvycod());
        vehicleVO.setStyle(vehicle.getStyzon());
        vehicleVO.setCurrentOdometerReading(vehicle.getXcodometer());
        vehicleVO.setLastUpdateTime(vehicle.getXlasttime());
        vehicleVO.setLastUpdateDate(vehicle.getXlastdate());
        vehicleVO.setReference(vehicle.getReference());
        vehicleVO.setLastInsp(vehicle.getLastinsp());
        vehicleVO.setExpiryInsp(vehicle.getInspexp());
        vehicleVO.setVehicleAllocationInsp(vehicle.getXdelinspec());
        vehicleVO.setReturnVehicleInsp(vehicle.getXretinspec());
        vehicleVO.setGpsTrackerId(vehicle.getGpsId());
        vehicleVO.setRefGMSmobile(vehicle.getMobtrac());
        //todo
        //vehicleVO.setTrackingWebServices(vehicle.getXx10ct);
        vehicleVO.setMobileRadio(vehicle.getMobrad());
        vehicleVO.setFireExtinguisher(vehicle.getFireExit());
        vehicleVO.setEquipmentNotes(vehicle.getEquipnot());
        vehicleVO.setAsset(vehicle.getAasref());
        vehicleVO.setReference(vehicle.getLicref());
        vehicleVO.setExpiration(vehicle.getLicexp());
        vehicleVO.setNote(vehicle.getLicnot());
        vehicleVO.setSupplier(vehicle.getVendor());
        vehicleVO.setInsuranceExpiration(vehicle.getInsexp());
        vehicleVO.setReference(vehicle.getInsref());
        vehicleVO.setInsuranceNote(vehicle.getInsnot());

        /*String IMAGE_QUERY = "SELECT BLOB_0 FROM "+dbSchema+".CBLOB WHERE CODBLB_0= :codblob_0 AND IDENT2_0='IMG1' AND IDENT1_0 = :ident1_0";

        Query query = entityManager.createNativeQuery(IMAGE_QUERY);
        query.setParameter("codblob_0", "XX10CVEH");
        query.setParameter("ident1_0", vehicle.getCodeyve());

        try {
            Object blob = query.getSingleResult();
            vehicleVO.setImage(blob!=null?(byte[]) blob: null);
        } catch (NoResultException e) {
            vehicleVO.setImage(null);
        }*/

        List<RouteRenewal> routeRenewals = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            String depotNameField = "depotName" + i;
            String serviceTimeField = "serviceTime" + i;
            String depotName = getFieldValue(vehicle, depotNameField)!=null? (String) getFieldValue(vehicle, depotNameField) :"";
            Double serviceTime = getFieldValue(vehicle, serviceTimeField)!=null? (Double) getFieldValue(vehicle, serviceTimeField) : null;

            String siteDesc = siteMap.getOrDefault(depotName, "");

            if (depotName != null && serviceTime != null && !depotName.trim().isEmpty()) {
                routeRenewals.add(new RouteRenewal(depotName,siteDesc, serviceTime));
            }
        }
        vehicleVO.setRouteRenewalsList(routeRenewals);

        List<VehicleTable> driversList = new ArrayList<>();
        vehicleVO.setAllDriverFlag(vehicle.getAllDriver());
        if(vehicle.getAllDriver()!=2) {
            for (int i = 0; i <= 9; i++) {
                String driverIdField = "driverId" + i;
                String driverId = getFieldValue(vehicle, driverIdField) != null ?
                        (String) getFieldValue(vehicle, driverIdField) : "";

                String driverDesc = driverMap.getOrDefault(driverId, "");

                if (driverId != null && !driverId.trim().isEmpty() ) {
                    driversList.add(new VehicleTable(driverId, driverDesc));
                }
            }
            vehicleVO.setDriverIds(driversList);
        }else{
            vehicleVO.setDriverIds(driversList);
        }

        List<VehicleTable> customersList = new ArrayList<>();
        vehicleVO.setAllCustomerFlag(vehicle.getAllBPCNum());
        if(vehicle.getAllBPCNum()!=2) {
            for (int i = 0; i <= 9; i++) {
                String customersField = "bpcNum" + i;
                String customerId = getFieldValue(vehicle, customersField) != null ?
                        (String) getFieldValue(vehicle, customersField) : "";

                String customerDesc = customerMap.getOrDefault(customerId, "");

                if (customerId != null && !customerId.trim().isEmpty()) {
                    customersList.add(new VehicleTable(customerId,customerDesc));
                }
            }
            vehicleVO.setCustomerIds(customersList);
        }else{
            vehicleVO.setCustomerIds(customersList);
        }

        List<VehicleTable> categoryList = new ArrayList<>();
        vehicleVO.setAllCategoryFlag(vehicle.getAllTclCod());
        if(vehicle.getAllTclCod()!=2) {
            for (int i = 0; i <= 9; i++) {
                String categoryField = "tclcod" + i;
                String categoryId = getFieldValue(vehicle, categoryField) != null ?
                        (String) getFieldValue(vehicle, categoryField) : "";

                String categoryDesc = categoryMap.getOrDefault(categoryId, "");

                if (categoryId != null && !categoryId.trim().isEmpty()) {
                    categoryList.add(new VehicleTable(categoryId,categoryDesc));
                }
            }
            vehicleVO.setCategoryIds(categoryList);
        }else{
            vehicleVO.setCategoryIds(categoryList);
        }

        List<Integer> routeList = new ArrayList<>();
        vehicleVO.setXallrutcds(vehicle.getXallrutcds());
        if(vehicle.getXallrutcds()!=2) {
            for (int i = 0; i <= 9; i++) {
                String routeField = "xrutcds" + i;
                Integer routeFieldValue = (Integer) getFieldValue(vehicle, routeField);
                if (routeFieldValue!=null && routeFieldValue!=0) {
                    routeList.add(routeFieldValue);
                }
            }
            vehicleVO.setRoutesList(routeList);
        }else{
            vehicleVO.setRoutesList(routeList);
        }

        List<TechnicalInspection> technicalInspectionList = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            String insTypeField = "xinsptyp" + i;
            String lastCheckField = "xlstchk" + i;
            String periodicityField = "xperiodicity"+i;
            String nextVisitField = "xnextvisit"+i;
            String typeField = "xtypein"+i;
            String insType = getFieldValue(vehicle, insTypeField)!=null? (String) getFieldValue(vehicle, insTypeField) :"";
            Date lastCheck = getFieldValue(vehicle, lastCheckField)!=null? (Date) getFieldValue(vehicle, lastCheckField) :null;
            Integer periodicity = getFieldValue(vehicle, periodicityField)!=null? (int) getFieldValue(vehicle, periodicityField) :null;
            Date nextVisit = getFieldValue(vehicle, nextVisitField)!=null? (Date) getFieldValue(vehicle, nextVisitField) :null;
            Integer type = getFieldValue(vehicle, typeField)!=null? (int) getFieldValue(vehicle, typeField) :null;

            technicalInspectionList.add(new TechnicalInspection(insType,lastCheck, periodicity,nextVisit,type));
        }
        vehicleVO.setTechnicalInspectionList(technicalInspectionList);

        vehicleVO.setTransactionHistoryList(getTransactionHistoryListByVehicleCode(vehicle.getCodeyve()));

        return vehicleVO;
    }

    private Map<String, byte[]> getVehicleImageMap(List<String> vehicleCodes) {
        String sql = "SELECT IDENT1_0, BLOB_0 FROM " + dbSchema + ".CBLOB " +
                "WHERE CODBLB_0 = :codblob_0 AND IDENT2_0 = 'IMG1' " +
                "AND IDENT1_0 IN :ident1_0_list";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("codblob_0", "XX10CVEH");
        query.setParameter("ident1_0_list", vehicleCodes);

        List<Object[]> results = query.getResultList();
        Map<String, byte[]> imageMap = new HashMap<>();

        for (Object[] row : results) {
            String ident1 = (String) row[0];
            byte[] blob = (byte[]) row[1];
            imageMap.put(ident1, blob);
        }

        return imageMap;
    }

    private FleetVehicleVO getVehicleResponse(FleetVehicle vehicle) throws IllegalAccessException {
        FleetVehicleVO vehicleVO = new FleetVehicleVO();
        if(vehicle==null){
            return null;
        }

        vehicleVO.setRowId(vehicle.getRowId());
        vehicleVO.setCode(vehicle.getCodeyve());
        vehicleVO.setRegistration(vehicle.getName());
        vehicleVO.setSite(vehicle.getFcy());
        vehicleVO.setVehicleClass(vehicle.getCategory());
        vehicleVO.setActiveFlag(vehicle.getXacvflg());
        vehicleVO.setOwnership(vehicle.getOwnerShip());
        vehicleVO.setTrailer(vehicle.getTrailer());
        vehicleVO.setCarrier(vehicle.getBptNum());
        vehicleVO.setStartDepotName(vehicle.getStartdepotn());
        vehicleVO.setEndDepotName(vehicle.getEnddepotname());
        vehicleVO.setBrand(vehicle.getBrand());
        vehicleVO.setModel(vehicle.getModel());
        vehicleVO.setColor(vehicle.getColor());
        vehicleVO.setFuelType(vehicle.getFuelType());
        vehicleVO.setLocation(vehicle.getCodeyve());
        vehicleVO.setEngineCC(vehicle.getEngicc());
        vehicleVO.setChasisNum(vehicle.getChasnum());
        vehicleVO.setYearOfManufacture(vehicle.getXyearofman());
        vehicleVO.setPerformance(vehicle.getXper());
        vehicleVO.setInsuranceAmountYearly(vehicle.getInsYear());
        vehicleVO.setRoadTaxAmountYearly(vehicle.getRoaYear());
        vehicleVO.setEmptyVehicleMass(vehicle.getEmptmass());
        vehicleVO.setGrossVehicleMass(vehicle.getGromass());
        vehicleVO.setTolerance(vehicle.getXtolerance());
        vehicleVO.setSkillCriteria(vehicle.getX10cskillcri());
        vehicleVO.setLoadingTime(vehicle.getStartdepots());
        vehicleVO.setOffloadingTime(vehicle.getEnddepotserv());
        vehicleVO.setEarliestStartTime(vehicle.getEarliestStart());
        vehicleVO.setLatestStartTime(vehicle.getLatestStart());
        vehicleVO.setAvailableHours(vehicle.getOvertTimeStart());
        vehicleVO.setCostPerUnitOverTime(vehicle.getCostPerUnitO());
        vehicleVO.setCostPerUnitDistance(vehicle.getCostPerUnitD());
        vehicleVO.setCostPerUnitTime(vehicle.getCostPerUnitT());
        vehicleVO.setFixedCost(vehicle.getFixedCost());
        vehicleVO.setTotalMaxDistance(vehicle.getMaxTotalDist());
        vehicleVO.setMaxTotalTime(vehicle.getMaxTotalTime());
        vehicleVO.setMaxTotalTravelTime(vehicle.getMaxTotalTrav());
        vehicleVO.setMaxSpeed(vehicle.getXmaxspeed());
        vehicleVO.setOverTimeHrs(vehicle.getX1coverhrs());
        vehicleVO.setMaxAllowedWeight(vehicle.getCapacities());
        vehicleVO.setMaxAllowedVolume(vehicle.getVol());
        vehicleVO.setQuantity(vehicle.getQty());
        vehicleVO.setMaxOrderCount(vehicle.getMaxOrderCOU());
        vehicleVO.setNoOfPallets(vehicle.getXnbpallet());
        vehicleVO.setStackHeight(vehicle.getXbathght());
        vehicleVO.setSurfaceSol(vehicle.getXgndocc());
        vehicleVO.setVehicleFuelCapacity(vehicle.getXvfcap());
        vehicleVO.setVehicleFuelUnits(vehicle.getXvfcu());
        vehicleVO.setCurrentDriver(vehicle.getXcuralldri());
        vehicleVO.setDate(vehicle.getXdate());
        vehicleVO.setTime(vehicle.getXtime());
        vehicleVO.setCo2Coef(vehicle.getCo2em());
        vehicleVO.setUnavailable(vehicle.getUvycod());
        vehicleVO.setStyle(vehicle.getStyzon());
        vehicleVO.setCurrentOdometerReading(vehicle.getXcodometer());
        vehicleVO.setLastUpdateTime(vehicle.getXlasttime());
        vehicleVO.setLastUpdateDate(vehicle.getXlastdate());
        vehicleVO.setReference(vehicle.getReference());
        vehicleVO.setLastInsp(vehicle.getLastinsp());
        vehicleVO.setExpiryInsp(vehicle.getInspexp());
        vehicleVO.setVehicleAllocationInsp(vehicle.getXdelinspec());
        vehicleVO.setReturnVehicleInsp(vehicle.getXretinspec());
        vehicleVO.setGpsTrackerId(vehicle.getGpsId());
        vehicleVO.setRefGMSmobile(vehicle.getMobtrac());
        //todo
        //vehicleVO.setTrackingWebServices(vehicle.getXx10ct);
        vehicleVO.setMobileRadio(vehicle.getMobrad());
        vehicleVO.setFireExtinguisher(vehicle.getFireExit());
        vehicleVO.setEquipmentNotes(vehicle.getEquipnot());
        vehicleVO.setAsset(vehicle.getAasref());
        vehicleVO.setReference(vehicle.getLicref());
        vehicleVO.setExpiration(vehicle.getLicexp());
        vehicleVO.setNote(vehicle.getLicnot());
        vehicleVO.setSupplier(vehicle.getVendor());
        vehicleVO.setInsuranceExpiration(vehicle.getInsexp());
        vehicleVO.setReference(vehicle.getInsref());
        vehicleVO.setInsuranceNote(vehicle.getInsnot());

        String IMAGE_QUERY = "SELECT BLOB_0 FROM " + dbSchema + ".CBLOB WHERE CODBLB_0 = :codblob_0 AND IDENT2_0 = 'IMG1' AND IDENT1_0 = :ident1_0";
        Query query = entityManager.createNativeQuery(IMAGE_QUERY);
        query.setParameter("codblob_0", "XX10CVEH");
        query.setParameter("ident1_0", vehicle.getCodeyve());

        try {
            Object blob = query.getSingleResult();
            if (blob != null && blob instanceof byte[]) {
                vehicleVO.setImage((byte[]) blob);
            } else {
                vehicleVO.setImage(null);
            }
        } catch (NoResultException e) {
            vehicleVO.setImage(null);
        }

        List<RouteRenewal> routeRenewals = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            String depotNameField = "depotName" + i;
            String serviceTimeField = "serviceTime" + i;
            String depotName = getFieldValue(vehicle, depotNameField)!=null? (String) getFieldValue(vehicle, depotNameField) :"";
            Double serviceTime = getFieldValue(vehicle, serviceTimeField)!=null? (Double) getFieldValue(vehicle, serviceTimeField) : null;

            Optional<DropdownData> siteDescData = commonRepository.getSiteList().stream().filter(x->x.getValue().equals(depotName)).findFirst();
            String siteDesc = "";
            if(siteDescData.isPresent()){
                siteDesc = siteDescData.get().getLabel();
            }

            if (depotName != null && serviceTime != null && !depotName.trim().isEmpty()) {
                routeRenewals.add(new RouteRenewal(depotName,siteDesc, serviceTime));
            }
        }
        vehicleVO.setRouteRenewalsList(routeRenewals);

        List<VehicleTable> driversList = new ArrayList<>();
        vehicleVO.setAllDriverFlag(vehicle.getAllDriver());
        if(vehicle.getAllDriver()!=2) {
            for (int i = 0; i <= 9; i++) {
                String driverIdField = "driverId" + i;
                String driverId = getFieldValue(vehicle, driverIdField) != null ?
                        (String) getFieldValue(vehicle, driverIdField) : "";

                Optional<DropdownData> driverDescData = commonRepository.getDriverList().stream().filter(x->x.getValue().equals(driverId)).findFirst();
                String driverDesc = "";
                if(driverDescData.isPresent()){
                    driverDesc = driverDescData.get().getLabel();
                }

                if (driverId != null && !driverId.trim().isEmpty() ) {
                    driversList.add(new VehicleTable(driverId, driverDesc));
                }
            }
            vehicleVO.setDriverIds(driversList);
        }else{
            vehicleVO.setDriverIds(driversList);
        }

        List<VehicleTable> customersList = new ArrayList<>();
        vehicleVO.setAllCustomerFlag(vehicle.getAllBPCNum());
        if(vehicle.getAllBPCNum()!=2) {
            for (int i = 0; i <= 9; i++) {
                String customersField = "bpcNum" + i;
                String customerId = getFieldValue(vehicle, customersField) != null ?
                        (String) getFieldValue(vehicle, customersField) : "";

                Optional<DropdownData> customerDescData = commonRepository.getCustomerList().stream().
                        filter(x->x.getValue().equals(customerId)).findFirst();
                String customerDesc = "";
                if(customerDescData.isPresent()){
                    customerDesc = customerDescData.get().getLabel();
                }

                if (customerId != null && !customerId.trim().isEmpty()) {
                    customersList.add(new VehicleTable(customerId,customerDesc));
                }
            }
            vehicleVO.setCustomerIds(customersList);
        }else{
            vehicleVO.setCustomerIds(customersList);
        }

        List<VehicleTable> categoryList = new ArrayList<>();
        vehicleVO.setAllCategoryFlag(vehicle.getAllTclCod());
        if(vehicle.getAllTclCod()!=2) {
            for (int i = 0; i <= 9; i++) {
                String categoryField = "tclcod" + i;
                String categoryId = getFieldValue(vehicle, categoryField) != null ?
                        (String) getFieldValue(vehicle, categoryField) : "";

                Optional<DropdownData> categoryDescData = commonRepository.getCategoryList().stream()
                        .filter(x->x.getValue().equals(categoryId)).findFirst();
                String categoryDesc = "";
                if(categoryDescData.isPresent()){
                    categoryDesc = categoryDescData.get().getLabel();
                }

                if (categoryId != null && !categoryId.trim().isEmpty()) {
                    categoryList.add(new VehicleTable(categoryId,categoryDesc));
                }
            }
            vehicleVO.setCategoryIds(categoryList);
        }else{
            vehicleVO.setCategoryIds(categoryList);
        }

        List<Integer> routeList = new ArrayList<>();
        vehicleVO.setXallrutcds(vehicle.getXallrutcds());
        if(vehicle.getXallrutcds()!=2) {
            for (int i = 0; i <= 9; i++) {
                String routeField = "xrutcds" + i;
                Integer routeFieldValue = (Integer) getFieldValue(vehicle, routeField);
                if (routeFieldValue!=null && routeFieldValue!=0) {
                    routeList.add(routeFieldValue);
                }
            }
            vehicleVO.setRoutesList(routeList);
        }else{
            vehicleVO.setRoutesList(routeList);
        }

        List<TechnicalInspection> technicalInspectionList = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            String insTypeField = "xinsptyp" + i;
            String lastCheckField = "xlstchk" + i;
            String periodicityField = "xperiodicity"+i;
            String nextVisitField = "xnextvisit"+i;
            String typeField = "xtypein"+i;
            String insType = getFieldValue(vehicle, insTypeField)!=null? (String) getFieldValue(vehicle, insTypeField) :"";
            Date lastCheck = getFieldValue(vehicle, lastCheckField)!=null? (Date) getFieldValue(vehicle, lastCheckField) :null;
            Integer periodicity = getFieldValue(vehicle, periodicityField)!=null? (int) getFieldValue(vehicle, periodicityField) :null;
            Date nextVisit = getFieldValue(vehicle, nextVisitField)!=null? (Date) getFieldValue(vehicle, nextVisitField) :null;
            Integer type = getFieldValue(vehicle, typeField)!=null? (int) getFieldValue(vehicle, typeField) :null;

            technicalInspectionList.add(new TechnicalInspection(insType,lastCheck, periodicity,nextVisit,type));
        }
        vehicleVO.setTechnicalInspectionList(technicalInspectionList);

        vehicleVO.setTransactionHistoryList(getTransactionHistoryListByVehicleCode(vehicle.getCodeyve()));

        return vehicleVO;
    }

    private List<TransactionHistory> getTransactionHistoryListByVehicleCode(String codeyve) {
        String queryString = "select XODOMETER_0, XLUDATE_0, XLUTIME_0, XTRANSC_0, XSOURCE_0 from "
                + dbSchema + ".XX10CODOMTR where XVEHICLE_0= '"+codeyve+"'";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(result -> new TransactionHistory(
                        result[0]!=null?(int)result[0]:null,
                        result[1]!=null?(Date)result[1]:null,
                        result[2]!=null?result[2].toString():"",
                        result[3]!=null?result[3].toString():"",
                        result[4]!=null?((Byte) result[4]).intValue():null))
                .collect(Collectors.toList());
    }

    private Object getFieldValue(Object object, String fieldName) throws IllegalAccessException {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return  field.get(object);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public String deleteVehicleByCodeyve(String codeyve) {
        vehicleRepository.deleteByCodeyve(codeyve);
        String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CVEH' " +
                "and IDENT2_0 = 'IMG1' and IDENT1_0 = :ident1";
        Query query = entityManager.createNativeQuery(deleteImage);
        query.setParameter("ident1", codeyve);
        query.executeUpdate();
        return "success";
    }

    public void updateVehicle(FleetVehicleVO vehicle) throws NoSuchFieldException, IllegalAccessException {
        vehicleRepository.save(getVehicleModelForCreationOrUpdate(vehicle, false));
    }

    public Trailer createTrailer(TrailerVO trailerVO) throws NoSuchFieldException, IllegalAccessException {
        Trailer trailer = getTrailerModel(trailerVO, true);
        return trailerRepository.save(trailer);
    }

    private Trailer getTrailerModel(TrailerVO trailerVO, Boolean isCreate) throws NoSuchFieldException, IllegalAccessException {
        Trailer trailer = new Trailer();
        
        trailer.setRowid(isCreate?null:trailerVO.getRowid());
        trailer.setTrailer(trailerVO.getTrailer());
        trailer.setDes(trailerVO.getDes());
        trailer.setFcy(trailerVO.getFcy());
        trailer.setLinkTo(trailerVO.getLinkTo());
        trailer.setType(trailerVO.getType());
        trailer.setMake(trailerVO.getMake());
        trailer.setModel(trailerVO.getModel());
        trailer.setXtailgate(trailerVO.getXtailgate());
        trailer.setAnnee(trailerVO.getAnnee());
        trailer.setAasref(trailerVO.getAasref());
        trailer.setNbaxle(trailerVO.getNbaxle());
        trailer.setStyzon(trailerVO.getStyzon());
        trailer.setMaxLen(trailerVO.getMaxLen());
        trailer.setMaxWid(trailerVO.getMaxWid());
        trailer.setMaxLovol(trailerVO.getMaxLovol());
        trailer.setCurbWei(trailerVO.getCurbWei());
        trailer.setMaxFH(trailerVO.getMaxFH());
        trailer.setMaxLoams(trailerVO.getMaxLoams());
        trailer.setGvwr(trailerVO.getGvwr());
        trailer.setXbathght(trailerVO.getXbathght());
        trailer.setXgndocc(trailerVO.getXgndocc());
        trailer.setLastInsp(trailerVO.getLastInsp());
        trailer.setXrentable(trailerVO.getXrentable());
        trailer.setXsideope(trailerVO.getXsideope());
        trailer.setXuvycod(trailerVO.getXuvycod());
        trailer.setComment(trailerVO.getComment());
        trailer.setXstomgtcod(trailerVO.getXstomgtcod());
        trailer.setXlotmgtcod(trailerVO.getXlotmgtcod());
        trailer.setXseril(trailerVO.getXseril());

        if(isCreate && trailerVO.getImage()!=null){
            String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                    "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                    "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                    "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                    "VALUES (0, 'XX10CTRA', :codblb, '', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                    ":currentDate, :currentDate, :auuid, :image)";

            Query query = entityManager.createNativeQuery(insertImage);
            query.setParameter("codblb", trailerVO.getTrailer());
            query.setParameter("currentDate", new Date());
            query.setParameter("auuid", new byte[]{0});
            query.setParameter("image", trailerVO.getImage());
            query.executeUpdate();
        } else if(trailerVO.getImage()!=null){
            String getImage = "select IDENT1_0, BLOB_0 from "+dbSchema+".CBLOB where CODBLB_0='XX10CTRA' " +
                    "AND IDENT1_0='"+trailerVO.getTrailer()+"' ";
            List<Object[]> imageRes = entityManager.createNativeQuery(getImage).getResultList();
            if(!imageRes.isEmpty()){
                String updateImage = "UPDATE " + dbSchema + ".CBLOB  " +
                        "    SET BLOB_0 = :image, UPDDAT_0 = :currentDate, UPDDATTIM_0 = :currentDate  \n" +
                        "    WHERE CODBLB_0 = 'XX10CTRA' AND IDENT1_0 = :ident1 ";
                Query query = entityManager.createNativeQuery(updateImage);
                query.setParameter("image", trailerVO.getImage());
                query.setParameter("currentDate", new Date());
                query.setParameter("ident1", trailerVO.getTrailer());
                query.executeUpdate();
            }else{
                String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                        "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                        "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                        "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                        "VALUES (0, 'XX10CTRA', :codblb, '', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                        ":currentDate, :currentDate, :auuid, :image)";
                Query query = entityManager.createNativeQuery(insertImage);
                query.setParameter("codblb", trailerVO.getTrailer());
                query.setParameter("currentDate", new Date());
                query.setParameter("auuid", new byte[]{0});
                query.setParameter("image", trailerVO.getImage());
                query.executeUpdate();
            }
        }else if(!isCreate && trailerVO.getImage()==null){
            String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CTRA' " +
                    " and IDENT1_0 = :ident1";
            Query query = entityManager.createNativeQuery(deleteImage);
            query.setParameter("ident1", trailerVO.getTrailer());
            query.executeUpdate();
        }

        if(isCreate) {
            int compartmentCount = 1;
            for (TrailerCompartment compartment : trailerVO.getCompartmentList()) {
                String queryString = "insert into "+dbSchema+".XX10CTRAILD " +
                        "(UPDTICK_0, XTRAILER_0, XFCY_0, XLINNUM_0, XCOMPARTMENT_0, XCAPACITY_0, CREDATTIM_0, UPDDATTIM_0, AUUID_0, CREUSR_0, UPDUSR_0) " +
                        "values (0, :trailer, :fcy, :linNum, :compartment, :capacity, :credAtTime, :updAtTime, :uuid, '', '')";

                Query nativeQuery = entityManager.createNativeQuery(queryString);

                nativeQuery.setParameter("trailer", trailerVO.getTrailer());
                nativeQuery.setParameter("fcy", trailerVO.getFcy());
                nativeQuery.setParameter("linNum", compartmentCount * 1000);
                nativeQuery.setParameter("compartment", compartment.getCompartment());
                nativeQuery.setParameter("capacity", compartment.getCapacity());
                nativeQuery.setParameter("credAtTime", new Date());
                nativeQuery.setParameter("updAtTime", new Date());
                nativeQuery.setParameter("uuid", new byte[]{0});

                nativeQuery.executeUpdate();
            }
        }

        int inspCount = 0;
        for (TechnicalInspection inspection : trailerVO.getTechnicalInspectionList()) {
            try {
                String insType = "xinsptyp" + inspCount;
                String lastCheck = "xlstchk" + inspCount;
                String periodicity = "xperiodicity"+inspCount;
                String nextVisit = "xnextvisit"+inspCount;
                String type = "xtypein"+inspCount;
                Field insTypeField = trailer.getClass().getDeclaredField(insType);
                Field lastCheckField = trailer.getClass().getDeclaredField(lastCheck);
                Field periodicityField = trailer.getClass().getDeclaredField(periodicity);
                Field nextVisitField = trailer.getClass().getDeclaredField(nextVisit);
                Field typeField = trailer.getClass().getDeclaredField(type);

                insTypeField.setAccessible(true);
                lastCheckField.setAccessible(true);
                periodicityField.setAccessible(true);
                nextVisitField.setAccessible(true);
                typeField.setAccessible(true);

                insTypeField.set(trailer, inspection.getInspectionType());
                lastCheckField.set(trailer, inspection.getLastCheck());
                periodicityField.set(trailer, inspection.getPeriodicity());
                nextVisitField.set(trailer, inspection.getNextVisit());
                typeField.set(trailer, inspection.getType());
                inspCount++;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw e;
            }
        }

        return trailer;
    }

    public List<TrailerVO> getAllTrailers() throws IllegalAccessException {
        List<Trailer> trailers =  trailerRepository.findAll();
        List<TrailerVO> trailerVOList = new ArrayList<>();
        for(Trailer trailer: trailers){
            trailerVOList.add(getTrailerResponse(trailer));
        }
        return trailerVOList;
    }

    private TrailerVO getTrailerResponse(Trailer trailer) throws IllegalAccessException {
        TrailerVO trailerVO = new TrailerVO();
        if(trailer==null){
            return null;
        }

        trailerVO.setRowid(trailer.getRowid());
        trailerVO.setTrailer(trailer.getTrailer());
        trailerVO.setDes(trailer.getDes());
        trailerVO.setFcy(trailer.getFcy());
        trailerVO.setLinkTo(trailer.getLinkTo());
        trailerVO.setType(trailer.getType());
        trailerVO.setMake(trailer.getMake());
        trailerVO.setModel(trailer.getModel());
        trailerVO.setXtailgate(trailer.getXtailgate());
        trailerVO.setAnnee(trailer.getAnnee());
        trailerVO.setAasref(trailer.getAasref());
        trailerVO.setNbaxle(trailer.getNbaxle());
        trailerVO.setStyzon(trailer.getStyzon());
        trailerVO.setMaxLen(trailer.getMaxLen());
        trailerVO.setMaxWid(trailer.getMaxWid());
        trailerVO.setMaxLovol(trailer.getMaxLovol());
        trailerVO.setCurbWei(trailer.getCurbWei());
        trailerVO.setMaxFH(trailer.getMaxFH());
        trailerVO.setMaxLoams(trailer.getMaxLoams());
        trailerVO.setGvwr(trailer.getGvwr());
        trailerVO.setXbathght(trailer.getXbathght());
        trailerVO.setXgndocc(trailer.getXgndocc());
        trailerVO.setLastInsp(trailer.getLastInsp());
        trailerVO.setXrentable(trailer.getXrentable());
        trailerVO.setXsideope(trailer.getXsideope());
        trailerVO.setXuvycod(trailer.getXuvycod());
        trailerVO.setComment(trailer.getComment());
        trailerVO.setXstomgtcod(trailer.getXstomgtcod());
        trailerVO.setXlotmgtcod(trailer.getXlotmgtcod());
        trailerVO.setXseril(trailer.getXseril());

        String IMAGE_QUERY = "SELECT BLOB_0 FROM "+dbSchema+".CBLOB WHERE CODBLB_0= :codblob_0 AND IDENT1_0 = :ident1_0";

        Query imgQuery = entityManager.createNativeQuery(IMAGE_QUERY);
        imgQuery.setParameter("codblob_0", "XX10CTRA");
        imgQuery.setParameter("ident1_0", trailer.getTrailer());

        try {
            Object blob = imgQuery.getSingleResult();
            trailerVO.setImage(blob!=null?(byte[]) blob: null);
        } catch (NoResultException e) {
            trailerVO.setImage(null);
        }

        List<TrailerCompartment> compartmentList;
        String queryString = "select XTRAILER_0, XLINNUM_0, XCOMPARTMENT_0, " +
                "XCAPACITY_0 from "+dbSchema+".XX10CTRAILD where XTRAILER_0='"+trailer.getTrailer()+"' ";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> results = query.getResultList();
        compartmentList = results.stream()
                .map(result -> new TrailerCompartment(
                        result[0]!=null?result[0].toString():"",
                        result[1]!=null?(Integer) result[1]:null,
                        result[2]!=null?result[2].toString():"",
                        result[3]!=null?(Double)result[3]:null))
                .collect(Collectors.toList());

        trailerVO.setCompartmentList(compartmentList);

        List<TechnicalInspection> technicalInspectionList = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            String insTypeField = "xinsptyp" + i;
            String lastCheckField = "xlstchk" + i;
            String periodicityField = "xperiodicity"+i;
            String nextVisitField = "xnextvisit"+i;
            String typeField = "xtypein"+i;
            String insType = getFieldValue(trailer, insTypeField)!=null? (String) getFieldValue(trailer, insTypeField) :"";
            Date lastCheck = getFieldValue(trailer, lastCheckField)!=null? (Date) getFieldValue(trailer, lastCheckField) :null;
            Integer periodicity = getFieldValue(trailer, periodicityField)!=null? (int) getFieldValue(trailer, periodicityField) :null;
            Date nextVisit = getFieldValue(trailer, nextVisitField)!=null? (Date) getFieldValue(trailer, nextVisitField) :null;
            Integer type = getFieldValue(trailer, typeField)!=null? (int) getFieldValue(trailer, typeField) :null;

            technicalInspectionList.add(new TechnicalInspection(insType,lastCheck,periodicity,nextVisit,type));
        }
        trailerVO.setTechnicalInspectionList(technicalInspectionList);

        return trailerVO;
    }

    public TrailerVO getTrailerByTrailerCode(String trailer) throws IllegalAccessException {
        Trailer trailerObj =  trailerRepository.findByTrailer(trailer);
        return getTrailerResponse(trailerObj);
    }

    public void updateTrailer(TrailerVO trailerVO) throws NoSuchFieldException, IllegalAccessException {
        trailerRepository.save(getTrailerModel(trailerVO, false));
    }

    public String deleteTrailerByTrailerCode(String trailer) {
        trailerRepository.deleteByTrailer(trailer);
        String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CTRA' " +
                " and IDENT1_0 = :ident1";
        Query query = entityManager.createNativeQuery(deleteImage);
        query.setParameter("ident1", trailer);
        query.executeUpdate();
        return "success";
    }

    public boolean checkTrailerExists(String trailer) {
        return trailerRepository.existsByTrailer(trailer);
    }

    public VehClass createVehicleClass(VehicleClassVO vehicleClassVO) {
        VehClass vehClass = getVehicleClassModal(vehicleClassVO, true);
        return vehicleClassRepository.save(vehClass);
    }

    private VehClass getVehicleClassModal(VehicleClassVO vehicleClassVO, Boolean isCreate) {
        VehClass vehClass = new VehClass();
        vehClass.setRowid(isCreate?null: vehicleClassVO.getRowid());
        vehClass.setClassName(vehicleClassVO.getClassName());
        vehClass.setDesc(vehicleClassVO.getDesc());
        vehClass.setEnaFlag(vehicleClassVO.getEnaFlag());
        vehClass.setCry(vehicleClassVO.getCry());
        vehClass.setTyp(vehicleClassVO.getTyp());
        vehClass.setAxlnbr(vehicleClassVO.getAxlnbr());
        vehClass.setXmaxcapw(vehicleClassVO.getXmaxcapw());
        vehClass.setXmaxunit(vehicleClassVO.getXmaxunit());
        vehClass.setXmaxcapv(vehicleClassVO.getXmaxcapv());
        vehClass.setXmaxvunit(vehicleClassVO.getXmaxvunit());
        vehClass.setXskillno(vehicleClassVO.getXskillno());
        vehClass.setXinspin(vehicleClassVO.getXinspin());
        vehClass.setXinspout(vehicleClassVO.getXinspout());
        vehClass.setXmanin(vehicleClassVO.getXmanin());
        vehClass.setXmanout(vehicleClassVO.getXmanout());
        if(isCreate && vehicleClassVO.getImage()!=null){
            String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                    "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                    "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                    "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                    "VALUES (0, 'XX10CCLA', :codblb, '', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                    ":currentDate, :currentDate, :auuid, :image)";

            Query query = entityManager.createNativeQuery(insertImage);
            query.setParameter("codblb", vehicleClassVO.getClassName());
            query.setParameter("currentDate", new Date());
            query.setParameter("auuid", new byte[]{0});
            query.setParameter("image", vehicleClassVO.getImage());
            query.executeUpdate();
        }
        else if(vehicleClassVO.getImage()!=null){
            String getImage = "select IDENT1_0, BLOB_0 from "+dbSchema+".CBLOB where CODBLB_0='XX10CCLA' " +
                    "AND IDENT1_0='"+vehicleClassVO.getClassName()+"' ";
            List<Object[]> imageRes = entityManager.createNativeQuery(getImage).getResultList();
            if(!imageRes.isEmpty()){
                String updateImage = "UPDATE " + dbSchema + ".CBLOB  " +
                        "    SET BLOB_0 = :image, UPDDAT_0 = :currentDate, UPDDATTIM_0 = :currentDate  \n" +
                        "    WHERE CODBLB_0 = 'XX10CCLA' AND IDENT1_0 = :ident1 ";
                Query query = entityManager.createNativeQuery(updateImage);
                query.setParameter("image", vehicleClassVO.getImage());
                query.setParameter("currentDate", new Date());
                query.setParameter("ident1", vehicleClassVO.getClassName());
                query.executeUpdate();
            }else{
                String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                        "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                        "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                        "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                        "VALUES (0, 'XX10CCLA', :codblb, '', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                        ":currentDate, :currentDate, :auuid, :image)";
                Query query = entityManager.createNativeQuery(insertImage);
                query.setParameter("codblb", vehicleClassVO.getClassName());
                query.setParameter("currentDate", new Date());
                query.setParameter("auuid", new byte[]{0});
                query.setParameter("image", vehicleClassVO.getImage());
                query.executeUpdate();
            }
        }else if(!isCreate && vehicleClassVO.getImage()==null){
            String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CCLA' " +
                    "and IDENT1_0 = :ident1";
            Query query = entityManager.createNativeQuery(deleteImage);
            query.setParameter("ident1", vehicleClassVO.getClassName());
            query.executeUpdate();
        }
        return vehClass;
    }

    public boolean checkVehicleClassExists(String className) {
        return vehicleClassRepository.existsByClassName(className);
    }

    public List<VehicleClassVO> getAllVehicleClassList() {
        List<VehicleClassVO> vehicleVOList = new ArrayList<>();
        List<VehClass> vehicles = vehicleClassRepository.findAll();
        for(VehClass vehicleClass: vehicles){
            vehicleVOList.add(getVehicleClassResponse(vehicleClass));
        }
        return vehicleVOList;
    }

    private VehicleClassVO getVehicleClassResponse(VehClass vehClass) {
        VehicleClassVO vehicleClassVO = new VehicleClassVO();

        if(vehClass==null){
         return null;
        }
        vehicleClassVO.setRowid(vehClass.getRowid());
        vehicleClassVO.setClassName(vehClass.getClassName());
        vehicleClassVO.setDesc(vehClass.getDesc());
        vehicleClassVO.setEnaFlag(vehClass.getEnaFlag());
        vehicleClassVO.setCry(vehClass.getCry());
        vehicleClassVO.setTyp(vehClass.getTyp());
        vehicleClassVO.setAxlnbr(vehClass.getAxlnbr());
        vehicleClassVO.setXmaxcapw(vehClass.getXmaxcapw());
        vehicleClassVO.setXmaxunit(vehClass.getXmaxunit());
        vehicleClassVO.setXmaxcapv(vehClass.getXmaxcapv());
        vehicleClassVO.setXmaxvunit(vehClass.getXmaxvunit());
        vehicleClassVO.setXskillno(vehClass.getXskillno());
        vehicleClassVO.setXinspin(vehClass.getXinspin());
        vehicleClassVO.setXinspout(vehClass.getXinspout());
        vehicleClassVO.setXmanin(vehClass.getXmanin());
        vehicleClassVO.setXmanout(vehClass.getXmanout());

        String IMAGE_QUERY = "SELECT BLOB_0 FROM "+dbSchema+".CBLOB WHERE CODBLB_0= :codblob_0 AND IDENT1_0 = :ident1_0";

        Query imgQuery = entityManager.createNativeQuery(IMAGE_QUERY);
        imgQuery.setParameter("codblob_0", "XX10CCLA");
        imgQuery.setParameter("ident1_0", vehClass.getClassName());

        try {
            Object blob = imgQuery.getSingleResult();
            vehicleClassVO.setImage(blob!=null?(byte[]) blob: null);
        } catch (NoResultException e) {
            vehicleClassVO.setImage(null);
        }

        String queryString = "select XTRACOD_0, XDES_0, XMAXCAPW_0, XMAXUNIT_0, XMAXCAPV_0, XMAXVUNIT_0  from "+dbSchema+".XX10CXTRA \n" +
                "where XTRACOD_0 in( select XTRAILER_0 from "+dbSchema+".XX10CXASSOC1 where XCLASS_0 = '"+vehClass.getClassName()+"')";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> results = query.getResultList();
        List<VehicleAssociation> associationList = results.stream()
                .map(result -> new VehicleAssociation( "Trailer",
                        result[0]!=null?result[0].toString():"",
                        result[1]!=null?result[1].toString():"",
                        result[2]!=null?(BigDecimal) result[2]:null,
                        result[3]!=null?result[3].toString():"",
                        "",
                        result[4]!=null?(BigDecimal) result[4]:null,
                        result[5]!=null?result[5].toString():"",
                        ""))
                .collect(Collectors.toList());

        List<String> uomList = Stream.concat(
                        associationList.stream().map(VehicleAssociation::getWeightUOM),
                        associationList.stream().map(VehicleAssociation::getVolumeUOM)
                )
                .collect(Collectors.toList());

        String uomQueryString = "SELECT IDENT1_0, TEXTE_0 FROM "+dbSchema+".ATEXTRA " +
                "WHERE CODFIC_0='TABUNIT' AND ZONE_0='DES' AND IDENT1_0 IN :ident1List " +
                "AND LANGUE_0='ENG'";

        Query uomQuery = entityManager.createNativeQuery(uomQueryString);
        uomQuery.setParameter("ident1List", uomList);
        List<Object[]> uomResults =  uomQuery.getResultList();
        List<DropdownData> uomData = uomResults.stream()
                .map(result -> new DropdownData(
                        result[0]!=null?result[0].toString():"",
                        result[1]!=null?result[1].toString():""
                ))
                .collect(Collectors.toList());

        for(VehicleAssociation association: associationList){
            Optional<DropdownData> weightUom = uomData.stream().filter(x->x.getValue().equals(association.getWeightUOM())).findFirst();
            weightUom.ifPresent(dropdownData -> association.setWeightUOMDesc(dropdownData.getLabel()));

            Optional<DropdownData> volumeUom = uomData.stream().filter(x->x.getValue().equals(association.getVolumeUOM())).findFirst();
            volumeUom.ifPresent(dropdownData -> association.setVolumeUOMDesc(dropdownData.getLabel()));
        }

        vehicleClassVO.setAssociationList(associationList);

        return vehicleClassVO;
    }

    public VehicleClassVO getVehicleClassByClass(String className) {
        VehClass vehClass =  vehicleClassRepository.findByClassName(className);
        return getVehicleClassResponse(vehClass);
    }

    public void updateVehicleClass(VehicleClassVO vehicleClassVO) {
        VehClass vehClass = getVehicleClassModal(vehicleClassVO, false);
        vehicleClassRepository.save(vehClass);
    }

    public String deleteVehicleClassByClass(String className) {
        vehicleClassRepository.deleteByClassName(className);
        String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CCLA' " +
                " and IDENT1_0 = :ident1";
        Query query = entityManager.createNativeQuery(deleteImage);
        query.setParameter("ident1", className);
        query.executeUpdate();
        return "success";
    }

    public boolean checkVehicleExists(String codeyve) {
        return vehicleRepository.existsByCodeyve(codeyve);
    }

    public boolean checkDriverExists(String driverId) {
        return driverRepository.existsByDriverId(driverId);
    }

    @Transactional
    public FleetDriver createDriver(FleetDriverVO driverVO) throws NoSuchFieldException, IllegalAccessException {
         return driverRepository.save(getDriverModelForCreateOrUpdate(driverVO, true));
    }

    private FleetDriver getDriverModelForCreateOrUpdate(FleetDriverVO driverVO, boolean isCreate) throws NoSuchFieldException, IllegalAccessException {
        FleetDriver driver;
        if(isCreate){
            driver= new FleetDriver();
        } else {
            driver=entityManager.find(FleetDriver.class, driverVO.getRowId());
            if(driver==null) {
                throw new EntityNotFoundException("Driver not found with this rowID: "+driverVO.getDriverId());
            }
        }
        if (isCreate) {
            driver.setDriverId(driverVO.getDriverId());
        }
        System.out.println("xsalerep = " + driver.getXsalerep());
        driver.setDriver(driverVO.getDriver());
        driver.setFcy(driverVO.getFcy());
        driver.setActive(driverVO.getActive());
        driver.setXsalesrep(driverVO.getXsalesrep());
        driver.setXdriver(driverVO.getXdriver());
        driver.setX10csup(driverVO.getX10csup());
        driver.setBptnum(driverVO.getBptnum());
        driver.setXbus(driverVO.getXbus());
        driver.setLanmain(driverVO.getLanmain());
        driver.setBir(driverVO.getBir());
        driver.setLastvime(driverVO.getLastvime());
        driver.setDelivby(driverVO.getDelivby());
        driver.setXnooftrips(driverVO.getXnooftrips());
        driver.setXper(driverVO.getXper());
        driver.setLicenum(driverVO.getLicenum());
        driver.setLicetyp(driverVO.getLicetyp());
        driver.setLicedat(driverVO.getLicedat());
        driver.setValidat(driverVO.getValidat());
        driver.setXlncstarttim(driverVO.getXlncstarttim());
        driver.setXlncdur(driverVO.getXlncdur());
        driver.setStyzon(driverVO.getStyzon());
        driver.setXuvycod(driverVO.getXuvycod());
        driver.setBpaaddlig0(driverVO.getBpaaddlig0());
        driver.setBpaaddlig2(driverVO.getBpaaddlig2());
        driver.setBpaaddlig1(driverVO.getBpaaddlig1());
        driver.setCry(driverVO.getCry());
        driver.setPoscod(driverVO.getPoscod());
        driver.setCty(driverVO.getCty());
        driver.setSat(driverVO.getSat());
        driver.setMob(driverVO.getMob());
        driver.setTel0(driverVO.getTel0());
        driver.setWeb(driverVO.getWeb());
        driver.setXuser(driverVO.getXuser());
        driver.setXpwd(driverVO.getXpwd());
        driver.setXskpcon(driverVO.getXskpcon());
        driver.setXrescon(driverVO.getXrescon());
        driver.setXqtychgcon(driverVO.getXqtychgcon());
        driver.setXspotcon(driverVO.getXspotcon());
        driver.setXsihcon(driverVO.getXsihcon());
        driver.setXpaycon(driverVO.getXpaycon());
        driver.setXgeocon(driverVO.getXgeocon());
        driver.setNote(driverVO.getNote());
        driver.setXcondriv(driverVO.getXcondriv());
        driver.setXlonghaul(driverVO.getXlonghaul());
        driver.setX1cunion(driverVO.getX1cunion());
        driver.setX1coverhrs(driverVO.getX1coverhrs());
        driver.setXmaxhrsday(driverVO.getXmaxhrsday());
        driver.setXmaxhrsweek(driverVO.getXmaxhrsweek());
        driver.setXdriverhrs(driverVO.getXdriverhrs());
        driver.setX10cmon(driverVO.getX10cmon());
        driver.setX10ctues(driverVO.getX10ctues());
        driver.setX10cwed(driverVO.getX10cwed());
        driver.setX10cthu(driverVO.getX10cthu());
        driver.setX10cfri(driverVO.getX10cfri());
        driver.setX10csat(driverVO.getX10csat());
        driver.setX10csun(driverVO.getX10csun());
        driver.setXallvehicle(driverVO.getXallvehicle());
        driver.setXdocno(driverVO.getXdocno());

        if(isCreate && driverVO.getImage()!=null){
            String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                    "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                    "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                    "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                    "VALUES (0, 'XX10CDR', :driverId, '', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                    ":currentDate, :currentDate, :auuid, :image)";

            Query query = entityManager.createNativeQuery(insertImage);
            query.setParameter("driverId", driverVO.getDriverId());
            query.setParameter("currentDate", new Date());
            query.setParameter("auuid", new byte[]{0});
            query.setParameter("image", driverVO.getImage());
            query.executeUpdate();
        }else if(driverVO.getImage()!=null){
            String getImage = "select IDENT1_0, BLOB_0 from "+dbSchema+".CBLOB where CODBLB_0='XX10CDR' " +
                    "AND IDENT1_0='"+driverVO.getDriverId()+"' ";
            List<Object[]> imageRes = entityManager.createNativeQuery(getImage).getResultList();
            if(!imageRes.isEmpty()){
                String updateImage = "UPDATE " + dbSchema + ".CBLOB  " +
                        "    SET BLOB_0 = :image, UPDDAT_0 = :currentDate, UPDDATTIM_0 = :currentDate  \n" +
                        "    WHERE CODBLB_0 = 'XX10CDR' AND IDENT1_0 = :ident1 ";
                Query query = entityManager.createNativeQuery(updateImage);
                query.setParameter("image", driverVO.getImage());
                query.setParameter("currentDate", new Date());
                query.setParameter("ident1", driverVO.getDriverId());
                query.executeUpdate();
            }else{
                String insertImage = "INSERT INTO " + dbSchema + ".CBLOB (" +
                        "UPDTICK_0, CODBLB_0, IDENT1_0, IDENT2_0, NAMBLB_0, TYPBLB_0, CREUSR_0, " +
                        "CREDAT_0, CRETIM_0, UPDUSR_0, UPDDAT_0, UPDTIM_0, CREDATTIM_0, " +
                        "UPDDATTIM_0, AUUID_0, BLOB_0) " +
                        "VALUES (0, 'XX10CDR', :codblb, '', '', 3, '', :currentDate, '', '', :currentDate, '', " +
                        ":currentDate, :currentDate, :auuid, :image)";
                Query query = entityManager.createNativeQuery(insertImage);
                query.setParameter("codblb", driverVO.getDriverId());
                query.setParameter("currentDate", new Date());
                query.setParameter("auuid", new byte[]{0});
                query.setParameter("image", driverVO.getImage());
                query.executeUpdate();
            }
        }else if(!isCreate && driverVO.getImage()==null){
            String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CDR' " +
                    " and IDENT1_0 = :ident1";
            Query query = entityManager.createNativeQuery(deleteImage);
            query.setParameter("ident1", driverVO.getDriverId());
            query.executeUpdate();
        }

        if(isCreate){
            if(driverVO.getDocumentList() != null){
                int lineNum=1;
                for(Document document: driverVO.getDocumentList()) {
                    String query = "insert into " + dbSchema + ".XX10CDRIVERD " +
                            "(UPDTICK_0, LINNUM_0, GTPASS_0, ISSAUTHORITY_0, ISSEMIRATE_0, PASSDURATION_0, FEES_0, CURRENCY_0, EXPDATE_0, DRIVERID_0, CREDATTIM_0, UPDDATTIM_0, AUUID_0, " +
                            "CREUSR_0, UPDUSR_0, XDOCNO_0, XDOCUP_0, XTYPE_0) " +
                            "values (0, :lineNum, :docType, :issuingAuthority, '', :issuingDate, 0.0, '', :expiration, :driverId, :credAtTime, :updAtTime, :uuid, '', '', :docNum, '', 1)";

                    Query nativeQuery = entityManager.createNativeQuery(query);

                    nativeQuery.setParameter("lineNum", lineNum * 1000);
                    nativeQuery.setParameter("docType", document.getDocType());
                    nativeQuery.setParameter("issuingAuthority", document.getIssuingAuthority());
                    nativeQuery.setParameter("issuingDate", document.getIssuingDate());
                    nativeQuery.setParameter("expiration", document.getExpiration());
                    nativeQuery.setParameter("driverId", driverVO.getDriverId());
                    nativeQuery.setParameter("credAtTime", new Date());
                    nativeQuery.setParameter("updAtTime", new Date());
                    nativeQuery.setParameter("uuid", new byte[]{0});
                    nativeQuery.setParameter("docNum", document.getDocNum());

                    nativeQuery.executeUpdate();

                    lineNum++;
                }
            }
        }else if(driverVO.getDocumentList()==null || driverVO.getDocumentList().isEmpty()){
            String deleteQuery = "DELETE from "+dbSchema+".XX10CDRIVERD where DRIVERID_0= '"+driverVO.getDriverId()+"'";
            entityManager.createNativeQuery(deleteQuery).executeUpdate();
        } else{
            String docQuery = "SELECT GTPASS_0,  XDOCNO_0, ISSAUTHORITY_0, PASSDURATION_0, EXPDATE_0 , LINNUM_0\n" +
                    "from "+dbSchema+".XX10CDRIVERD where DRIVERID_0='"+driverVO.getDriverId()+"' order by LINNUM_0";

            Query query = entityManager.createNativeQuery(docQuery);
            List<Object[]> results = query.getResultList();
            List<Document> documentList = results.stream()
                    .map(result -> new Document(
                            result[0]!=null?result[0].toString():"",
                            result[1]!=null?result[1].toString():"",
                            result[2]!=null?result[2].toString():null,
                            result[3]!=null?(Date) result[3]:null,
                            result[4]!=null?(Date) result[4]:null,
                            result[5]!=null?(Integer) result[5]:null
                    ))
                    .collect(Collectors.toList());

            int documentCount=0;
            for(Document document: driverVO.getDocumentList()){
                if(documentList.size()>= (documentCount+1)) {
                    if (!document.getDocType().equals(documentList.get(documentCount).getDocType()) ||
                            !document.getIssuingAuthority().equals(documentList.get(documentCount).getIssuingAuthority()) ||
                            !document.getIssuingDate().equals(documentList.get(documentCount).getIssuingDate()) ||
                            !document.getExpiration().equals(documentList.get(documentCount).getExpiration()) ||
                            !document.getDocNum().equals(documentList.get(documentCount).getDocNum())) {

                        if (!document.getLineNum().equals(documentList.get(documentCount).getLineNum())) {
                            String insertQuery = "insert into "+dbSchema+".XX10CDRIVERD " +
                                    "(UPDTICK_0, LINNUM_0, GTPASS_0, ISSAUTHORITY_0, ISSEMIRATE_0, PASSDURATION_0, FEES_0, CURRENCY_0, EXPDATE_0, DRIVERID_0, CREDATTIM_0, UPDDATTIM_0, AUUID_0, " +
                                    "CREUSR_0, UPDUSR_0, XDOCNO_0, XDOCUP_0, XTYPE_0) " +
                                    "values (0, :lineNum, :docType, :issuingAuthority, '', :issuingDate, 0.0, '', :expiration, :driverId, :credAtTime, :updAtTime, :uuid, '', '', :docNum, '', 1)";

                            Query nativeQuery = entityManager.createNativeQuery(insertQuery);

                            nativeQuery.setParameter("lineNum", document.getLineNum());
                            nativeQuery.setParameter("docType", document.getDocType());
                            nativeQuery.setParameter("issuingAuthority", document.getIssuingAuthority());
                            nativeQuery.setParameter("issuingDate", document.getIssuingDate());
                            nativeQuery.setParameter("expiration", document.getExpiration());
                            nativeQuery.setParameter("driverId", driverVO.getDriverId());
                            nativeQuery.setParameter("credAtTime", new Date());
                            nativeQuery.setParameter("updAtTime", new Date());
                            nativeQuery.setParameter("uuid", new byte[]{0});
                            nativeQuery.setParameter("docNum", document.getDocNum());
                            nativeQuery.executeUpdate();
                        } else {
                            String updateQuery = "UPDATE "+dbSchema+".XX10CDRIVERD " +
                                    "SET GTPASS_0 = :docType, " +
                                    "ISSAUTHORITY_0 = :issuingAuthority, " +
                                    "PASSDURATION_0 = :issuingDate, " +
                                    "EXPDATE_0 = :expiration, " +
                                    "UPDDATTIM_0 = :updAtTime, " +
                                    "AUUID_0 = :uuid, " +
                                    "XDOCNO_0 = :docNum, " +
                                    "XTYPE_0 = 1 " +
                                    "WHERE LINNUM_0 = :lineNum AND DRIVERID_0 = :driverId";

                            Query nativeQuery = entityManager.createNativeQuery(updateQuery);

                            nativeQuery.setParameter("lineNum", document.getLineNum());
                            nativeQuery.setParameter("docType", document.getDocType());
                            nativeQuery.setParameter("issuingAuthority", document.getIssuingAuthority());
                            nativeQuery.setParameter("issuingDate", document.getIssuingDate());
                            nativeQuery.setParameter("expiration", document.getExpiration());
                            nativeQuery.setParameter("driverId", driverVO.getDriverId());
                            nativeQuery.setParameter("updAtTime", new Date());
                            nativeQuery.setParameter("uuid", new byte[]{0});
                            nativeQuery.setParameter("docNum", document.getDocNum());

                            nativeQuery.executeUpdate();
                        }
                    }
                }else{
                    String insertQuery = "insert into "+dbSchema+".XX10CDRIVERD " +
                            "(UPDTICK_0, LINNUM_0, GTPASS_0, ISSAUTHORITY_0, ISSEMIRATE_0, PASSDURATION_0, FEES_0, CURRENCY_0, EXPDATE_0, DRIVERID_0, CREDATTIM_0, UPDDATTIM_0, AUUID_0, " +
                            "CREUSR_0, UPDUSR_0, XDOCNO_0, XDOCUP_0, XTYPE_0) " +
                            "values (0, :lineNum, :docType, :issuingAuthority, '', :issuingDate, 0.0, '', :expiration, :driverId, :credAtTime, :updAtTime, :uuid, '', '', :docNum, '', 1)";

                    Query nativeQuery = entityManager.createNativeQuery(insertQuery);

                    nativeQuery.setParameter("lineNum", document.getLineNum());
                    nativeQuery.setParameter("docType", document.getDocType());
                    nativeQuery.setParameter("issuingAuthority", document.getIssuingAuthority());
                    nativeQuery.setParameter("issuingDate", document.getIssuingDate());
                    nativeQuery.setParameter("expiration", document.getExpiration());
                    nativeQuery.setParameter("driverId", driverVO.getDriverId());
                    nativeQuery.setParameter("credAtTime", new Date());
                    nativeQuery.setParameter("updAtTime", new Date());
                    nativeQuery.setParameter("uuid", new byte[]{0});
                    nativeQuery.setParameter("docNum", document.getDocNum());
                    nativeQuery.executeUpdate();
                }
                documentCount++;
            }

            List<Integer> existingLineNum = documentList.stream().map(Document::getLineNum).collect(Collectors.toList());
            List<Integer> currentLineNum = driverVO.getDocumentList().stream().map(Document::getLineNum).collect(Collectors.toList());
            for(Integer lineNum: existingLineNum){
                if(!currentLineNum.contains(lineNum)){
                    String deleteQuery = "DELETE from "+dbSchema+".XX10CDRIVERD where DRIVERID_0= '"+driverVO.getDriverId()+"' and LINNUM_0="+lineNum+" ";
                    entityManager.createNativeQuery(deleteQuery).executeUpdate();
                }
            }
        }

        if(driverVO.getSiteList() != null) {
            int siteCount = 0;
            for (VehicleTable site : driverVO.getSiteList()) {
                try {
                    String siteFieldName = "xfcy" + siteCount++;
                    Field siteField = driver.getClass().getDeclaredField(siteFieldName);
                    siteField.setAccessible(true);
                    siteField.set(driver, site.getId());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }

        if(driverVO.getXallvehicle()!=2 && driverVO.getVehicleClassList() != null) {
            int vehicleClassCount = 0;
            for(VehicleTable vehicleClass : driverVO.getVehicleClassList()){
                try {
                    String vehClassFieldName = "xvehicleclas" + vehicleClassCount++;
                    Field vehClassField = driver.getClass().getDeclaredField(vehClassFieldName);
                    vehClassField.setAccessible(true);
                    vehClassField.set(driver, vehicleClass.getId());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }

        if(driverVO.getUnavailableDaysList() != null && !driverVO.getUnavailableDaysList().isEmpty()){
            driver.setXuvystrdat(driverVO.getUnavailableDaysList().get(0).getStartDate());
            driver.setXuvyenddat(driverVO.getUnavailableDaysList().get(0).getEndDate());
            driver.setXuvycod(driverVO.getUnavailableDaysList().get(0).getDesc());
        }

        return driver;
    }

    public FleetDriverVO getDriverById(String driverId) throws IllegalAccessException {
        FleetDriver driver = driverRepository.findByDriverId(driverId);
        if(driver == null) {
            return null;
        }
        Map<String, String> siteMap = commonRepository.getSiteList().stream().collect(Collectors.toMap(e-> (String) e.getValue(), DropdownData::getLabel));
        Map<String, String> vehicleClassMap = commonRepository.getVehicleClassList().stream().collect(Collectors.toMap(e -> (String) e.getValue(), DropdownData::getLabel));
        return getDriverResponse1(driver, siteMap, vehicleClassMap);
    }

    public void updateDriver(FleetDriverVO driverVO) throws NoSuchFieldException, IllegalAccessException {
        driverRepository.save(getDriverModelForCreateOrUpdate(driverVO, false));
    }

    public String deleteDriverById(String driverId) {
        driverRepository.deleteByDriverId(driverId);
        String deleteImage = "delete from TMSNEW.CBLOB where CODBLB_0 = 'XX10CDR' " +
                " and IDENT1_0 = :ident1";
        Query query = entityManager.createNativeQuery(deleteImage);
        query.setParameter("ident1", driverId);
        query.executeUpdate();

        String deleteDocument = "delete from TMSNEW.XX10CDRIVERD where DRIVERID_0 = :driverId ";
        Query deleteDocumentQuery = entityManager.createNativeQuery(deleteDocument);
        deleteDocumentQuery.setParameter("driverId", driverId);
        deleteDocumentQuery.executeUpdate();

        return "success";
    }

    //Optimized method for reducing time for response --Added by Shubham
    public List<FleetDriverVO> getAllDriversList1() throws IllegalAccessException {
        List<FleetDriver> driverList = driverRepository.findAll();
        List<FleetDriverVO> fleetDriverVOList = new ArrayList<>();

        Map<String, String> siteMap=commonRepository.getSiteList().stream().collect(Collectors.toMap(e -> (String) e.getValue(),DropdownData::getLabel));
        Map<String, String> vehicleClassMap = commonRepository.getVehicleClassList().stream().collect(Collectors.toMap(e -> (String) e.getValue(), DropdownData::getLabel));
        for(FleetDriver driver: driverList){
            fleetDriverVOList.add(getDriverResponse1(driver, siteMap, vehicleClassMap));
        }
        return fleetDriverVOList;
    }
    public FleetDriverVO getDriverResponse1(FleetDriver driver,
                                            Map<String, String> siteMap,
                                            Map<String, String> vehicleClassMap) throws IllegalAccessException {
        FleetDriverVO driverVO = new FleetDriverVO();
        if(driver==null){
            return null;
        }

        driverVO.setRowId(driver.getRowId());
        driverVO.setDriverId(driver.getDriverId());
        driverVO.setDriver(driver.getDriver());
        driverVO.setFcy(driver.getFcy());
        driverVO.setActive(driver.getActive());
        driverVO.setXsalesrep(driver.getXsalesrep());
        driverVO.setXdriver(driver.getXdriver());
        driverVO.setX10csup(driver.getX10csup());
        driverVO.setBptnum(driver.getBptnum());
        driverVO.setXbus(driver.getXbus());
        driverVO.setLanmain(driver.getLanmain());
        driverVO.setBir(driver.getBir());
        driverVO.setLastvime(driver.getLastvime());
        driverVO.setDelivby(driver.getDelivby());
        driverVO.setXnooftrips(driver.getXnooftrips());
        driverVO.setXper(driver.getXper());
        driverVO.setLicenum(driver.getLicenum());
        driverVO.setLicetyp(driver.getLicetyp());
        driverVO.setLicedat(driver.getLicedat());
        driverVO.setValidat(driver.getValidat());
        driverVO.setXlncstarttim(driver.getXlncstarttim());
        driverVO.setXlncdur(driver.getXlncdur());
        driverVO.setStyzon(driver.getStyzon());
        driverVO.setXuvycod(driver.getXuvycod());
        driverVO.setBpaaddlig0(driver.getBpaaddlig0());
        driverVO.setBpaaddlig2(driver.getBpaaddlig2());
        driverVO.setBpaaddlig1(driver.getBpaaddlig1());
        driverVO.setCry(driver.getCry());
        driverVO.setPoscod(driver.getPoscod());
        driverVO.setCty(driver.getCty());
        driverVO.setSat(driver.getSat());
        driverVO.setMob(driver.getMob());
        driverVO.setTel0(driver.getTel0());
        driverVO.setWeb(driver.getWeb());
        driverVO.setXuser(driver.getXuser());
        driverVO.setXpwd(driver.getXpwd());
        driverVO.setXskpcon(driver.getXskpcon());
        driverVO.setXrescon(driver.getXrescon());
        driverVO.setXqtychgcon(driver.getXqtychgcon());
        driverVO.setXspotcon(driver.getXspotcon());
        driverVO.setXsihcon(driver.getXsihcon());
        driverVO.setXpaycon(driver.getXpaycon());
        driverVO.setXgeocon(driver.getXgeocon());
        driverVO.setNote(driver.getNote());
        driverVO.setXcondriv(driver.getXcondriv());
        driverVO.setXlonghaul(driver.getXlonghaul());
        driverVO.setX1cunion(driver.getX1cunion());
        driverVO.setX1coverhrs(driver.getX1coverhrs());
        driverVO.setXmaxhrsday(driver.getXmaxhrsday());
        driverVO.setXmaxhrsweek(driver.getXmaxhrsweek());
        driverVO.setXdriverhrs(driver.getXdriverhrs());
        driverVO.setX10cmon(driver.getX10cmon());
        driverVO.setX10ctues(driver.getX10ctues());
        driverVO.setX10cwed(driver.getX10cwed());
        driverVO.setX10cthu(driver.getX10cthu());
        driverVO.setX10cfri(driver.getX10cfri());
        driverVO.setX10csat(driver.getX10csat());
        driverVO.setX10csun(driver.getX10csun());
        driverVO.setXallvehicle(driver.getXallvehicle());
        driverVO.setXdocno(driver.getXdocno());

        //Image fetch
        String IMAGE_QUERY = "SELECT BLOB_0 FROM "+dbSchema+".CBLOB WHERE CODBLB_0= :codblob_0  AND IDENT1_0 = :ident1_0";

        Query imgQuery = entityManager.createNativeQuery(IMAGE_QUERY);
        imgQuery.setParameter("codblob_0", "XX10CDR");
        imgQuery.setParameter("ident1_0", driver.getDriverId());

        try {
            Object blob = imgQuery.getSingleResult();
            driverVO.setImage(blob!=null?(byte[]) blob: null);
        } catch (NoResultException e) {
            driverVO.setImage(null);
        }

        //Document fetch
        String queryString = "select GTPASS_0,  XDOCNO_0, ISSAUTHORITY_0, PASSDURATION_0, EXPDATE_0 , LINNUM_0" +
                " from "+dbSchema+".XX10CDRIVERD where DRIVERID_0='"+driver.getDriverId()+"'";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> results = query.getResultList();
        List<Document> documentList = results.stream()
                .map(result -> new Document(
                        result[0]!=null?result[0].toString():"",
                        result[1]!=null?result[1].toString():"",
                        result[2]!=null?result[2].toString():null,
                        result[3]!=null?(Date) result[3]:null,
                        result[4]!=null?(Date) result[4]:null,
                        result[5]!=null?(Integer) result[5]:null
                ))
                .collect(Collectors.toList());
        driverVO.setDocumentList(documentList);

        //Site list optimization
        List<VehicleTable> siteList = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            String siteField = "xfcy" + i;
            String site = getFieldValue(driver, siteField)!=null?
                    (String) getFieldValue(driver, siteField) :"";

            if(site != null && !site.trim().isEmpty()) {
                siteList.add(new VehicleTable(site, siteMap.getOrDefault(site,"")));
            }
        }
        driverVO.setSiteList(siteList);

        //Vehicle Class Optimization
        List<VehicleTable> vehicleClassList = new ArrayList<>();
        if(driver.getXallvehicle()!=2){
            for (int i = 0; i <= 9; i++) {
                String vehicleClassField = "xvehicleclas" + i;
                String vehicleClass = getFieldValue(driver, vehicleClassField)!=null?
                        (String) getFieldValue(driver, vehicleClassField) :"";

                if(vehicleClass != null && !vehicleClass.trim().isEmpty()){
                    vehicleClassList.add(new VehicleTable(vehicleClass,vehicleClassMap.getOrDefault(vehicleClass,"")));
                }
            }
        }
        driverVO.setVehicleClassList(vehicleClassList);

        //Unavailable days
        Date startDate = getFieldValue(driver, "xuvystrdat")!=null?
                (Date) getFieldValue(driver, "xuvystrdat") : null;
        Date endDate = getFieldValue(driver, "xuvyenddat")!=null?
                (Date) getFieldValue(driver, "xuvyenddat") : null;
        driverVO.setUnavailableDaysList(Collections.singletonList(new UnavailableDays(startDate, endDate, "")));
        return driverVO;
    }


    public List<FleetDriverVO> getAllDriversList() throws IllegalAccessException {
        List<FleetDriver> driverList = driverRepository.findAll();
        List<FleetDriverVO> fleetDriverVOList = new ArrayList<>();
        for(FleetDriver driver: driverList){
            fleetDriverVOList.add(getDriverResponse(driver));
        }
        return fleetDriverVOList;
    }
    public FleetDriverVO getDriverResponse(FleetDriver driver) throws IllegalAccessException {
        FleetDriverVO driverVO = new FleetDriverVO();
        if(driver==null){
            return null;
        }

        driverVO.setRowId(driver.getRowId());
        driverVO.setDriverId(driver.getDriverId());
        driverVO.setDriver(driver.getDriver());
        driverVO.setFcy(driver.getFcy());
        driverVO.setActive(driver.getActive());
        driverVO.setXsalesrep(driver.getXsalerep());
        driverVO.setXdriver(driver.getXdriver());
        driverVO.setX10csup(driver.getX10csup());
        driverVO.setBptnum(driver.getBptnum());
        driverVO.setXbus(driver.getXbus());
        driverVO.setLanmain(driver.getLanmain());
        driverVO.setBir(driver.getBir());
        driverVO.setLastvime(driver.getLastvime());
        driverVO.setDelivby(driver.getDelivby());
        driverVO.setXnooftrips(driver.getXnooftrips());
        driverVO.setXper(driver.getXper());
        driverVO.setLicenum(driver.getLicenum());
        driverVO.setLicetyp(driver.getLicetyp());
        driverVO.setLicedat(driver.getLicedat());
        driverVO.setValidat(driver.getValidat());
        driverVO.setXlncstarttim(driver.getXlncstarttim());
        driverVO.setXlncdur(driver.getXlncdur());
        driverVO.setStyzon(driver.getStyzon());
        driverVO.setXuvycod(driver.getXuvycod());
        driverVO.setBpaaddlig0(driver.getBpaaddlig0());
        driverVO.setBpaaddlig2(driver.getBpaaddlig2());
        driverVO.setBpaaddlig1(driver.getBpaaddlig1());
        driverVO.setCry(driver.getCry());
        driverVO.setPoscod(driver.getPoscod());
        driverVO.setCty(driver.getCty());
        driverVO.setSat(driver.getSat());
        driverVO.setMob(driver.getMob());
        driverVO.setTel0(driver.getTel0());
        driverVO.setWeb(driver.getWeb());
        driverVO.setXuser(driver.getXuser());
        driverVO.setXpwd(driver.getXpwd());
        driverVO.setXskpcon(driver.getXskpcon());
        driverVO.setXrescon(driver.getXrescon());
        driverVO.setXqtychgcon(driver.getXqtychgcon());
        driverVO.setXspotcon(driver.getXspotcon());
        driverVO.setXsihcon(driver.getXsihcon());
        driverVO.setXpaycon(driver.getXpaycon());
        driverVO.setXgeocon(driver.getXgeocon());
        driverVO.setNote(driver.getNote());
        driverVO.setXcondriv(driver.getXcondriv());
        driverVO.setXlonghaul(driver.getXlonghaul());
        driverVO.setX1cunion(driver.getX1cunion());
        driverVO.setX1coverhrs(driver.getX1coverhrs());
        driverVO.setXmaxhrsday(driver.getXmaxhrsday());
        driverVO.setXmaxhrsweek(driver.getXmaxhrsweek());
        driverVO.setXdriverhrs(driver.getXdriverhrs());
        driverVO.setX10cmon(driver.getX10cmon());
        driverVO.setX10ctues(driver.getX10ctues());
        driverVO.setX10cwed(driver.getX10cwed());
        driverVO.setX10cthu(driver.getX10cthu());
        driverVO.setX10cfri(driver.getX10cfri());
        driverVO.setX10csat(driver.getX10csat());
        driverVO.setX10csun(driver.getX10csun());
        driverVO.setXallvehicle(driver.getXallvehicle());
        driverVO.setXdocno(driver.getXdocno());

        String IMAGE_QUERY = "SELECT BLOB_0 FROM "+dbSchema+".CBLOB WHERE CODBLB_0= :codblob_0  AND IDENT1_0 = :ident1_0";

        Query imgQuery = entityManager.createNativeQuery(IMAGE_QUERY);
        imgQuery.setParameter("codblob_0", "XX10CDR");
        imgQuery.setParameter("ident1_0", driver.getDriverId());

        try {
            Object blob = imgQuery.getSingleResult();
            driverVO.setImage(blob!=null?(byte[]) blob: null);
        } catch (NoResultException e) {
            driverVO.setImage(null);
        }

        String queryString = "select GTPASS_0,  XDOCNO_0, ISSAUTHORITY_0, PASSDURATION_0, EXPDATE_0 , LINNUM_0" +
                " from "+dbSchema+".XX10CDRIVERD where DRIVERID_0='"+driver.getDriverId()+"'";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> results = query.getResultList();
        List<Document> documentList = results.stream()
                .map(result -> new Document(
                        result[0]!=null?result[0].toString():"",
                        result[1]!=null?result[1].toString():"",
                        result[2]!=null?result[2].toString():null,
                        result[3]!=null?(Date) result[3]:null,
                        result[4]!=null?(Date) result[4]:null,
                        result[5]!=null?(Integer) result[5]:null
                      ))
                .collect(Collectors.toList());
        driverVO.setDocumentList(documentList);

        List<VehicleTable> siteList = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            String siteField = "xfcy" + i;
            String site = getFieldValue(driver, siteField)!=null?
                    (String) getFieldValue(driver, siteField) :"";

            Optional<DropdownData> siteDescData = commonRepository.getSiteList().stream().filter(x->x.getValue().equals(site)).findFirst();
            String siteDesc = "";
            if(siteDescData.isPresent()){
                siteDesc = siteDescData.get().getLabel();
            }

            if (site != null  && !site.trim().isEmpty()) {
                siteList.add(new VehicleTable(site, siteDesc));
            }
        }

        driverVO.setSiteList(siteList);


        List<VehicleTable> vehicleClassList = new ArrayList<>();
        if(driver.getXallvehicle()!=2){
            for (int i = 0; i <= 9; i++) {
                String vehicleClassField = "xvehicleclas" + i;
                String vehicleClass = getFieldValue(driver, vehicleClassField)!=null?
                                        (String) getFieldValue(driver, vehicleClassField) :"";

                Optional<DropdownData> vehClassDescData = commonRepository.getVehicleClassList().stream().filter(x->x.getValue().equals(vehicleClass)).findFirst();
                String vehClassDesc = "";
                if(vehClassDescData.isPresent()){
                    vehClassDesc = vehClassDescData.get().getLabel();
                }

                if (vehicleClass != null  && !vehicleClass.trim().isEmpty()) {
                    vehicleClassList.add(new VehicleTable(vehicleClass, vehClassDesc));
                }
            }
        }
        driverVO.setVehicleClassList(vehicleClassList);

        List<UnavailableDays> unavailableDaysList = new ArrayList<>();
        String startDateField = "xuvystrdat";
        String endDateField = "xuvyenddat";
        Date startDate = getFieldValue(driver, startDateField)!=null?
                (Date) getFieldValue(driver, startDateField) : null;
        Date endDate = getFieldValue(driver, endDateField)!=null?
                (Date) getFieldValue(driver, endDateField) : null;

        UnavailableDays unavailableDays = new UnavailableDays(startDate,endDate,"");
        unavailableDaysList.add(unavailableDays);
        driverVO.setUnavailableDaysList(unavailableDaysList);

        return driverVO;
    }

    public Map<String, Object> getCommonData() {
        Map<String, Object> commonData = new HashMap<>();

        List<DropdownData> siteList = commonRepository.getSiteList();
        List<DropdownData> carrierList = commonRepository.getCarrierList();
        List<DropdownData> businessLineList = commonRepository.getBusinessLineList();
        List<DropdownData> languageList = commonRepository.getPrimaryLanguageList();
        List<StyleData> styleList = commonRepository.getStyleList();
        List<DropdownData> unAvailableList = commonRepository.getUnAvailableList();
        List<DropdownData> countryList = commonRepository.getCountryList();
        List<DropdownData> inspectionList = commonRepository.getInspectionList();
        List<DropdownData> fixedAssetList = commonRepository.getFixedAssetList();

        commonData.put("siteList", siteList);
        commonData.put("carrierList", carrierList);
        commonData.put("businessLineList", businessLineList);
        commonData.put("languageList", languageList);
        commonData.put("styleList", styleList);
        commonData.put("unAvailableList", unAvailableList);
        commonData.put("countryList", countryList);
        commonData.put("inspectionList", inspectionList);
        commonData.put("fixedAssetList", fixedAssetList);

        return commonData;
    }

    public Map<String, Object> getVehicleCommonData() {
        Map<String, Object> commonData = new HashMap<>();

        List<DropdownData> siteList = commonRepository.getSiteList();
        List<DropdownData> vehicleClassList = commonRepository.getVehicleClassList();
        List<DropdownData> ownerShipList = commonRepository.getLocalMenuList(1554);
        List<DropdownData> brandList = commonRepository.getLocalMenuList(1556);
        List<DropdownData> carrierList = commonRepository.getCarrierList();
        List<DropdownData> colorList = commonRepository.getLocalMenuList(1557);
        List<DropdownData> fuelTypeList = commonRepository.getLocalMenuList(1558);
        List<DropdownData> performanceList = commonRepository.getLocalMenuList(1528);
        List<DropdownData> vehicleFuelUnitList = commonRepository.getVehicleFuelUnitList();
        List<StyleData> styleList = commonRepository.getStyleList();
        List<DropdownData> unAvailableList = commonRepository.getUnAvailableList();
        List<DropdownData> driverList = commonRepository.getDriverList();
        List<DropdownData> customerList = commonRepository.getCustomerList();

        List<DropdownData> categoryList = commonRepository.getCategoryList();
        List<DropdownData> routeCodesList = commonRepository.getLocalMenuList(409);

        commonData.put("siteList", siteList);
        commonData.put("ownerShipList", ownerShipList);
        commonData.put("brandList", brandList);
        commonData.put("carrierList", carrierList);
        commonData.put("colorList", colorList);
        commonData.put("fuelTypeList", fuelTypeList);
        commonData.put("performanceList", performanceList);
        commonData.put("styleList", styleList);
        commonData.put("unAvailableList", unAvailableList);
        commonData.put("vehicleFuelUnitList", vehicleFuelUnitList);
        commonData.put("driverList", driverList);
        commonData.put("customerList", customerList);
        commonData.put("categoryList", categoryList);
        commonData.put("vehicleClassList", vehicleClassList);
        commonData.put("routeCodesList", routeCodesList);

        return commonData;
    }

    public Map<String, Object> getPostalData(String country) {
        Map<String, Object> postalData = new HashMap<>();
        List<PostalCodeDetails> list = commonRepository.getPostalDetailsList(country);
        postalData.put("postalData", list);
        return postalData;
    }

    public Map<String, Object> getVehicleClassCommonData() {
        Map<String, Object> commonData = new HashMap<>();

        List<DropdownData> countryList = commonRepository.getCountryList();
        List<DropdownData> typeList = commonRepository.getLocalMenuList(1563);
        List<DropdownData> inspectionList = commonRepository.getInspectionList();

        commonData.put("countryList", countryList);
        commonData.put("typeList", typeList);
        commonData.put("inspectionList", inspectionList);
        return commonData;
    }

    public Map<String, Object> getTrailerCommonData() {
        Map<String, Object> commonData = new HashMap<>();

        List<DropdownData> siteList = commonRepository.getSiteList();
        List<DropdownData> trailerTypeList = commonRepository.getTrailerTypeList();
        List<DropdownData> fixedAssetList = commonRepository.getFixedAssetList();
        List<StyleData> styleList = commonRepository.getStyleList();
        List<DropdownData> unAvailableList = commonRepository.getUnAvailableList();

        commonData.put("siteList", siteList);
        commonData.put("trailerTypeList", trailerTypeList);
        commonData.put("fixedAssetList", fixedAssetList);
        commonData.put("styleList", styleList);
        commonData.put("unAvailableList", unAvailableList);

        return commonData;
    }

    public Map<String, Object> getDriverCommonData() {
        Map<String, Object> commonData = new HashMap<>();

        List<DropdownData> siteList = commonRepository.getSiteList();
        List<DropdownData> carrierList = commonRepository.getCarrierList();
        List<DropdownData> businessLineList = commonRepository.getBusinessLineList();
        List<DropdownData> languageList = commonRepository.getPrimaryLanguageList();
        List<StyleData> styleList = commonRepository.getStyleList();
        List<DropdownData> unAvailableList = commonRepository.getUnAvailableList();
        List<DropdownData> countryList = commonRepository.getCountryList();
        List<DropdownData> inspectionList = commonRepository.getInspectionList();
        List<DropdownData> fixedAssetList = commonRepository.getFixedAssetList();
        List<DropdownData> performanceList = commonRepository.getLocalMenuList(1528);
        List<DropdownData> licenseTypeList = commonRepository.getLocalMenuList(1561);
        List<DropdownData> vehicleClassList = commonRepository.getVehicleClassList();
        List<DropdownData> documentTypeList = commonRepository.getDocumentTypeList();
        List<DropdownData> issueAuthList = commonRepository.getissueAuthList();

        commonData.put("siteList", siteList);
        commonData.put("carrierList", carrierList);
        commonData.put("businessLineList", businessLineList);
        commonData.put("languageList", languageList);
        commonData.put("styleList", styleList);
        commonData.put("unAvailableList", unAvailableList);
        commonData.put("countryList", countryList);
        commonData.put("inspectionList", inspectionList);
        commonData.put("fixedAssetList", fixedAssetList);
        commonData.put("performanceList", performanceList);
        commonData.put("licenseTypeList", licenseTypeList);
        commonData.put("vehicleClassList", vehicleClassList);
        commonData.put("documentTypeList", documentTypeList);
        commonData.put("issueAuthList", issueAuthList);

        return commonData;
    }

    public boolean checkAllocationExists(String transactionNumber) {
        return allocationRepository.existsByTransactionNumber(transactionNumber);
    }

    public Allocation createAllocation(Allocation allocation) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        String maxValueQuery = "select MAX(CAST(RIGHT(XTRANNO_0, 3) AS INT)) AS MaxValue from "+dbSchema+".XX10CALLOC";

        Integer maxValue = (Integer) entityManager.createNativeQuery(maxValueQuery).getSingleResult();
        allocation.setTransactionNumber("VA-"+allocation.getDriverId()+"-"+sdf.format(new Date())+"-"+String.format("%03d", maxValue+1));
        return allocationRepository.save(allocation);
    }

    public List<Allocation> getAllAllocationsList() {
        return allocationRepository.findAll();
    }

    public Allocation getAllocationByTransactionNumber(String transactionNumber) {
        return allocationRepository.findByTransactionNumber(transactionNumber);
    }

    public Allocation updateAllocation(Allocation allocation) {
       return allocationRepository.save(allocation);
    }

    public String deleteAllocationByTransactionNumber(String transactionNumber) {
        allocationRepository.deleteByTransactionNumber(transactionNumber);
        return "success";
    }

    public Map<String, Object> getAllocationCommonData() {
        Map<String, Object> commonData = new HashMap<>();

        List<Map<String, Object>> vehicleData = commonRepository.getVehicleData();
        List<Map<String, Object>> driverData = commonRepository.getDriverData();
        List<DropdownData> statusList = commonRepository.getLocalMenuList(1510);
        List<DropdownData> licenseTypeList = commonRepository.getLocalMenuList(1561);

        commonData.put("vehicleData", vehicleData);
        commonData.put("driverData", driverData);
        commonData.put("statusList", statusList);
        commonData.put("licenseTypeList", licenseTypeList);

        return commonData;
    }

    public List<ImageVO> getAllImages() {
        /*String imageQuery = "SELECT IDENT1_0, BLOB_0 FROM "+dbSchema+".CBLOB\n" +
                "WHERE CODBLB_0 = 'ITM'";
        Query query = entityManager.createNativeQuery(imageQuery);
        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(result -> new ImageVO( (String)result[0],
                        (byte[]) result[1]))
                .collect(Collectors.toList());*/

        List<String> xfcyValues = new ArrayList<>(Arrays.asList("CON001", "CON002", "CON003"));

        StringBuilder queryBuilder = new StringBuilder("select IDENT1_0, BLOB_0 from "+dbSchema+".CBLOB where IDENT1_0 in (");

        for (int i = 0; i < xfcyValues.size(); i++) {
            if (i > 0) queryBuilder.append(", ");
            queryBuilder.append(":identi" + i);
        }
        queryBuilder.append(")");

        Query imageQuery = entityManager.createNativeQuery(queryBuilder.toString());

        for (int i = 0; i < xfcyValues.size(); i++) {
            imageQuery.setParameter("identi" + i, xfcyValues.get(i));
        }

        List<Object[]> results = imageQuery.getResultList();
        return results.stream()
                .map(result -> new ImageVO( (String)result[0],
                        (byte[]) result[1]))
                .collect(Collectors.toList());
    }

    public boolean checkTrailerTypeExists(String trailerCode) {
        return trailerTypeRepository.existsByTrailerCode(trailerCode);
    }

    public TrailerType createTrailerType(TrailerType trailerType) throws NoSuchFieldException, IllegalAccessException {
        trailerType.setCredattim(new Date());
        trailerType.setUpddattim(new Date());
        if(trailerType.getXall()!=2) {
            int count = 0;
            for (DropdownData product : trailerType.getProductCategoryList()) {
                try {
                    String prodCategory = "xtsicod" + count++;
                    Field prodCategoryField = trailerType.getClass().getDeclaredField(prodCategory);
                    prodCategoryField.setAccessible(true);
                    prodCategoryField.set(trailerType, product.getValue());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
        }
        return trailerTypeRepository.save(trailerType);
    }

    public List<TrailerType> getAllTrailerTypes() throws IllegalAccessException {
        List<TrailerType> trailerTypeList =  trailerTypeRepository.findAll();
        for(TrailerType trailerType: trailerTypeList){
            trailerType.setTrailerClassList(getTrailerClassList(trailerType.getTrailerCode()));
            trailerType.setProductCategoryList(getProductCategoryList(trailerType));
        }

        return trailerTypeList;
    }

    public TrailerType getTrailerTypeByTrailerCode(String trailerCode) throws IllegalAccessException {
        TrailerType trailerType =  trailerTypeRepository.findByTrailerCode(trailerCode);
        trailerType.setTrailerClassList(getTrailerClassList(trailerType.getTrailerCode()));
        trailerType.setProductCategoryList(getProductCategoryList(trailerType));
        return trailerType;
    }

    private List<DropdownData> getProductCategoryList(TrailerType trailerType) throws IllegalAccessException {
        List<DropdownData> productCategoryList = new ArrayList<>();
        if(trailerType.getXall()!=2){
            for (int i = 0; i <= 9; i++) {
                String productCategory = "xtsicod" + i;
                String productCategoryValue= getFieldValue(trailerType, productCategory)!=null?
                        (String) getFieldValue(trailerType, productCategory) :"";

                Optional<DropdownData> prodCategoryData = commonRepository.getCategoryList().stream().filter(x->x.getValue().equals(productCategoryValue)).findFirst();
                String productCategoryDesc = "";
                if(prodCategoryData.isPresent()){
                    productCategoryDesc = prodCategoryData.get().getLabel();
                }

                if (productCategoryValue != null  && !productCategoryValue.trim().isEmpty()) {
                    productCategoryList.add(new DropdownData(productCategoryValue, productCategoryDesc));
                }
            }
        }
        return productCategoryList;
    }

    private List<TrailerClass> getTrailerClassList(String code) {
        String vehClassQuery = "select a.XCLASS_0, c.XMAXCAPW_0, c.XMAXUNIT_0, x1.TEXTE_0 wuom, c.XMAXCAPV_0, c.XMAXVUNIT_0, x2.TEXTE_0 vwom, c.AXLNBR_0 \n" +
                "from TMSNEW.XX10CXASSOC1 a \n" +
                "join TMSNEW.XX10CCLASS c on c.CLASS_0= a.XCLASS_0\n" +
                "join TMSNEW.ATEXTRA x1 on x1.IDENT1_0=c.XMAXUNIT_0\n" +
                "join TMSNEW.ATEXTRA x2 on x2.IDENT1_0=c.XMAXVUNIT_0\n" +
                "where a.XTRAILER_0='"+code+"'\n" +
                "and x1.LANGUE_0='ENG' and x1.ZONE_0='DES' and x1.CODFIC_0='TABUNIT'\n" +
                "and x2.LANGUE_0='ENG' and x2.ZONE_0='DES' and x2.CODFIC_0='TABUNIT'";

        Query query = entityManager.createNativeQuery(vehClassQuery);
        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(result -> new TrailerClass( result[0]!=null?result[0].toString():"",
                        result[1]!=null?(BigDecimal) result[1]:null,
                        result[2]!=null?result[2].toString():"",
                        result[3]!=null?result[3].toString():"",
                        result[4]!=null?(BigDecimal) result[4]:null,
                        result[5]!=null?result[5].toString():"",
                        result[6]!=null?result[6].toString():"",
                        result[7]!=null?(short)result[7]:0))
                .collect(Collectors.toList());
    }

    public TrailerType updateTrailerType(TrailerType trailerType) throws NoSuchFieldException, IllegalAccessException {
        trailerType.setUpddattim(new Date());
        if(trailerType.getXall()!=2) {
            int count = 0;
            for (DropdownData product : trailerType.getProductCategoryList()) {
                try {
                    String prodCategory = "xtsicod" + count++;
                    Field prodCategoryField = trailerType.getClass().getDeclaredField(prodCategory);
                    prodCategoryField.setAccessible(true);
                    prodCategoryField.set(trailerType, product.getValue());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw e;
                }
            }
            if(count<10) {
                for(int i=count;i<10;i++) {
                    try {
                        String prodCategory = "xtsicod" + i;
                        Field prodCategoryField = trailerType.getClass().getDeclaredField(prodCategory);
                        prodCategoryField.setAccessible(true);
                        prodCategoryField.set(trailerType, "");
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw e;
                    }
                }
            }
        }
        return trailerTypeRepository.save(trailerType);
    }

    public String deleteTrailerTypeByTrailerCode(String trailerCode) {
        trailerTypeRepository.deleteByTrailerCode(trailerCode);
        return "success";
    }

    public Map<String, Object> getTrailerTypeCommonData() {
        Map<String, Object> commonData = new HashMap<>();

        List<DropdownData> typeList = commonRepository.getLocalMenuList(1564);
        List<DropdownData> categoryList = commonRepository.getCategoryList();

        commonData.put("typeList", typeList);
        commonData.put("categoryList", categoryList);

        return commonData;
    }
}
