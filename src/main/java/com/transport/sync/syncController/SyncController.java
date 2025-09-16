package com.transport.sync.syncController;

import com.transport.sync.syncService.IsyncService;
import com.transport.sync.syncService.SyncService;
import com.transport.sync.syncDto.SiteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sync")
@CrossOrigin(origins={"http://localhost:3000","http://localhost:8082","http://localhost:8081","http://192.168.1.211:8081","http://192.168.1.211:8082","http://solutions.tema-systems.com:8081","http://solutions.tema-systems.com:8082"})
public class SyncController {

    private final IsyncService siteService;

    @Autowired
    public SyncController(IsyncService siteService) {
        this.siteService = siteService;
    }

    @GetMapping("/allsites")
    public ResponseEntity<Object> getAllSites() {
        return siteService.getAllXtmsSites();
    }

    @GetMapping("/getAllSyncData")
    public ResponseEntity<Object> getSyncStatus() {
        return siteService.getAllSyncData();
    }

    @PostMapping("/site")
    public ResponseEntity<Object> syncSites() {
        return siteService.syncSitesFromERP();
    }

    @PutMapping("/updateSite")
    public ResponseEntity<?> updateSite(@RequestBody SiteDto request) {
        return siteService.updateSite(request);
    }
}
