package com.transport.tracking.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SiteVO {

    public String id;
    public String value;
    public String defflg;
    public double lat;
    public double lng;
    public String cur;
    public String distunit;
    public String volunit;
    public String massunit;
    public int maxStops;
    public String driverDayShift;
    public String driverWeekShift;
    public String driverBreakDuration;
    public String drivingHrsBtwBreak;
    public String totalDrivingHrsPerDay;
    public String totalWorkingHrsPerDay;

}
