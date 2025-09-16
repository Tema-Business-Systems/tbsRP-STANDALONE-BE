package com.transport.sync.syncService;

import com.transport.sync.syncMapper.SyncMapper;
import com.transport.tracking.model.Facility;
import com.transport.sync.syncModel.XtmsSite;
import com.transport.tracking.repository.FacilityRepository;
import com.transport.sync.syncRepo.XtmsSiteRepository;
import com.transport.sync.syncDto.SiteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SyncService implements IsyncService{
    private final FacilityRepository facilityRepo;
    private final XtmsSiteRepository xtmsSiteRepository;

    public SyncService(FacilityRepository facilityRepo, XtmsSiteRepository xtmsSiteRepository) {
        this.facilityRepo = facilityRepo;
        this.xtmsSiteRepository = xtmsSiteRepository;
    }

    @Override
    public ResponseEntity<Object> getAllXtmsSites() {
        List<XtmsSite> sites = (List<XtmsSite>) xtmsSiteRepository.findAll();
        List<SiteDto> dtos = sites.stream()
                .map(SyncMapper::toDto)
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sites fetched successfully");
        response.put("data", dtos);
        return ResponseEntity.ok(response);
    }

    @Override
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

    @Override
    public ResponseEntity<Object> syncSitesFromERP() {
        List<Facility> erpSites = (List<Facility>) facilityRepo.findAll();
        List<XtmsSite> sitesToSave = new ArrayList<>();
        for(Facility facility: erpSites) {
            XtmsSite site = xtmsSiteRepository.findBySiteId(facility.getFcy());
            if(site == null) {
                site=new XtmsSite();
            }
            if("Manual".equalsIgnoreCase(site.getLocategeoby())) {
                continue;
            }
            SyncMapper.updateEntityFromFacility(facility, site);
            sitesToSave.add(site);
        }
        xtmsSiteRepository.saveAll(sitesToSave);
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

    @Override
    public ResponseEntity<Object> updateSite(SiteDto request) {
        XtmsSite site = xtmsSiteRepository.findBySiteId(request.getSiteId());
        if(site==null) {
            return ResponseEntity.badRequest().body("Site with ID " + request.getSiteId() + " not found");
        }

        SyncMapper.updateEntityFromDto(request, site);
        site.setXupdate(new java.util.Date());
        xtmsSiteRepository.save(site);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Site updated successfully");
        response.put("data",SyncMapper.toDto(site));
        return ResponseEntity.ok(response);
    }
}
