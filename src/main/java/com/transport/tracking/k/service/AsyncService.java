package com.transport.tracking.k.service;

import com.transport.tracking.k.constants.TransportConstants;
import com.transport.tracking.model.*;
import lombok.extern.slf4j.Slf4j;
import com.transport.tracking.repository.*;
import com.transport.tracking.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

@Slf4j
@Component
public class AsyncService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private VehTrailRepository vehTrailRepository;

    @Autowired
    private VehDriverRepository vehDriverRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverProRepository driverProRepository;

    @Autowired
    private PickupRepository pickupRepository;

    @Autowired
    private DropsRepository dropsRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${time.hours.add}")
    private int hours = 9;

    @Value("${db.schema}")
    private String dbSchema;
    //private String dbSchema = "tbs.TMSBURBAN";

    private String DriverWeek = "select WorkWeekCycle, RemainingWorkMinutes  from {0}.vw_WeeklyDriverReport WHERE DRIVERID = ''{1}'' AND DATEPART(YEAR, REPORTYEAR) = DATEPART(YEAR, ''{2}'') AND DATEPART(WEEK, REPORTWEEK) = DATEPART(WEEK, ''{2}'') ";
    private String DriverMonthly = "select WorkWeekCycle, RemainingWorkMinutes from {0}.vw_MonthlyDriverReport WHERE DRIVERID = ''{1}'' AND DATEPART(YEAR, REPORTYEAR) = DATEPART(YEAR, ''{2}'') AND DATEPART(WEEK, REPORTMONTH) = DATEPART(MONTH, ''{2}'') ";
    private String DriverDate = "select * from {0}.vw_WeeklyDriverReport WHERE  d.DOCNUM = x.DOCNUM where {1}";

    private String DORPS_QUERY = "select d.*, x.PRODUCTCODE, x.PRODUCTNAME,x.PRODUCTCATEG, x.QUANTITY,x.WEIGHT, x.VOLUME, x.WEI_UNIT, x.VOL_UNIT, x.UOM, x.DOCLINENO, x.CONV_QTY, x.PURUNIT \n" +
            " from {0}.XTMSDROP d left join {0}.XTMSDROPD x on d.DOCNUM = x.DOCNUM where {1}";

    private String PICKUP_QUERY = "select d.*, x.PRODUCTCODE, x.PRODUCTNAME,x.PRODUCTCATEG, x.QUANTITY,x.WEIGHT, x.VOLUME, x.WEI_UNIT, x.VOL_UNIT, x.UOM, x.DOCLINENO, x.CONV_QTY, x.PURUNIT \n" +
            " from {0}.XTMSPICKUP d left join {0}.XTMSPICKUPD x on d.DOCNUM = x.DOCNUM where {1}";

    private static String ONLY_DATE = "d.DOCDATE = ''{0}''";

    private static String SITE_DATE = "d.SITE IN {0} AND d.DOCDATE = ''{1}''";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private static Map<String, List<TimeVO>> timeMap = new HashMap<>();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    private static String SITE_DATERANGE = "d.SITE IN {0} AND d.DOCDATE BETWEEN  ''{1}'' AND ''{2}''";


    private static String ONLY_DATERANGE = "d.DOCDATE BETWEEN ''{0}'' AND ''{1}''";

    @Async
    public CompletableFuture<List<VehicleVO>> getVehicles(List<String> site, List<String> vehicleSList,Date selDate) {
        List<Vehicle> vehicleList = null;
        String dddate = format.format(selDate);
        List<VehicleVO> vehicleVOList = new ArrayList<>();
        if(!StringUtils.isEmpty(site)) {
            vehicleList = vehicleRepository.findBySitesAndDate(site,dddate);
        }else {
            vehicleList = new ArrayList<>();
            Iterator<Vehicle> iterator = vehicleRepository.findAll().iterator();
            while(iterator.hasNext()) {
            }
        }
        if(!CollectionUtils.isEmpty(vehicleList)) {
            vehicleVOList = vehicleList.parallelStream().map(a-> this.convert(a, vehicleSList,dddate))
                    .collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(vehicleVOList);
    }

    private VehicleVO convert(Vehicle vehicle, List<String> vehicleSList,String dddate) {
        VehicleVO vehicleVO = new VehicleVO();
        vehicleVO.setCodeyve(vehicle.getCodeyve());
		vehicleVO.setIsStockExist(vehicle.getIsStockExist());
        String vehsite = vehicle.getFcy();
        String Veh = vehicle.getCodeyve();
        log.info("inside update validate");
        log.info(Veh);
        log.info(dddate);
        vehicleVO.setName(vehicle.getName());

        if(!StringUtils.isEmpty(vehicle.getDrivername())) {
            vehicleVO.setDrivername(vehicle.getDrivername());
        }
        vehicleVO.setDriverid(vehicle.getDriverid());

 if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getTrailer())) {
            vehicleVO.setTrailer(vehicle.getTrailer());
        }

       // vehicleVO.setDriverid(vehicle.getDriverid());
       // if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getTrailer())) {
      /*  VehTrail vehTrail =  vehTrailRepository.findTrailerbyVehicleSiteandDate(Veh,vehsite,dddate);

            if(Objects.nonNull(vehTrail)) {
                vehicleVO.setTrailer(vehTrail.getTrailer());
                log.info(vehTrail.getTrailer());
            }
           // vehicleVO.setTrailer(vehicle.getTrailer());
		   */

        List<String> equipmentList = new ArrayList<>();
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment1())) {
            equipmentList.add(vehicle.getEquipment1());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment2())) {
            equipmentList.add(vehicle.getEquipment2());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment3())) {
            equipmentList.add(vehicle.getEquipment3());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment4())) {
            equipmentList.add(vehicle.getEquipment4());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment5())) {
            equipmentList.add(vehicle.getEquipment5());
        }
        vehicleVO.setEquipmentList(equipmentList);
        vehicleVO.setTrailerList(!StringUtils.isEmpty(vehicle.getTrialerList()) ? Arrays.asList(vehicle.getTrialerList().split(",")) : new ArrayList<>());
        vehicleVO.setTrailerLink(vehicle.getTrailerLink());
        vehicleVO.setAlldrivers(vehicle.getAlldrivers());
        vehicleVO.setDriverslist(vehicle.getDriverslist());
        vehicleVO.setAllcustomers(vehicle.getAllcustomers());
        vehicleVO.setCustomerlist(vehicle.getCustomerlist());
        vehicleVO.setCapacities(vehicle.getCapacities());
        vehicleVO.setVol(vehicle.getVol());
        vehicleVO.setMaxordercnt(vehicle.getMaxordercnt());

        //vehicleVO.setType("open");
        String startTime = vehicle.getStarttime();

        if(StringUtils.isEmpty(startTime)) {
            startTime = "0800";
        }
        vehicleVO.setTimelineInterval(this.getTimeList(startTime));
        vehicleVO.setLateral(vehicle.getLateral());
        vehicleVO.setClassName(vehicle.getClassName());
        vehicleVO.setLoadBay(vehicle.getLoadBay());
        vehicleVO.setTailGate(vehicle.getTailGate());
        vehicleVO.setTclcod(vehicle.getTclcod());
        vehicleVO.setSpeciality(this.constructSpeciality(vehicle.getLoadBay(),vehicle.getOvertimestar(), vehicle.getTailGate(), vehicle.getTclcod(),vehicle.getStartdepots(),
                vehicle.getEnddepotserv(),vehicle.getFixedcost(),vehicle.getCostperunitt(),vehicle.getCostperunitd(),vehicle.getCostperunito(),vehicle.getMaxspeed(),stackHeightConv(vehicle.getLength()),
                stackHeightConv(vehicle.getHeigth()),stackHeightConv(vehicle.getWidth())));
        vehicleVO.setStarttime(vehicle.getStarttime());
        vehicleVO.setLateststarttime(vehicle.getLateststarttime());
        vehicleVO.setOvertimestar(vehicle.getOvertimestar());
        vehicleVO.setSkills(vehicle.getSkills());
        vehicleVO.setStartdepotn(vehicle.getStartdepotn());
        vehicleVO.setEnddepotname(vehicle.getEnddepotname());
        vehicleVO.setStartdepots(vehicle.getStartdepots());
        vehicleVO.setEnddepotserv(vehicle.getEnddepotserv());
        vehicleVO.setBptnum(vehicle.getBptnum());
        vehicleVO.setXvol(vehicle.getXvol());
        vehicleVO.setXweu(vehicle.getXweu());
        vehicleVO.setAprodCategDesc(vehicle.getAprodCategDesc());
        vehicleVO.setAroutecodeDesc(vehicle.getAroutecodeDesc());
        vehicleVO.setAvehClassListDesc(vehicle.getAvehClassListDesc());
        vehicleVO.setSkills(vehicle.getSkills());
        vehicleVO.setMaxspeed(vehicle.getMaxspeed());
        vehicleVO.setXmaxtotaldis(vehicle.getXmaxtotaldis());
        vehicleVO.setMaxtotaldist(vehicle.getMaxtotaldist());
        vehicleVO.setMaxtotaltime(vehicle.getMaxtotaltime());
        vehicleVO.setMaxtotaltrvtime(vehicle.getMaxtotaltrvtime());
        vehicleVO.setCostperunitt(vehicle.getCostperunitt());
        vehicleVO.setCostperunitd(vehicle.getCostperunitd());
        vehicleVO.setCostperunito(vehicle.getCostperunito());
        vehicleVO.setFcy(vehicle.getFcy());
        vehicleVO.setColor(vehicle.getColor());
        vehicleVO.setCatego(vehicle.getCatego());
        vehicleVO.setModel(vehicle.getModel());
        vehicleVO.setFixedcost(vehicle.getFixedcost());
        vehicleVO.setLength(stackHeightConv(vehicle.getLength()));
        vehicleVO.setHeigth(stackHeightConv(vehicle.getHeigth()));
        vehicleVO.setWidth(stackHeightConv(vehicle.getWidth()));
        return vehicleVO;
    }

    private String constructSpeciality(String loadBay, double overtimestarts,String tailGate, String tcloud,double loadingtime,double offloadingtime,double fixedcost,
                                       double timecost,double distancecost,double ovetimecost,double maxspeed,double len,double hei,double wei) {
        StringBuilder sb = new StringBuilder();
        sb.append("PRD CATEG : "+ tcloud);
        sb.append("\n");
        sb.append("TAILGATE : "+ tailGate);
        sb.append("\n");
        sb.append("LOADBAY : "+ loadBay);
        sb.append("\n");
        sb.append("Dimensions : "+ len+"Cms ,"+ wei+"Cms ,"+ hei+"Cms");
        sb.append("\n");
        sb.append("OverTime Starts : "+ roundoff2dec(overtimestarts)+"Hrs");
        sb.append("\n");
        sb.append("Loading Time : "+ convertDectoString(loadingtime)+"Mins");
        sb.append("\n");
        sb.append("Offloading Time : "+ convertDectoString(offloadingtime)+"Mins");
        sb.append("\n");
        sb.append("Max Speed : "+ roundoff2dec(maxspeed)+"Kms/Hr");
        sb.append("\n");
        sb.append("Fixed cost : "+ roundoff2dec(fixedcost)+"€");
        sb.append("\n");
        sb.append("CostPerUnitTime : "+ roundoff2dec(timecost)+"€");
        sb.append("\n");
        sb.append("CostPerUnitDistance : "+ roundoff2dec(distancecost)+"€");
        sb.append("\n");
        sb.append("CostPerUnitOverTime : "+ roundoff2dec(ovetimecost)+"€");
        sb.append("\n");
        return sb.toString();
    }

    private double roundoff2dec(double x){
          double bd = x;
        bd = Math.round(bd * 100.0) / 100.0;

        return bd;
    }


    private int convertDectoString(double x){
        double y = 60;
        double res = y * x;
        int r = (int) res;
        return r;
    }

    private String constructDropSpeciality(String loadbay, String tailgate,int stime, int wtime, double sheight, String vehtype,String timngs, String packing, String height, String loadingord) {
        StringBuilder sb = new StringBuilder();
        if(timngs == null) {
            timngs= "";
        }
        if(vehtype == null){
            vehtype = "";
        }


        sb.append("LoadBay : "+ loadbay);
        sb.append("\n");
        sb.append("TailGate : "+ tailgate);
        sb.append("\n");
        sb.append("BP ServiceTime : "+ stime+" Mins");
        sb.append("\n");
        sb.append("WaitingTime : "+ wtime+" Mins");
        sb.append("\n");
        sb.append("StackHeight : "+ sheight+ " Cms");
        sb.append("\n");
        sb.append("Vehicle Type : "+ vehtype);
        sb.append("\n");
        sb.append("Timings : "+ timngs);
        sb.append("\n");
        sb.append("Packing : "+ packing);
        sb.append("\n");
        sb.append("Height : "+ height);
        sb.append("\n");
        sb.append("LoadingOrder : "+ loadingord);
        sb.append("\n");
        return sb.toString();
    }

    public List<TimeVO> getTimeList(String timeStr) {
        List<TimeVO> timeList = new ArrayList<>();
        try {
            String actualTime = StringUtils.replace(timeStr, "0", "");
            int time = Integer.parseInt(actualTime);
            if(time > 23) {

            }else {
                for(int i = 0; i < hours; i ++) {
                    TimeVO timeVO = new TimeVO();
                    timeVO.setValue(String.valueOf(i * 12));
                    timeVO.setLabel(String.format("%s:%s", time + i, "00"));
                    timeList.add(timeVO);
                }
            }
        }catch (Exception e) {
            return this.getTimeList("0800");
        }
        return timeList;
    }

    public static void main(String[] arg) {
        AsyncService asyncService = new AsyncService();
        String actualTime = StringUtils.replace("0800", "0", "");
        int time = Integer.parseInt(actualTime);
        List<TimeVO> timeList = new ArrayList<>();
        if(time > 23) {

        }else {
            for(int i = 0; i < 9; i ++) {
                TimeVO timeVO = new TimeVO();
                timeVO.setValue(String.valueOf(i * 10));
                timeVO.setLabel(String.format("%s:%s", time + i, "00"));
                timeList.add(timeVO);
            }
        }
        System.out.println(timeList);
        //System.out.println(asyncService.getTimeList("0700"));
    }

    @Async
    public CompletableFuture<List<EquipmentVO>> getEquipments(List<String> site, List<String> equipmentSList) {
        List<Equipment> equipmentList = null;
        List<EquipmentVO> equipmentVOList = new ArrayList<>();
        if(!StringUtils.isEmpty(site)) {
            equipmentList = equipmentRepository.findByXfcyIn(site);
        }else {
            equipmentList = new ArrayList<>();
            Iterator<Equipment> iterator = equipmentRepository.findAll().iterator();
            while(iterator.hasNext()) {
                equipmentList.add(iterator.next());
            }
        }
        if(!CollectionUtils.isEmpty(equipmentList)) {
            equipmentVOList = equipmentList.parallelStream().map(a-> this.convertEquipment(a, equipmentSList))
                    .collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(equipmentVOList);
    }


    private EquipmentVO convertEquipment(Equipment equipment, List<String> equipmentList) {
        EquipmentVO equipmentVO = new EquipmentVO();
        if(equipmentList.contains(equipment.getXequipid())) {
            equipmentVO.setType(TransportConstants.ALLOCATED);
        }
        equipmentVO.setXequipid(equipment.getXequipid());
        equipmentVO.setXfcy(equipment.getXfcy());
        equipmentVO.setXdescript(equipment.getXdescript());
        equipmentVO.setXequiptyp(equipment.getXequiptyp());
        equipmentVO.setXcodeyve("");
        return equipmentVO;
    }

    @Async
    public CompletableFuture<List<TrailVO>> getTrails(List<String> site, List<String> trailerList, Date selDate) {
        List<Trail> trailList = null;
        String dddate = format.format(selDate);
        List<TrailVO> trailVOList = new ArrayList<>();
        if(!StringUtils.isEmpty(site)) {
            trailList = trailRepository.findBySitesAndDate(site,dddate);
        }else {
            trailList = new ArrayList<>();
            Iterator<Trail> iterator = trailRepository.findAll().iterator();
            while(iterator.hasNext()) {
                trailList.add(iterator.next());
            }
        }
        if(!CollectionUtils.isEmpty(trailList)) {
            trailVOList = trailList.parallelStream().map(a-> this.convert(a, trailerList))
                    .collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(trailVOList);
    }

    private TrailVO convert(Trail trail, List<String> trailersList) {
        TrailVO trailVO = new TrailVO();
        trailVO.setTrailer(trail.getTrailer());
        trailVO.setTyp(trail.getTyp());
        if(trailersList.contains(trail.getTrailer())) {
            trailVO.setType(TransportConstants.ALLOCATED);
        }
        trailVO.setDes(trail.getDes());
        trailVO.setFcy(trail.getFcy());
        trailVO.setColor(trail.getColor());
		trailVO.setIsStockExist(trail.getIsStockExist());
        trailVO.setTclcod(trail.getTclcod());
        trailVO.setAllproducts(trail.getAllproducts());
        trailVO.setModel(trail.getModel());
        trailVO.setMaxloams(trail.getMaxloams());
        trailVO.setMaxlovol(trail.getMaxlovol());
        trailVO.setXmaxloams(trail.getXmaxloams());
        trailVO.setXmaxlovol(trail.getXmaxlovol());
        trailVO.setMaxlen(trail.getMaxlen());
        trailVO.setMaxwid(trail.getMaxwid());
        trailVO.setMaxfh(trail.getMaxfh());
        return trailVO;
    }

    @Async
    public CompletableFuture<List<DriverVO>> getDrivers(List<String> site, List<String> driverSList, Date selDate) {
        List<Driver> driverList = null;
        String dddate = format.format(selDate);
        List<DriverVO> driverVOList = new ArrayList<>();
        if(!StringUtils.isEmpty(site)) {
            driverList = driverRepository.findBySitesAndDate(site,dddate);
        }else {
            driverList = new ArrayList<>();
            Iterator<Driver> iterator = driverRepository.findAll().iterator();
            while(iterator.hasNext()) {
                driverList.add(iterator.next());
            }
        }
        if(!CollectionUtils.isEmpty(driverList)) {
            driverVOList = driverList.parallelStream().map(a-> this.convert(a, driverSList, selDate))
                    .collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(driverVOList);
    }

    private DriverVO convert(Driver driver, List<String> driverList, Date dddate) {
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverid(driver.getDriverid());
//        if(driverList.contains(driver.getDriver())) {
//            driverVO.setType(TransportConstants.OPEN);
//        }
        driverVO.setType(TransportConstants.OPEN);
        driverVO.setDriver(driver.getDriver());
        driverVO.setLicenum(driver.getLicenum());
        driverVO.setLicedat(driver.getLicedat());
        driverVO.setCty(driver.getCty());
        driverVO.setFcy(driver.getFcy());
        driverVO.setColor(driver.getColor());
        driverVO.setPoscod(driver.getPoscod());
        driverVO.setCry(driver.getCry());
        driverVO.setLncstrtime(driver.getLncstrtime());
        driverVO.setLncduration(driver.getLncduration());

        LocalDate tempdate =  convertDateToLocalDate(dddate);
        Map<String, LocalDateTime> weekRange = getWeekRange(tempdate);
        Map<String, LocalDateTime> MonthRange = getMonthRange(tempdate);

        Date wsdate = convertLocalDateTimeToDate(weekRange.get("startDate"));
        Date wedate = convertLocalDateTimeToDate(weekRange.get("endDate"));
        Date msdate =  convertLocalDateTimeToDate(MonthRange.get("startDate"));
        Date medate =  convertLocalDateTimeToDate(MonthRange.get("endDate"));

     List<DriverSchedule> WeeklydriverActivity =    driverProRepository.findByDriverLogswithDateRange(driver.getDriverid(),wsdate, wedate);

        List<DriverSchedule> MonthlydriverActivity =    driverProRepository.findByDriverLogswithDateRange(driver.getDriverid(),msdate, medate);
        List<DriverSchedule> SelectedDatedriverActivity =    driverProRepository.findByDriverLogswithDateRange(driver.getDriverid(),dddate, dddate);


        Long selectedWorkedMins = calculateWorkedMinutes(SelectedDatedriverActivity);
        Long selectedShiftMins = convertToMinutes(driver.getDriverDayShiftHrs());

        Long weeklyWorkedMins = calculateWorkedMinutes(WeeklydriverActivity);
        Long weeklyShiftMins = convertToMinutes(driver.getDriverWeekShiftHrs());

        Long monthlyWorkedMins = calculateWorkedMinutes(MonthlydriverActivity);
        Long monthlyShiftMins = calculateWorkedMinutes_fixed(convertDateToLocalDate(msdate) , convertDateToLocalDate(medate));

      driverVO.setMonthRemHrs(monthlyShiftMins - monthlyWorkedMins);
      driverVO.setWeeklRemHrs(weeklyShiftMins - weeklyWorkedMins);
      driverVO.setMonthCycleHrs(monthlyShiftMins);
      driverVO.setWeekCycleHrs(weeklyShiftMins);
      driverVO.setScheduledHrs(selectedShiftMins - selectedWorkedMins);
      driverVO.setShiftHrs(selectedShiftMins);
      driverVO.setMonthWorkedHrs(monthlyWorkedMins);
      driverVO.setWeeklyWorkedHrs(weeklyWorkedMins);
      driverVO.setDayWorkedHrs(selectedWorkedMins);

        return driverVO;
    }

    public static long convertToMinutes(String time) {
        long hours, minutes;

        if (time.contains(":")) {
            // HH:MM format
            String[] parts = time.split(":");
            hours = Long.parseLong(parts[0]);
            minutes = Long.parseLong(parts[1]);
        } else {
            // HHMM format
            if (time.length() != 4) {
                throw new IllegalArgumentException("Invalid HHMM time format");
            }
            hours = Long.parseLong(time.substring(0, 2));
            minutes = Long.parseLong(time.substring(2, 4));
        }

        return hours * 60 + minutes;
    }

    private Long calculateWorkedMinutes_fixed(LocalDate startDate, LocalDate endDate) {

        long daysBetween = ChronoUnit.DAYS.between(startDate,endDate) + 1;
        return daysBetween * 480L;
    }

    private Long calculateWorkedMinutes(List<DriverSchedule> schedules) {
        if (schedules == null || schedules.isEmpty()) {
            return 0L;
        }
        return schedules.stream()
                .mapToLong(DriverSchedule::getWorked_mints)
                .sum();
    }

    private Long calculateShiftMinutes(List<DriverSchedule> schedules) {
        if (schedules == null || schedules.isEmpty()) {
            return 0L;
        }
        return schedules.stream()
                .mapToLong(DriverSchedule::getShiftMints)
                .sum();
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static Map<String, LocalDateTime> getWeekRange(LocalDate date) {
        Map<String, LocalDateTime> result = new HashMap<>();

        LocalDate startDate = date.with(DayOfWeek.MONDAY);
        LocalDate endDate = date.with(DayOfWeek.SUNDAY);

        result.put("startDate", startDate.atStartOfDay());
        result.put("endDate", endDate.atTime(LocalTime.MAX));

        return result;
    }

    public static Map<String, LocalDateTime> getMonthRange(LocalDate date) {
        Map<String, LocalDateTime> result = new HashMap<>();

        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());

        result.put("startDate", startDate.atStartOfDay());
        result.put("endDate", endDate.atTime(LocalTime.MAX));

        return result;
    }

    @Async
    public CompletableFuture<List<PickUPVO>> getPickUps(List<String> site, Date date, List<String> pickupList, Map<String, String> dropsVehicleMap) {
        List<PickUP> pickUPS = null;
        List<PickUPVO> pickUPVOList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        String Sites =  this.ListtoString(site);
        if(!StringUtils.isEmpty(site)) {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(PICKUP_QUERY, dbSchema,
                    MessageFormat.format(SITE_DATE, Sites, dateFormat.format(date))), paramMap);
             //log.info(resultList);
           // pickUPS = pickupRepository.findByDocdate(date);
        }else {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(PICKUP_QUERY, dbSchema,
                    MessageFormat.format(ONLY_DATE, dateFormat.format(date))), paramMap);
         //   pickUPS = pickupRepository.findByDocdate(date);
           // log.info(resultList);
        }
//        if(!CollectionUtils.isEmpty(pickUPS)) {
//            pickUPVOList = pickUPS.parallelStream().map(a-> this.convertPickups(a))
//                    .collect(Collectors.toList());
//        }
        if(!CollectionUtils.isEmpty(resultList)) {
            /*dropsList = resultList.stream().map(a-> this.convertDrops(a))
                    .collect(Collectors.toList());*/
            pickUPVOList = this.convertPickups(resultList, pickupList, dropsVehicleMap);
        }
        return CompletableFuture.completedFuture(pickUPVOList);
    }

    private List<PickUPVO> convertPickups(List<Map<String, Object>> list, List<String> pickupList, Map<String, String> dropsVehicleMap) {
        Map<String, PickUPVO> dropsMap = new HashMap<>();
        for(Map<String, Object> map: list) {
            String docNum = this.convertToString(map.get("DOCNUM"));
            PickUPVO dropsVO = null;
            if(Objects.nonNull(dropsMap.get(docNum))) {
                dropsVO = dropsMap.get(docNum);
            }else {
                dropsVO = new PickUPVO();
            }
            dropsVO.setDocnum(docNum);
            if(pickupList.contains(docNum)) {
                dropsVO.setType(TransportConstants.ALLOCATED);
            }
            this.convertPickUPVO(map, dropsVO, dropsVehicleMap);
            dropsMap.put(docNum, dropsVO);
        }
        return new ArrayList<>(dropsMap.values());
    }

    private PickUPVO convertPickUPVO(Map<String, Object> drops, PickUPVO dropsVO, Map<String, String> dropsVehicleMap) {
        dropsVO.setDoctype(this.convertToString(drops.get("DOCTYPE")));
        dropsVO.setDocdate(this.convertToString(drops.get("DOCDATE")));
        dropsVO.setRouteTag(this.convertToString(drops.get("ROUTETAG")));
        if(Objects.nonNull(drops.get("ROUTECOLOR"))) {
            dropsVO.setRouteColor(this.convertToString(drops.get("ROUTECOLOR")));
        }else {
            dropsVO.setRouteColor(";font-style:normal;background-color:#92a8d1");
        }
        dropsVO.setRouteTagFRA(this.convertToString(drops.get("ROUTETAGFRA")));
        dropsVO.setFromTime(this.convertToString(drops.get("FROMTIME")));

        dropsVO.setPriority(this.convertToString(drops.get("PRIORITY")));
        dropsVO.setSkills(this.convertToString(drops.get("SKILLSET")));
        dropsVO.setAprodCategDesc(this.convertToString(drops.get("APRODCATEGDESC")));
        dropsVO.setAroutecodeDesc(this.convertToString(drops.get("AROUTECOCDESC")));
        dropsVO.setAvehClassListDesc(this.convertToString(drops.get("AVEHCLASSLISTDESC")));


        dropsVO.setToTime(this.convertToString(drops.get("TOTIME")));
        dropsVO.setDlvystatus(this.convertToString(drops.get("DLVYSTATUS")));
        dropsVO.setPtlink(this.convertToString(drops.get("PTLINK")));
        dropsVO.setPtheader(this.convertToString(drops.get("PTHEADER")));
        dropsVO.setMovtype(this.convertToString(drops.get("MOVTYPE")));
        dropsVO.setPoscode(this.convertToString(drops.get("POSCODE")));
        dropsVO.setBpcode(this.convertToString(drops.get("BPCODE")));
        dropsVO.setBpname(this.convertToString(drops.get("BPNAME")));
        dropsVO.setAdrescode(this.convertToString(drops.get("ADRESCODE")));
        dropsVO.setAdresname(this.convertToString(drops.get("ADRESNAME")));
        dropsVO.setSite(this.convertToString(drops.get("SITE")));
        dropsVO.setAdrescode(this.convertToString(drops.get("ADRESCODE")));
        dropsVO.setAdresname(this.convertToString(drops.get("ADRESNAME")));

        if(Objects.nonNull(drops.get("CARRCOLOR"))) {
            dropsVO.setCarrierColor(this.convertToString(drops.get("CARRCOLOR")));
        }else {
            dropsVO.setCarrierColor(";font-style:normal;background-color:#92a8d1");
        }
        dropsVO.setTrailer(this.convertToString(drops.get("TRAILER")));
        dropsVO.setCarrier(this.convertToString(drops.get("CARRIER")));
        dropsVO.setDrivercode(this.convertToString(drops.get("DRIVERCODE")));
        dropsVO.setCity(this.convertToString(drops.get("CITY")));
        dropsVO.setNetweight(this.convertToBigDecimal(drops.get("NETWEIGHT")));
        dropsVO.setWeightunit(this.convertToString(drops.get("WEIGHTUNIT")));
        dropsVO.setVolume(this.convertToBigDecimal(drops.get("VOLUME")));
        dropsVO.setVolume_unit(this.convertToString(drops.get("VOLUME_UNIT")));
        dropsVO.setLng(this.convertDouble(drops.get("GPS_X")));
        dropsVO.setLat(this.convertDouble(drops.get("GPS_Y")));
        dropsVO.setVehicleCode(dropsVehicleMap.get(dropsVO.getDocnum()));
        dropsVO.setTripno(this.convertToString(drops.get("TRIPNO")));
      //  dropsVO.setDlvflg(this.convertToString(drops.get("DLVFLG")));
        dropsVO.setPairedDoc(this.convertToString(drops.get("PAIREDDOCUMENT")));
        dropsVO.setServiceTime(this.convertToString(drops.get("SERVICETIME")));
        dropsVO.setWaitingTime(this.convertToString(drops.get("WaitingTime")));
        dropsVO.setVehType(this.convertToString(drops.get("vehType")));
        dropsVO.setBPServiceTime(this.convertToString(drops.get("BPServiceTime")));

      //  dropsVO.setLogisticDetails(this.convertToString(drops.get("Speciality")));
        dropsVO.setLogisticDetails("");
       // dropsVO.setBPServiceTime(this.convertToString(drops.get("BPServiceTime")));
        if(Objects.isNull(dropsVO.getProducts())) {
            List<ProductVO> productVOS = new ArrayList<>();
            dropsVO.setProducts(productVOS);
        }
        dropsVO.getProducts().add(getProductVO(drops));
        return dropsVO;
    }

    private double  stackHeightConv(BigDecimal wt){
        double xy;
        BigDecimal res  = BigDecimal.ZERO;
        if(wt == null){
            res  = BigDecimal.ZERO;
        }
        else {
            res = wt;

        }
        xy = res.doubleValue();
        return xy;
    }




    private int  DecimaltoString(BigDecimal wt){
         int xy;
         BigDecimal res  = BigDecimal.ZERO;
          if(wt == null){
              res  = BigDecimal.ZERO;
                       }
          else {
             res = wt.multiply(new BigDecimal(60));

          }
        xy = res.intValue();
        return xy;
    }



    private int abc(Short a){
        int res;
        if(a == null || a == 0){
            res = 0;

        }
        else{
            res = a*60;
            
        }
        return res;
    }

    private String HeightConv(Short a){
        String res;
        int x;
        if(a == null || a == 0 || a == 1){
            res = "";
        }
        else{
            x = a + 13;
            res = String.valueOf(x);
        }
        return res;
    }


    private String LoadingOrderConv(Short lorder)
    {
        String res;
        if(lorder == null || lorder == 0 || lorder == 1){
            return "";
        }
        else if(lorder == 2) {
            return "Sens 80";
        }
        else if(lorder == 3) {
            return "Sens 100";
        }
        else if(lorder == 4) {
            return "Habituel";
        }
        else{
            return "";
        }
    }
    private String PackingConv(Short val)
    {
       String res;

        if(val == null || val == 0 || val == 1){
            return "";
        }
        else if(val == 2){
            return "Deboitee";
        }
        else if(val == 3){
            return "Emboitee";
        }
        else if(val == 4){
            return "Les duex";
        }
        else {
            return "";
        }
    }

    private PickUPVO convertPickups(PickUP pickUP) {
        PickUPVO pickUPVO = new PickUPVO();
        pickUPVO.setMovtype(pickUP.getMovtype());
        pickUPVO.setDocnum(pickUP.getDocnum());
        pickUPVO.setCity(pickUP.getCity());
        pickUPVO.setTrailer(pickUP.getTrailer());
        pickUPVO.setCarrier(pickUP.getCarrier());
        pickUPVO.setDrivercode(pickUP.getDrivercode());
        pickUPVO.setPoscode(pickUP.getPoscode());
        if(!StringUtils.isEmpty(pickUP.getGps_x()) && !StringUtils.isEmpty(pickUP.getGps_y())) {
            try {
                pickUPVO.setLng(Double.parseDouble(pickUP.getGps_x()));
                pickUPVO.setLat(Double.parseDouble(pickUP.getGps_y()));
            }catch(Exception e) {
            }
        }
        return pickUPVO;
    }
    private String constructLogistics(Object loadBay, Object tailGate) {
        StringBuilder sb = new StringBuilder();
        if(Objects.nonNull(loadBay)) {
            sb.append("LoadBay : "+ (String) loadBay);
            sb.append("\n");
        }
        if(Objects.nonNull(tailGate)) {
            sb.append("tailGate : "+ (String) loadBay);
        }
        return sb.toString();
    }

    public String ListtoString(List<String> sites)
    {
        String preparan = "(";
        String ListCommaSeparated = sites.stream()
                .map(String::toUpperCase)
                .map(String->("'"+String+"'"))
                .collect(Collectors.joining(","));

        String postparan = ")";
        String totalString = preparan+ListCommaSeparated+postparan;

        return totalString;
    }

    @Async
    public CompletableFuture<List<DropsVO>> getDrops(List<String> site, Date date, List<String> dropList, Map<String, String> dropsVehicleMap) {
        List<Drops> drops = null;
        List<DropsVO> dropsList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        String Sites =  this.ListtoString(site);
        if(!StringUtils.isEmpty(site)) {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(DORPS_QUERY, dbSchema,
                    MessageFormat.format(SITE_DATE, Sites, dateFormat.format(date))), paramMap);
           // log.info(resultList);
          //  drops =  dropsRepository.findByDocdate(date);

        }else {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(DORPS_QUERY, dbSchema,
                    MessageFormat.format(ONLY_DATE, dateFormat.format(date))), paramMap);
         //   drops = dropsRepository.findByDocdate(date);
           // log.info(resultList);
        }
        if(!CollectionUtils.isEmpty(resultList)) {
            /*dropsList = resultList.stream().map(a-> this.convertDrops(a))
                    .collect(Collectors.toList());*/
            dropsList = this.convertDrops(resultList, dropList, dropsVehicleMap);
        }
        return CompletableFuture.completedFuture(dropsList);
    }

    private List<DropsVO> convertDrops(List<Map<String, Object>> list, List<String> dropsList, Map<String, String> dropsVehicleMap ) {
        Map<String, DropsVO> dropsMap = new HashMap<>();
        for(Map<String, Object> map: list) {
            String docNum = this.convertToString(map.get("DOCNUM"));
            DropsVO dropsVO = null;
            if(Objects.nonNull(dropsMap.get(docNum))) {
                dropsVO = dropsMap.get(docNum);
            }else {
                dropsVO = new DropsVO();
            }
            dropsVO.setDocnum(docNum);
            if(dropsList.contains(docNum)) {
                dropsVO.setType(TransportConstants.ALLOCATED);
            }
            this.convertDrops(map, dropsVO, dropsVehicleMap);
            dropsMap.put(docNum, dropsVO);
        }
        return new ArrayList<>(dropsMap.values());
    }

    private DropsVO convertDrops(Map<String, Object> drops, DropsVO dropsVO, Map<String, String> dropsVehicleMap) {
        dropsVO.setDoctype(this.convertToString(drops.get("DOCTYPE")));
        dropsVO.setDocdate(this.convertToString(drops.get("DOCDATE")));
        dropsVO.setRouteTag(this.convertToString(drops.get("ROUTETAG")));
        if(Objects.nonNull(drops.get("ROUTECOLOR"))) {
            dropsVO.setRouteColor(this.convertToString(drops.get("ROUTECOLOR")));
        }else {
            dropsVO.setRouteColor(";font-style:normal;background-color:#92a8d1");
        }
        dropsVO.setRouteTagFRA(this.convertToString(drops.get("ROUTETAGFRA")));
        dropsVO.setFromTime(this.convertToString(drops.get("FROMTIME")));
        dropsVO.setToTime(this.convertToString(drops.get("TOTIME")));
        dropsVO.setPriority(this.convertToString(drops.get("PRIORITY")));
        dropsVO.setSkills(this.convertToString(drops.get("SKILLSET")));
        dropsVO.setAprodCategDesc(this.convertToString(drops.get("APRODCATEGDESC")));
        dropsVO.setAroutecodeDesc(this.convertToString(drops.get("AROUTECOCDESC")));
        dropsVO.setAvehClassListDesc(this.convertToString(drops.get("AVEHCLASSLISTDESC")));


        dropsVO.setDlvystatus(this.convertToString(drops.get("DLVYSTATUS")));
        dropsVO.setPrelistCode(this.convertToString(drops.get("PRELISTCODE")));
        dropsVO.setPtlink(this.convertToString(drops.get("PTLINK")));
        dropsVO.setPtheader(this.convertToString(drops.get("PTHEADER")));
        dropsVO.setPoscode(this.convertToString(drops.get("POSCODE")));
        dropsVO.setCity(this.convertToString(drops.get("CITY")));
		dropsVO.setRouteCode(this.convertToString(drops.get("ROUTECODE")));
        dropsVO.setRouteBgColor(this.convertToString(drops.get("ROUTECODEBGCLR")));
        dropsVO.setRouteCodeDesc(this.convertToString(drops.get("ROUTECODEDESC")));
        dropsVO.setTrailer(this.convertToString(drops.get("TRAILER")));
        dropsVO.setCarrier(this.convertToString(drops.get("CARRIER")));
        dropsVO.setDrivercode(this.convertToString(drops.get("DRIVERCODE")));
        dropsVO.setBpcode(this.convertToString(drops.get("BPCODE")));
        dropsVO.setBpname(this.convertToString(drops.get("BPNAME")));
        dropsVO.setAdrescode(this.convertToString(drops.get("ADRESCODE")));
        dropsVO.setAdresname(this.convertToString(drops.get("ADRESNAME")));
        dropsVO.setSite(this.convertToString(drops.get("SITE")));
        if(Objects.nonNull(drops.get("CARRCOLOR"))) {
            dropsVO.setCarrierColor(this.convertToString(drops.get("CARRCOLOR")));
        }else {
            dropsVO.setCarrierColor(";font-style:normal;background-color:#92a8d1");
        }
        dropsVO.setNetweight(this.convertToBigDecimal(drops.get("NETWEIGHT")));
        dropsVO.setWeightunit(this.convertToString(drops.get("WEIGHTUNIT")));
        dropsVO.setVolume(this.convertToBigDecimal(drops.get("VOLUME")));
        dropsVO.setVolume_unit(this.convertToString(drops.get("VOLUME_UNIT")));
        dropsVO.setLng(this.convertDouble(drops.get("GPS_X")));
        dropsVO.setLat(this.convertDouble(drops.get("GPS_Y")));
        dropsVO.setVehicleCode(dropsVehicleMap.get(dropsVO.getDocnum()));
        dropsVO.setTripno(this.convertToString(drops.get("TRIPNO")));
        dropsVO.setPairedDoc(this.convertToString(drops.get("PAIREDDOC")));
        dropsVO.setServiceTime(this.convertToString(drops.get("SERVICETIME")));
        dropsVO.setWaitingTime(this.convertToString(drops.get("WaitingTime")));
        dropsVO.setBPServiceTime(this.convertToString(drops.get("BPServiceTime")));

        dropsVO.setLogisticDetails("");
        //dropsVO.setLogisticDetails(this.constructDropSpeciality(this.convertToString(drops.get("loadBay")),this.convertToString(drops.get("tailGate")),this.DecimaltoString((BigDecimal)drops.get("BPServiceTime")),this.DecimaltoString((BigDecimal) drops.get("WaitingTime"));
        //dropsVO.setBPServiceTime(this.convertToString(drops.get("BPServiceTime")));
        if(Objects.isNull(dropsVO.getProducts())) {
            List<ProductVO> productVOS = new ArrayList<>();
            dropsVO.setProducts(productVOS);
        }
        dropsVO.getProducts().add(getProductVO(drops));

        return dropsVO;
    }

    private ProductVO getProductVO(Map<String, Object> map) {
        ProductVO productVO = new ProductVO();
        productVO.setProductCode(this.convertToString(map.get("PRODUCTCODE")));
        productVO.setDocLineNum(this.convertToString(map.get("DOCLINENO")));
        productVO.setProductName(this.convertToString(map.get("PRODUCTNAME")));
        productVO.setProductCateg(this.convertToString(map.get("PRODUCTCATEG")));
        productVO.setQuantity(this.convertToString(map.get("QUANTITY")));
        productVO.setConvQty(this.convertToString(map.get("CONV_QTY")));
        productVO.setWeight(this.convertToString(map.get("WEIGHT")));
        productVO.setVolume(this.convertToString(map.get("VOLUME")));
        productVO.setWei_unit(this.convertToString(map.get("WEI_UNIT")));
        productVO.setVol_unit(this.convertToString(map.get("VOL_UNIT")));
        productVO.setPuu(this.convertToString(map.get("PURUNIT")));

        if(null != productVO.getQuantity() && productVO.getQuantity().length() > 4) {
            String quant = productVO.getQuantity().substring(0, productVO.getQuantity().indexOf("."));
            productVO.setQuantity(quant);
        }
        productVO.setUom(this.convertToString(map.get("UOM")));
        return productVO;
    }

    private String convertToString(Object value) {
        if(Objects.nonNull(value)) return value.toString();
        return null;
    }

    private int convertObjectToint(Object value) {
        if(Objects.nonNull(value)) return (int)value;
        return 0;
    }

    private double convertObjectTodouble(Object value) {
        if(Objects.nonNull(value)) return (double)value;
        return 0.00;
    }

    private int waitingtimeConversion(Object value){

        double res =  this.convertObjectTodouble(value);
       return this.convertDectoString(res);

    }




    private Date convertToDate(Object value) {
        if(Objects.nonNull(value)) {
            try {
                return format.parse(value.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Double convertDouble(Object value) {
        if(Objects.nonNull(value)) return Double.parseDouble(value.toString());
        return 0.0;
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if(Objects.nonNull(value)) return (BigDecimal) value;
        return new BigDecimal(0);
    }

    private DropsVO convertDrops(Drops drops) {
        DropsVO dropsVO = new DropsVO();
        dropsVO.setDoctype(drops.getDoctype());
        dropsVO.setDocnum(drops.getDocnum());
        dropsVO.setFromTime(drops.getFromTime());
        dropsVO.setToTime(drops.getToTime());
        dropsVO.setRouteTag(drops.getRouteTag());
        dropsVO.setRouteColor(drops.getRouteColor());
        dropsVO.setRouteTagFRA(drops.getRouteTagFRA());
		   dropsVO.setRouteCode(drops.getRouteCode());
        dropsVO.setRouteBgColor(drops.getRouteBgColor());
		dropsVO.setRouteCodeDesc(drops.getRouteCodeDesc());
        dropsVO.setPoscode(drops.getPoscode());
        dropsVO.setPrelistCode(drops.getPrelistCode());
        dropsVO.setCity(drops.getCity());
        dropsVO.setTrailer(drops.getTrailer());
        dropsVO.setCarrier(drops.getCarrier());
        dropsVO.setDrivercode(drops.getDrivercode());
        dropsVO.setNetweight(drops.getNetweight());
        dropsVO.setWeightunit(drops.getWeightunit());
        dropsVO.setVolume(drops.getNetweight());
        dropsVO.setVolume_unit(drops.getVolume_unit());
        if(!StringUtils.isEmpty(drops.getGps_x()) && !StringUtils.isEmpty(drops.getGps_y())) {
            try {
                dropsVO.setLng(Double.parseDouble(drops.getGps_x()));
                dropsVO.setLat(Double.parseDouble(drops.getGps_y()));
            }catch(Exception e) {
            }
        }
        return dropsVO;
    }



    //DATE RANGE pickups
    @Async
    public CompletableFuture<List<PickUPVO>> getPickUpsWithRange(List<String> site, Date sdate,Date edate, List<String> pickupList, Map<String, String> dropsVehicleMap) {
        List<PickUP> pickUPS = null;
        List<PickUPVO> pickUPVOList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        String Sites =  this.ListtoString(site);
        if(!StringUtils.isEmpty(site)) {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(PICKUP_QUERY, dbSchema,
                    MessageFormat.format(SITE_DATERANGE, Sites, dateFormat.format(sdate),dateFormat.format(edate))), paramMap);
            //log.info(resultList);
            // pickUPS = pickupRepository.findByDocdate(date);
        }else {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(PICKUP_QUERY, dbSchema,
                    MessageFormat.format(ONLY_DATERANGE, dateFormat.format(sdate), dateFormat.format(edate))), paramMap);
            //   pickUPS = pickupRepository.findByDocdate(date);
            // log.info(resultList);
        }
//        if(!CollectionUtils.isEmpty(pickUPS)) {
//            pickUPVOList = pickUPS.parallelStream().map(a-> this.convertPickups(a))
//                    .collect(Collectors.toList());
//        }
        if(!CollectionUtils.isEmpty(resultList)) {
            /*dropsList = resultList.stream().map(a-> this.convertDrops(a))
                    .collect(Collectors.toList());*/
            pickUPVOList = this.convertPickups(resultList, pickupList, dropsVehicleMap);
        }
        return CompletableFuture.completedFuture(pickUPVOList);
    }


    //Date range Drops
    @Async
    public CompletableFuture<List<DropsVO>> getDropsWithRange(List<String> site, Date sdate,Date edate, List<String> dropList, Map<String, String> dropsVehicleMap) {
        List<Drops> drops = null;
        List<DropsVO> dropsList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        String Sites =  this.ListtoString(site);
        if(!StringUtils.isEmpty(site)) {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(DORPS_QUERY, dbSchema,
                    MessageFormat.format(SITE_DATERANGE, Sites, dateFormat.format(sdate),dateFormat.format(edate))), paramMap);
            // log.info(resultList);
            //  drops =  dropsRepository.findByDocdate(date);

        }else {
            resultList = jdbcTemplate.queryForList(MessageFormat.format(DORPS_QUERY, dbSchema,
                    MessageFormat.format(ONLY_DATERANGE, dateFormat.format(sdate),dateFormat.format(edate) )), paramMap);
            //   drops = dropsRepository.findByDocdate(date);
            // log.info(resultList);
        }
        if(!CollectionUtils.isEmpty(resultList)) {
            /*dropsList = resultList.stream().map(a-> this.convertDrops(a))
                    .collect(Collectors.toList());*/
            dropsList = this.convertDrops(resultList, dropList, dropsVehicleMap);
        }
        return CompletableFuture.completedFuture(dropsList);
    }



}
