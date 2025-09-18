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
    private Short xdocument;

    @Column(name = "XDOCTYP_0")
    private String xdocTyp;

    @Column(name = "XROUTAG_0")
    private String xroutag;

    @Column(name = "XROUTAGFRA_0")
    private String xroutagFra;

    @Column(name = "X10C_SERVT_0")
    private Long x10cServt;

    @Column(name = "XSTYZON_0")
    private String xstyZon;

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

    @PrePersist
    public void prePersist() {
        if (creDatTim == null) {
            creDatTim = LocalDateTime.now();
        }
        updDatTim = LocalDateTime.now();

        if (auuid == null) {
            auuid = UUID.randomUUID();
        }
        if (creUsr == null) {
            creUsr = "";
        }
        if (updUsr == null) {
            updUsr = "";
        }
    }

    @PreUpdate
    public void preUpdate() {
        updDatTim = LocalDateTime.now();

        if (updUsr == null) {
            updUsr = "";
        }
    }
}
