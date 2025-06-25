package com.transport.hos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DriverBreakVO {
    private int updateTicket;
    private int breakId;
    private String driverId;
    private String tripId;
    private Date startDate;
    private String startTime;
    private Date endDate;
    private String endTime;
    private BigDecimal totalHours;
    private String startLat;
    private String startLng;
    private String endLat;
    private String endLng;
    private byte breakType;
    private Date createdDateTime;
    private Date updatedDateTime;
    private byte[] auuid;
    private String createdUser;
    private String updatedUser;
}

