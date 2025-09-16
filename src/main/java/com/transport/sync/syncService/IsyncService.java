package com.transport.sync.syncService;

import com.transport.sync.syncDto.SiteDto;
import org.springframework.http.ResponseEntity;

public interface IsyncService {
    ResponseEntity<Object> getAllXtmsSites();
    ResponseEntity<Object> getAllSyncData();
    ResponseEntity<Object> syncSitesFromERP();
    ResponseEntity<Object> updateSite(SiteDto request);
}
