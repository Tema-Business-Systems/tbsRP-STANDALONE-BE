package com.transport.hos.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.hos.model.Driver;
import com.transport.hos.model.DriverDetails;
import com.transport.hos.model.DriverList;
import com.transport.hos.model.VehicleDetails;
import com.transport.hos.repository.DriverListRepository;
import com.transport.hos.response.DriverListVO;
import com.transport.tracking.model.*;
import com.transport.tracking.repository.DriverProRepository;
import com.transport.tracking.repository.DriverRepository;
import com.transport.tracking.response.DocStatusVO;
import com.transport.tracking.response.DriverVO;
import com.transport.tracking.response.TripVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HOSService {


    @Autowired
    private DriverListRepository driverListRepository;

    @Autowired
    private DriverProRepository driverProRepository;


    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Driver> getDriversByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        return loadJsonFromFile("drivers", Driver.class);
    }


    public List<DriverList> getDriversListbySite(String site) throws IOException {
        List<DriverList> driversList = new ArrayList<>();

      driversList = driverListRepository.findBySite(site);


        return driversList;
    }




    public List<DriverListVO> getDriversListbySiteAndDate(String site, Date date) throws IOException {
        List<DriverList> driversList = new ArrayList<>();

        driversList = driverListRepository.findBySite(site);

        if(!CollectionUtils.isEmpty(driversList)) {
            return driversList.stream().map(a -> getDriverListVO(a, date)).collect(Collectors.toList());
        }
        return new ArrayList<>();

       // return driversList;
    }

    private DriverListVO getDriverListVO(DriverList driverList, Date dddate) {
        DriverListVO driverListVO = new DriverListVO();
        BeanUtils.copyProperties(driverList, driverListVO);

        LocalDate tempdate =  convertDateToLocalDate(dddate);
        Map<String, LocalDateTime> weekRange = getWeekRange(tempdate);
        Map<String, LocalDateTime> MonthRange = getMonthRange(tempdate);

        Date wsdate = convertLocalDateTimeToDate(weekRange.get("startDate"));
        Date wedate = convertLocalDateTimeToDate(weekRange.get("endDate"));
        Date msdate =  convertLocalDateTimeToDate(MonthRange.get("startDate"));
        Date medate =  convertLocalDateTimeToDate(MonthRange.get("endDate"));

        List<DriverSchedule> WeeklydriverActivity =    driverProRepository.findByDriverLogswithDateRange(driverList.getDriverId(),wsdate, wedate);

        List<DriverSchedule> MonthlydriverActivity =    driverProRepository.findByDriverLogswithDateRange(driverList.getDriverId(),msdate, medate);
        List<DriverSchedule> SelectedDatedriverActivity =    driverProRepository.findByDriverLogswithDateRange(driverList.getDriverId(),dddate, dddate);


        Long selectedWorkedMins = calculateWorkedMinutes(SelectedDatedriverActivity);
        Long selectedShiftMins = 480L;

        Long weeklyWorkedMins = calculateWorkedMinutes(WeeklydriverActivity);
        Long weeklyShiftMins = calculateWorkedMinutes_fixed( convertDateToLocalDate(wsdate) , convertDateToLocalDate(wedate) );

        Long monthlyWorkedMins = calculateWorkedMinutes(MonthlydriverActivity);
        Long monthlyShiftMins = calculateWorkedMinutes_fixed(convertDateToLocalDate(msdate) , convertDateToLocalDate(medate));

        driverListVO.setMonthRemHrs(monthlyShiftMins - monthlyWorkedMins);
        driverListVO.setWeeklRemHrs(weeklyShiftMins - weeklyWorkedMins);
        driverListVO.setMonthCycleHrs(monthlyShiftMins);
        driverListVO.setWeekCycleHrs(weeklyShiftMins);
        driverListVO.setScheduledHrs(selectedShiftMins - selectedWorkedMins);
        driverListVO.setShiftHrs(selectedShiftMins);
        driverListVO.setMonthWorkedHrs(monthlyWorkedMins);
        driverListVO.setWeeklyWorkedHrs(weeklyWorkedMins);
        driverListVO.setDayWorkedHrs(selectedWorkedMins);


        return driverListVO;
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


    public List<DriverDetails> getDriverDetailsByDriverId(String driverId) {
        return loadJsonFromFile("driverDetails", DriverDetails.class);
    }

    public VehicleDetails getVehicleDetails(LocalDateTime startDate, LocalDateTime endDate) {
        return loadJsonFromFile("vehicleDetails", VehicleDetails.class).get(0);
    }

    public static <T> List<T> loadJsonFromFile(String fileName, Class<T> clazz) {
        // Use the class loader to load the file as an InputStream
        try (InputStream inputStream = HOSService.class.getClassLoader().getResourceAsStream("json/"+fileName+".json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            // Parse JSON into a list of objects of the specified type
            return mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
