package com.transport.sync.syncDto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DocumentCfgDto {
    private Integer rowId;
    private Integer updTick;
    private Short xdocument;
    private String xdocTyp;
    private String xroutag;
    private String xroutagFra;
    private Long x10cServt;
    private String xstyZon;
    private LocalDateTime creDatTim;
    private LocalDateTime updDatTim;
    private UUID auuid;
    private String creUsr;
    private String updUsr;
}
