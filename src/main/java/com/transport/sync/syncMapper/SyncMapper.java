package com.transport.sync.syncMapper;

import com.transport.sync.syncDto.SiteDto;
import com.transport.sync.syncModel.XtmsSite;
import com.transport.tracking.model.Facility;

public class SyncMapper {
    private SyncMapper() {

    }

    public static SiteDto toDto(XtmsSite site) {
        if (site == null) return null;

        SiteDto dto = new SiteDto();
        dto.setSiteId(site.getSiteId());
        dto.setSiteName(site.getSiteName());
        dto.setFcysho(site.getFcysho());
        dto.setCry(site.getCry());
        dto.setBpaadd(site.getBpaadd());
        dto.setUpdusr(site.getUpdusr());
        dto.setUpddat(site.getUpddat());
        dto.setCredattim(site.getCredattim());
        dto.setUpddattim(site.getUpddattim());
        dto.setXx10cGeoy(site.getXx10cGeoy());
        dto.setXx10cGeox(site.getXx10cGeox());
        dto.setXtmsfcy(site.getXtmsfcy());
        dto.setXupdusr(site.getXupdusr());
        dto.setXupdate(site.getXupdate());
        dto.setX1cgeoso(site.getX1cgeoso());
        dto.setXadd(site.getXadd());
        dto.setXadddes(site.getXadddes());
        dto.setLocategeoby(site.getLocategeoby());
        dto.setRowid(site.getRowid());
        return dto;
    }

    public static void updateEntityFromDto(SiteDto dto, XtmsSite site) {
        if (dto == null || site == null) return;

        site.setLocategeoby(dto.getLocategeoby());
        site.setXupdusr(dto.getXupdusr());
        site.setXupdate(dto.getXupdate());
        site.setXx10cGeox(dto.getXx10cGeox());
        site.setXx10cGeoy(dto.getXx10cGeoy());
        site.setXtmsfcy(dto.getXtmsfcy());
    }

    public static void updateEntityFromFacility(Facility facility, XtmsSite site) {
        if(facility == null || site == null) return;

        site.setSiteId(facility.getFcy());
        site.setSiteName(facility.getFcynam());
        site.setFcysho(facility.getFcysho());
        site.setCry(facility.getCry());
        site.setBpaadd(facility.getBpaadd());
        site.setUpdusr(facility.getUpdusr());
        site.setUpddat(facility.getUpddat());
        site.setCredattim(facility.getCredattim());
        site.setUpddattim(facility.getUpddattim());
        site.setXx10cGeox(facility.getXx10c_geox());
        site.setXx10cGeoy(facility.getXx10c_geoy());
        site.setXtmsfcy(facility.getFcyNumber());
        site.setXupdusr(facility.getUpdusr());
        site.setXupdate(facility.getUpddat());
        site.setX1cgeoso(facility.getX1cgeoso());
        site.setXadd(facility.getXadd());
        site.setXadddes(facility.getXadddes());
        site.setLocategeoby("Auto");
    }

}
