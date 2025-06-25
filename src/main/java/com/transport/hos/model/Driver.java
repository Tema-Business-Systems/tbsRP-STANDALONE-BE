package com.transport.hos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    /*private String driverId;
    private List<DriverLogs> logs;*/

    private String driverId;
    private String driverName;
    private String driverStatus;
    private String timeInStatus;
    private String vehicleName;
    private String timeBreak;
    private String driverRemaining;
    private String shiftRemaining;
    private String cycleRemaining;
    private String cycleTomorrow;
    private String driverViolationToday;
    private String driverViolationCycle;
    private String driverLicense;


}

