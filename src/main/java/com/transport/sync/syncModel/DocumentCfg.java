package com.transport.sync.syncModel;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="XX10CDOCT")
@Data
public class DocumentCfg {
    @Id
    @Column(name = "ROWID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rowId;

    @Column(name = "UPDTICK_0")
    private Integer updTick;

    @Column(name = "XDOCUMENT_0")
    private Short xDocument;

    @Column(name = "XDOCTYP_0")
    private String xDocTyp;

    @Column(name = "XROUTAG_0")
    private String xRoutag;

    @Column(name = "XROUTAGFRA_0")
    private String xRoutagFra;

    @Column(name = "X10C_SERVT_0")
    private Long x10cServt;

    @Column(name = "XSTYZON_0")
    private String xStyZon;

    @Column(name = "CREDATTIM_0")
    private LocalDateTime creDatTim;

    @Column(name = "UPDDATTIM_0")
    private LocalDateTime updDatTim;

    @Column(name = "AUUID_0", columnDefinition = "BINARY(16)")
    private UUID auuid;

    @Column(name = "CREUSR_0")
    private String creUsr;

    @Column(name = "UPDUSR_0")
    private String updUsr;
}
