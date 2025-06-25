package com.transport.hos.controller;

import com.transport.hos.model.Driver;
import com.transport.hos.model.DriverDetails;
import com.transport.hos.model.DriverList;
import com.transport.hos.model.VehicleDetails;
import com.transport.hos.response.DriverListVO;
import com.transport.hos.service.HOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:8082","http://localhost:8081","http://192.168.1.211:8081","http://192.168.1.211:8082","http://solutions.tema-systems.com:8081","http://solutions.tema-systems.com:8082"})
@RequestMapping("/api/v1/hos")
public class HOSController {

    @Autowired
    private HOSService hosService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/drivers")
    public List<Driver> getDriversByDateRange(
            @RequestParam(defaultValue = "2025-02-14") String startDate,
            @RequestParam(defaultValue = "2025-02-14") String endDate
    ) throws IOException {
        System.out.println("Testing..............");
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
        return hosService.getDriversByDateRange(start, end);
    }

    @GetMapping("/driversbySite")
    public List<DriverList> getDriversBySite(
            @RequestParam(name = "site", required = false) String site
    ) throws IOException {
        System.out.println("Testing..............");
         return hosService.getDriversListbySite(site);
    }


    @GetMapping("/driversbySiteandDate")
    public List<DriverListVO> getDriversBySiteAndDate(
            @RequestParam(name = "site", required = false) String site,
            @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date

            ) throws IOException {
        System.out.println("Testing..............");
        return hosService.getDriversListbySiteAndDate(site, date);
    }



    @GetMapping("/vehicles")
    public VehicleDetails getVehiclesByDateRange(
            @RequestParam(defaultValue = "2025-02-14") String startDate,
            @RequestParam(defaultValue = "2025-02-14") String endDate
    ) throws IOException {
        System.out.println("Testing..............");
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
        return hosService.getVehicleDetails(start, end);
    }

    @GetMapping("/driver")
    public List<DriverDetails> getDriverById(
            @RequestParam(defaultValue = "1") String driverId
    ) throws IOException {
        System.out.println("Testing..............");
        return hosService.getDriverDetailsByDriverId(driverId);
    }
}
