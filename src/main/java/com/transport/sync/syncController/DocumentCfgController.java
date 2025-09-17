package com.transport.sync.syncController;

import com.transport.sync.syncDto.DocumentCfgDto;
import com.transport.sync.syncService.IDocCfgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/docCfg")
@RequiredArgsConstructor
public class DocumentCfgController {

    private final IDocCfgService service;

    @GetMapping("/allDocCfg")
    public ResponseEntity<List<DocumentCfgDto>> getAllCfgDocs () {
        return ResponseEntity.ok(service.getAllDoccfg());
    }

    @GetMapping("/DocsCfgByid/{id}")
    public ResponseEntity<DocumentCfgDto> getById(@PathVariable Integer id) {
        DocumentCfgDto dto = service.getDocCfgById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/createDocsCfg")
    public ResponseEntity<Map<String, Object>> createCfgDocs(@RequestBody DocumentCfgDto dto) {
        Map<String, Object> response = service.createDocCfg(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateDocsCfg/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody DocumentCfgDto dto) {
        Map<String, Object> updated = service.updateDoccfg(id, dto);
        return ResponseEntity.ok(service.updateDoccfg(id, dto));
    }

    @DeleteMapping("/deleteDocsCfg/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        service.deleteDocCfg(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Document deleted successfully");
        response.put("deletedId", id);
        return ResponseEntity.ok(response);
    }

}
