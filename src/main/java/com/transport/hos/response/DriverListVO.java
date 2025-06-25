package com.transport.hos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DriverListVO {
    private String driverId;
    private String driver;
    private String licenum;
    private String site;
    private String color;
    private int active;
    private String vehicle;
    private String lastUpdatedDate;
    private String lastUpdatedTime;
    private String currStatus;
    private String route;
    private Long weekCycleHrs;
    private Long weeklRemHrs;
    private Long shiftHrs;
    private Long scheduledHrs;
    private Long monthCycleHrs;
    private Long monthRemHrs;
    private Long monthWorkedHrs;
    private Long weeklyWorkedHrs;
    private Long dayWorkedHrs;

}

