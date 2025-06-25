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
@Table(name = "vw_ListofDrivers", schema = "TMSNEW")
public class DriverList {



    @Id
    @Column(name = "DRIVERID_0", nullable = false, length = 30)
    private String driverId;

    @Column(name = "DRIVER_0", nullable = false, length = 30)
    private String driver;

    @Column(name = "LICENUM_0", nullable = false, length = 30)
    private String licenum;

    @Column(name = "SITE", nullable = false, length = 30)
    private String site;

    @Column(name = "COLOR", nullable = false, length = 60)
    private String color;

    @Column(name = "ACTIVE_0")
    private int active;

    @Column(name = "VEHICLE", nullable = false, length = 10)
    private String vehicle;

    @Column(name = "LASTUPDATEDDATE", nullable = false)
    private String lastUpdatedDate;

    @Column(name = "LASTUPDATEDTIME", nullable = false, length = 50)
    private String lastUpdatedTime;

    @Column(name = "CURRSTATUS")
    private String currStatus;

    @Column(name = "ROUTE")
    private String route;


}

