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
public class DriverDetails {

    private String driverId;
    private String date;
    private String shift;
    private String driving;
    private String inViolation;
    private String from;
    private String to;
    private String violations;
    private String driverName;
    private String driverLicense;
    private List<DataPoint> dataPoints;

}
