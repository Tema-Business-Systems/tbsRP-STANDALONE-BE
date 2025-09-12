package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class SiteDto {
    private String siteId;
    private String siteName;
    private String fcysho;
    private String cry;
    private String bpaadd;
    private String updusr;
    private Date upddat;
    private Date credattim;
    private Date upddattim;
    private String xx10cGeoy;
    private String xx10cGeox;
    private int xtmsfcy;
    private String xupdusr;
    private Date xupdate;
    private Short x1cgeoso;
    private String xadd;
    private String xadddes;
    private String locategeoby;
    private BigDecimal rowid;
}
