package com.transport.sync.syncService;

import com.transport.sync.syncDto.DocumentCfgDto;
import com.transport.sync.syncMapper.DocumentCfgMapper;
import com.transport.sync.syncModel.DocumentCfg;
import com.transport.sync.syncRepo.DocumentCfgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentCfgService implements IDocCfgService{

    private final DocumentCfgRepository repository;

    @Override
    public List<DocumentCfgDto> getAllDoccfg() {
        return repository.findAll()
                .stream()
                .map(DocumentCfgMapper :: toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> createDocCfg(DocumentCfgDto dto) {
        String xDocTyp = dto.getXdocTyp();
        Short xDocument = dto.getXdocument();

        Optional<DocumentCfg> existing = repository.findByDocTypAndDocument(xDocTyp, xDocument);
        if (existing.isPresent()) {
            throw new RuntimeException(
                    "Record already exists with this doc type: " + xDocTyp + " and STD Doc: " + xDocument
            );
        }

        DocumentCfg entity = DocumentCfgMapper.toEntity(dto);
        DocumentCfg saved = repository.save(entity);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Record created successfully");
        response.put("data", DocumentCfgMapper.toDto(saved));

        return response;
    }

    @Override
    public DocumentCfgDto getDocCfgById(Integer id) {
        return repository.findById(id)
            .map(DocumentCfgMapper::toDto)
            .orElse(null);
    }

    @Override
    public Map<String, Object> updateDoccfg(Integer id, DocumentCfgDto dto) {
        DocumentCfg existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("DocumentCfg not found with ID: " + id));

        String xDocTyp = dto.getXdocTyp();
        Short xDocument = dto.getXdocument();
        Optional<DocumentCfg> duplicate = repository.findByDocTypAndDocument(xDocTyp, xDocument);
        if (duplicate.isPresent() && !duplicate.get().getRowId().equals(id)) {
            throw new RuntimeException(
                    "Record already exists with this doc type: " + xDocTyp + " and STD Doc: " + xDocument
            );
        }
        existing.setXdocTyp(dto.getXdocTyp());
        existing.setXdocument(dto.getXdocument());
        existing.setXroutag(dto.getXroutag());
        existing.setXroutagFra(dto.getXroutagFra());
        existing.setXstyZon(dto.getXstyZon());
        existing.setX10cServt(dto.getX10cServt());
        existing.setUpdTick(dto.getUpdTick());
        existing.setUpdUsr(dto.getUpdUsr());
        existing.setUpdDatTim(LocalDateTime.now());

        DocumentCfg saved = repository.save(existing);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Record updated successfully");
        response.put("data", DocumentCfgMapper.toDto(saved));
        return response;
    }

    @Override
    public void deleteDocCfg(Integer id) {
        Optional<DocumentCfg> existing = repository.findById(id);
        if (!existing.isPresent()) {
            throw new RuntimeException("Document with ID " + id + " not found");
        }
        repository.deleteById(id);
    }
}
