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
@Table(name = "XX10CDRVV", schema = "TMSNEW")
public class DriverViolation {

    @Id
    @Column(name = "UPDTICK_0", nullable = false)
    private int updateTicket;

    @Column(name = "VIOLATIONID_0", nullable = false)
    private int violationId;

    @Column(name = "TRIPID_0", nullable = false, length = 30)
    private String tripId;

    @Column(name = "VTYPE_0", nullable = false)
    private byte violationType;

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

    @Column(name = "VDESC_0", nullable = false, length = 40)
    private String violationDescription;

    @Column(name = "ROWID", nullable = false, precision = 38, scale = 0)
    private BigDecimal rowId;
}

