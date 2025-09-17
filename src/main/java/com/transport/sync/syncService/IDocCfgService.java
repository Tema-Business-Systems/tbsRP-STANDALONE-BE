package com.transport.sync.syncService;

import com.transport.sync.syncDto.DocumentCfgDto;

import java.util.List;
import java.util.Map;

public interface IDocCfgService {
    List<DocumentCfgDto> getAllDoccfg();
    DocumentCfgDto getDocCfgById(Integer id);
    Map<String, Object> createDocCfg(DocumentCfgDto dto);
    Map<String, Object> updateDoccfg(Integer id, DocumentCfgDto dto);
    void deleteDocCfg(Integer id);
}
