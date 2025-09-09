package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "XTMS_T_SITE")
public class XtmsSite {

    @Column(name = "SITE_ID_0")
    private String siteId;

    @Column(name = "SITE_NAME_0")
    private String siteName;

    @Column(name = "FCYSHO_0")
    private String fcysho;

    @Column(name = "CRY_0")
    private String cry;

    @Column(name = "BPAADD_0")
    private String bpaadd;

    @Column(name = "UPDUSR_0")
    private String updusr;

    @Column(name = "UPDDAT_0")
    private Date upddat;

    @Column(name = "CREDATTIM_0")
    private Date credattim;

    @Column(name = "UPDDATTIM_0")
    private Date upddattim;

    @Column(name = "XX10C_GEOY_0")
    private String xx10cGeoy;

    @Column(name = "XX10C_GEOX_0")
    private String xx10cGeox;

    @Column(name = "XTMSFCY_0")
    private Integer xtmsfcy;

    @Column(name = "XUPDUSR_0")
    private String xupdusr;

    @Column(name = "XUPDDATE_0")
    private Date xupdate;

    @Column(name = "X1CGEOSO_0")
    private Short x1cgeoso;

    @Column(name = "XADD_0")
    private String xadd;

    @Column(name = "XADDDES_0")
    private String xadddes;

    @Id
    @Column(name = "ROWID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal rowid;

    @PrePersist
    public void prePersist() {
        if (xx10cGeoy == null) xx10cGeoy = "";
        if (xx10cGeox == null) xx10cGeox = "";
        if (xtmsfcy == null) xtmsfcy = 1;
        if (xupdusr == null) xupdusr = "user from RP";
        if (xupdate == null) xupdate = new Date();
        if (x1cgeoso == null) x1cgeoso = 1;
        if (xadd == null) xadd = "";
        if (xadddes == null) xadddes = "";
    }
}
