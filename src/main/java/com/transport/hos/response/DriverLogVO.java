package com.transport.hos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DriverLogVO {
    private int updateTicket;
    private int logId;
    private String driverId;
    private String vehicleId;
    private Date startDate;
    private String startTime;
    private Date endDate;
    private String endTime;
    private BigDecimal totalHours;
    private String startLat;
    private String startLng;
    private String endLat;
    private String endLng;
    private byte logType;
    private Date createdDateTime;
    private Date updatedDateTime;
    private byte[] auuid;
    private String createdUser;
    private String updatedUser;
    private BigDecimal rowId;
}

