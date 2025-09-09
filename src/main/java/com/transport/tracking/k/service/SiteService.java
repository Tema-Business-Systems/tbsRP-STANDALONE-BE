package com.transport.tracking.k.service;

import com.transport.tracking.model.Facility;
import com.transport.tracking.model.XtmsSite;
import com.transport.tracking.repository.FacilityRepository;
import com.transport.tracking.repository.XtmsSiteRepository;
import com.transport.tracking.response.SiteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SiteService {
    private final FacilityRepository facilityRepo;
    private final XtmsSiteRepository xtmsSiteRepository;

    public SiteService(FacilityRepository facilityRepo, XtmsSiteRepository xtmsSiteRepository) {
        this.facilityRepo = facilityRepo;
        this.xtmsSiteRepository = xtmsSiteRepository;
    }
    public ResponseEntity<Object> getAllXtmsSites() {
        List<XtmsSite> sites = (List<XtmsSite>) xtmsSiteRepository.findAll();
        List<SiteDto> dtos = sites.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sites fetched successfully");
        response.put("data", dtos);
        return ResponseEntity.ok(response);
    }
    public ResponseEntity<Object> getAllSyncData() {
        Map<String, Object> data = new HashMap<>();
        long erpCount = facilityRepo.count();
        long tmsCount = xtmsSiteRepository.count();

        //site count
        Map<String, Object> siteCount = new HashMap<>();
        siteCount.put("erp", erpCount);
        siteCount.put("tms", tmsCount);
        data.put("site",siteCount);

        // product count
        Map<String, Object> productCount = new HashMap<>();
        productCount.put("erp", 220);
        productCount.put("tms", 95);
        data.put("product", productCount);

        // category count
        Map<String, Object> categoryCount = new HashMap<>();
        categoryCount.put("erp", 321);
        categoryCount.put("tms", 210);
        data.put("category", categoryCount);

        // customer count
        Map<String, Object> customerCount = new HashMap<>();
        customerCount.put("erp", 50);
        customerCount.put("tms", 49);
        data.put("customer", customerCount);

        // customer address count
        Map<String, Object> customerAddressCount = new HashMap<>();
        customerAddressCount.put("erp", 120);
        customerAddressCount.put("tms", 115);
        data.put("customerAddress", customerAddressCount);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sync status fetched successfully");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Object> syncSitesFromERP() {
        List<Facility> erpSites = (List<Facility>) facilityRepo.findAll();
        for(Facility facility: erpSites) {
            XtmsSite site = xtmsSiteRepository.findBySiteId(facility.getFcy());
            if(site == null) {
                site=new XtmsSite();
            }
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
            xtmsSiteRepository.save(site);
        }
        long erpCount = facilityRepo.count();
        long tmsCount = xtmsSiteRepository.count();

        Map<String, Object> counts = new HashMap<>();
        counts.put("erpSites", erpCount);
        counts.put("tmsSites", tmsCount);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sites synced successfully");
        response.put("data", counts);
        return ResponseEntity.ok(response);
    }

    private SiteDto convertToDto(XtmsSite site) {
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
        dto.setRowid(site.getRowid());
        return dto;
    }
}
