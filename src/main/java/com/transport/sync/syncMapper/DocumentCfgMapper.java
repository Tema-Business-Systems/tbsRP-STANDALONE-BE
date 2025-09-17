package com.transport.sync.syncMapper;

import com.transport.sync.syncDto.DocumentCfgDto;
import com.transport.sync.syncModel.DocumentCfg;

public class DocumentCfgMapper {
    private DocumentCfgMapper() {}

    public static DocumentCfgDto toDto(DocumentCfg entity) {
        if (entity == null) return null;

        DocumentCfgDto dto = new DocumentCfgDto();
        dto.setRowId(entity.getRowId());
        dto.setUpdTick(entity.getUpdTick());
        dto.setXDocument(entity.getXDocument());
        dto.setXDocTyp(entity.getXDocTyp());
        dto.setXRoutag(entity.getXRoutag());
        dto.setXRoutagFra(entity.getXRoutagFra());
        dto.setX10cServt(entity.getX10cServt());
        dto.setXStyZon(entity.getXStyZon());
        dto.setCreDatTim(entity.getCreDatTim());
        dto.setUpdDatTim(entity.getUpdDatTim());
        dto.setAuuid(entity.getAuuid());
        dto.setCreUsr(entity.getCreUsr());
        dto.setUpdUsr(entity.getUpdUsr());
        return dto;
    }

    public static DocumentCfg toEntity(DocumentCfgDto dto) {
        if (dto == null) return null;

        DocumentCfg entity = new DocumentCfg();
        entity.setUpdTick(dto.getUpdTick());

        entity.setXDocument(dto.getXDocument());
        entity.setXDocTyp(dto.getXDocTyp());
        entity.setXRoutag(dto.getXRoutag());
        entity.setXRoutagFra(dto.getXRoutagFra());
        entity.setX10cServt(dto.getX10cServt());
        entity.setXStyZon(dto.getXStyZon());
        entity.setCreDatTim(dto.getCreDatTim());
        entity.setUpdDatTim(dto.getUpdDatTim());
        entity.setAuuid(dto.getAuuid());
        entity.setCreUsr(dto.getCreUsr());
        entity.setUpdUsr(dto.getUpdUsr());
        return entity;
    }
}
