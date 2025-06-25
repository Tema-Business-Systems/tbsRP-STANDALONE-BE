package com.transport.hos.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "XX10CDRVLOGS", schema = "TMSNEW")
public class DriverLog {

    @Id
    @Column(name = "UPDTICK_0", nullable = false)
    private int updateTicket;

    @Column(name = "LOGID_0", nullable = false)
    private int logId;

    @Column(name = "DRIVERID_0", nullable = false, length = 30)
    private String driverId;

    @Column(name = "VEHICLEID_0", nullable = false, length = 10)
    private String vehicleId;

    @Column(name = "STARTDATE_0", nullable = false)
    private Date startDate;

    @Column(name = "STARTTIME_0", nullable = false, length = 50)
    private String startTime;

    @Column(name = "ENDDATE_0", nullable = false)
    private Date endDate;

    @Column(name = "ENDTIME_0", nullable = false, length = 50)
    private String endTime;

    @Column(name = "TOTALHRS_0", nullable = false, precision = 14, scale = 3)
    private BigDecimal totalHours;

    @Column(name = "STARTLAT_0", nullable = false, length = 30)
    private String startLatitude;

    @Column(name = "STARTLNG_0", nullable = false, length = 30)
    private String startLongitude;

    @Column(name = "ENDLAT_0", nullable = false, length = 30)
    private String endLatitude;

    @Column(name = "ENDLNG_0", nullable = false, length = 30)
    private String endLongitude;

    @Column(name = "LOGTYPE_0", nullable = false)
    private byte logType;

    @Column(name = "CREDATTIM_0", nullable = false)
    private Date createdDateTime;

    @Column(name = "UPDDATTIM_0", nullable = false)
    private Date updatedDateTime;

    @Column(name = "AUUID_0", nullable = false)
    private byte[] auuid;

    @Column(name = "CREUSR_0", nullable = false, length = 5)
    private String createdUser;

    @Column(name = "UPDUSR_0", nullable = false, length = 5)
    private String updatedUser;

    @Column(name = "ROWID", nullable = false, precision = 38, scale = 0)
    private BigDecimal rowId;
}

