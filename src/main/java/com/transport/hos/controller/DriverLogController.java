package com.transport.hos.controller;

import com.transport.hos.response.DriverLogVO;
import com.transport.hos.service.DriverLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class DriverLogController {

    @Autowired
    private DriverLogService driverLogService;

    @PostMapping
    public DriverLogVO addLog(@RequestBody DriverLogVO vo) {
        return driverLogService.saveLog(vo);
    }

    @GetMapping("/{id}")
    public DriverLogVO getLog(@PathVariable int id) {
        return driverLogService.getLogById(id);
    }

    @GetMapping
    public List<DriverLogVO> getAllLogs() {
        return driverLogService.getAllLogs();
    }

    @DeleteMapping("/{id}")
    public void deleteLog(@PathVariable int id) {
        driverLogService.deleteLog(id);
    }

    @PutMapping("/{id}")
    public DriverLogVO updateLog(@PathVariable int id, @RequestBody DriverLogVO vo) {
        return driverLogService.updateLog(id, vo);
    }

}

