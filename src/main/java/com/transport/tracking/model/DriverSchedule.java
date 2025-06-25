package com.transport.tracking.model;


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
@Table(name = "VW_DRIVER_HOS_RAW")
public class DriverSchedule {


      @Column(name= "DRIVERID_0")
      private String driverid;
      @Column(name= "VEHICLEID_0")
      private String vehicle;
      @Column(name= "ACTIVITY_DATE")
      private Date activityDate;
      @Column(name= "BREAK_MINUTES")
      private Long breakMints;
      @Column(name= "DRIVING_MINUTES")
      private Long driving_mints;
      @Column(name= "ONDUTY_MIN")
      private String onDutyTime;
      @Column(name= "OFFDUTY_MIN")
      private String offDutyTime;
      @Column(name= "WORKED_MINUTES")
      private Long worked_mints;
      @Column(name= "SHIFT_MINUTES")
      private Long shiftMints;
      @Column(name= "VIOLATION_FLAG")
      private String violation;
      @Id
      @Column(name= "ROWID")
      private BigDecimal rowid;



}
