package com.transport.fleet.service;

import com.transport.fleet.model.*;
import com.transport.fleet.repository.*;
import com.transport.fleet.response.*;
import com.transport.tracking.response.VehicleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
        vehicle.setXcurmtre(vehicleVO.getCurrentOdometerReading());
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

        int routeRenewalCount=0;
        for(RouteRenewal routeRenewal: vehicleVO.getRouteRenewalsList()){
            try {
                String depotName = "depotName" + routeRenewalCount;
                String serviceTime = "serviceTime" + routeRenewalCount++;
                Field depotNameField = vehicle.getClass().getDeclaredField(depotName);
                Field serviceTimeField = vehicle.getClass().getDeclaredField(serviceTime);
                depotNameField.setAccessible(true);
                depotNameField.set(vehicle, routeRenewal.getSite());
                serviceTimeField.setAccessible(true);
                serviceTimeField.set(vehicle, routeRenewal.getServiceTime());
            }catch (NoSuchFieldException | IllegalAccessException e) {
                throw e;
            }
        }

        vehicle.setAllDriver(vehicleVO.getAllDriverFlag());

        if(vehicleVO.getAllDriverFlag()!=2) {
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

        if(vehicleVO.getAllCustomerFlag()!=2) {
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

        if(vehicleVO.getAllCategoryFlag()!=2) {
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

        int inspCount = 0;
        for (TechnicalInspection inspection : vehicleVO.getTechnicalInspectionList()) {
            try {
                String insType = "xinsptyp" + inspCount;
                String lastCheck = "xlstchk" + inspCount;
                String periodicity = "xperiodicity"+inspCount;
                String nextVisit = "xnextvisit"+inspCount;
                String type = "xtypein"+inspCount;
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

        return vehicle;
    }

    public List<FleetVehicleVO> getAllVehicles() throws IllegalAccessException {
        List<FleetVehicleVO> vehicleVOList = new ArrayList<>();
        List<FleetVehicle> vehicles = vehicleRepository.findAll();
        for(FleetVehicle vehicle: vehicles){
            vehicleVOList.add(getVehicleResponse(vehicle));
        }
        return vehicleVOList;
    }

    public FleetVehicleVO getVehicleByCodeyve(String codeyve) throws IllegalAccessException {
        FleetVehicle vehicle = vehicleRepository.findByCodeyve(codeyve);
        return getVehicleResponse(vehicle);
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
        vehicleVO.setCurrentOdometerReading(vehicle.getXcurmtre());
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

        if(isCreate) {
            int compartmentCount = 1;
            for (TrailerCompartment compartment : trailerVO.getCompartmentList()) {
                String queryString = "insert into TMSNEW.XX10CTRAILD " +
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

            technicalInspectionList.add(new TechnicalInspection(insType,lastCheck, periodicity,nextVisit,type));
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

        String queryString = "select XTRAILER_0, XDES_0, XMAXLOAMS_0, XMASSUOM_0, " +
                "XMAXLOVOL_0, XVOLUOM_0 from "+dbSchema+".XX10CXASSOC1 where XCLASS_0 = '"+vehClass.getClassName()+"'\n";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> results = query.getResultList();
        List<VehicleAssociation> associationList = results.stream()
                .map(result -> new VehicleAssociation( "Trailer",
                        result[0]!=null?result[0].toString():"",
                        result[1]!=null?result[1].toString():"",
                        result[2]!=null?result[2].toString():null,
                        result[3]!=null?result[3].toString():"",
                        "",
                        result[4]!=null?result[4].toString():null,
                        result[5]!=null?result[5].toString():"",
                        ""))
                .collect(Collectors.toList());

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
        return "success";
    }

    public boolean checkVehicleExists(String codeyve) {
        return vehicleRepository.existsByCodeyve(codeyve);
    }

    public boolean checkDriverExists(String driverId) {
        return driverRepository.existsByDriverId(driverId);
    }

    public FleetDriver createDriver(FleetDriverVO driverVO) throws NoSuchFieldException, IllegalAccessException {
         return driverRepository.save(getDriverModelForCreateOrUpdate(driverVO, true));
    }

    private FleetDriver getDriverModelForCreateOrUpdate(FleetDriverVO driverVO, boolean isCreate) throws NoSuchFieldException, IllegalAccessException {
        FleetDriver driver = new FleetDriver();

        driver.setRowId(isCreate?null:driverVO.getRowId());
        driver.setDriverId(driverVO.getDriverId());
        driver.setDriver(driverVO.getDriver());
        driver.setFcy(driverVO.getFcy());
        driver.setActive(driverVO.getActive());
        driver.setXsalerep(driverVO.getXsalerep());
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

        if(isCreate){
            int lineNum=1;
            for(Document document: driverVO.getDocumentList()){
                String query = "insert into TMSNEW.XX10CDRIVERD " +
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
        }else if(driverVO.getDocumentList().isEmpty()){
            String deleteQuery = "DELETE from "+dbSchema+".XX10CDRIVERD where DRIVERID_0= '"+driverVO.getDriverId()+"'";
            entityManager.createNativeQuery(deleteQuery).executeUpdate();
        } else{
            String docQuery = "SELECT GTPASS_0,  XDOCNO_0, ISSAUTHORITY_0, PASSDURATION_0, EXPDATE_0 , LINNUM_0\n" +
                    "from TMSNEW.XX10CDRIVERD where DRIVERID_0='"+driverVO.getDriverId()+"' order by LINNUM_0";

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
                            String insertQuery = "insert into TMSNEW.XX10CDRIVERD " +
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
                            String updateQuery = "UPDATE TMSNEW.XX10CDRIVERD " +
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
                    String insertQuery = "insert into TMSNEW.XX10CDRIVERD " +
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

        int siteCount=0;
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

        if(driverVO.getXallvehicle()!=2) {
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

        if(!driverVO.getUnavailableDaysList().isEmpty()){
            driver.setXuvystrdat(driverVO.getUnavailableDaysList().get(0).getStartDate());
            driver.setXuvyenddat(driverVO.getUnavailableDaysList().get(0).getEndDate());
            driver.setXuvycod(driverVO.getUnavailableDaysList().get(0).getDesc());
        }
        
        return driver;
    }

    public List<FleetDriverVO> getAllDriversList() throws IllegalAccessException {
        List<FleetDriver> driverList = driverRepository.findAll();
        List<FleetDriverVO> fleetDriverVOList = new ArrayList<>();
        for(FleetDriver driver: driverList){
            fleetDriverVOList.add(getDriverResponse(driver));
        }
        return fleetDriverVOList;
    }

    public FleetDriverVO getDriverById(String driverId) throws IllegalAccessException {
        FleetDriver driver = driverRepository.findByDriverId(driverId);
        return getDriverResponse(driver);
    }

    public void updateDriver(FleetDriverVO driverVO) throws NoSuchFieldException, IllegalAccessException {
        driverRepository.save(getDriverModelForCreateOrUpdate(driverVO, false));
    }

    public String deleteDriverById(String driverId) {
        driverRepository.deleteByDriverId(driverId);
        return "success";
    }

    public FleetDriverVO getDriverResponse(FleetDriver driver) throws IllegalAccessException {
        FleetDriverVO driverVO = new FleetDriverVO();

        driverVO.setRowId(driver.getRowId());
        driverVO.setDriverId(driver.getDriverId());
        driverVO.setDriver(driver.getDriver());
        driverVO.setFcy(driver.getFcy());
        driverVO.setActive(driver.getActive());
        driverVO.setXsalerep(driver.getXsalerep());
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
        List<DropdownData> ownerShipList = commonRepository.getOwnerShipList();
        List<DropdownData> brandList = commonRepository.getBrandList();
        List<DropdownData> carrierList = commonRepository.getCarrierList();
        List<DropdownData> colorList = commonRepository.getColorList();
        List<DropdownData> fuelTypeList = commonRepository.getFuelTypeList();
        List<DropdownData> performanceList = commonRepository.getPerformaceList();
        List<DropdownData> vehicleFuelUnitList = commonRepository.getVehicleFuelUnitList();
        List<StyleData> styleList = commonRepository.getStyleList();
        List<DropdownData> unAvailableList = commonRepository.getUnAvailableList();
        List<DropdownData> driverList = commonRepository.getDriverList();
        List<DropdownData> customerList = commonRepository.getCustomerList();
        List<DropdownData> categoryList = commonRepository.getCategoryList();

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
        List<DropdownData> typeList = commonRepository.getTypeList();
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
        List<DropdownData> performanceList = commonRepository.getPerformaceList();
        List<DropdownData> licenseTypeList = commonRepository.getLicenseTypeList();
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

        return commonData;
    }
}
