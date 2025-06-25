package com.transport.hos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DriverViolationVO {
    private int updateTicket;
    private int violationId;
    private String tripId;
    private byte violationType;
    private Date createdDateTime;
    private Date updatedDateTime;
    private byte[] auuid;
    private String createdUser;
    private String updatedUser;
    private String violationDescription;
    private BigDecimal rowId;
}

