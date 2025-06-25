package com.transport.hos.controller;

import com.transport.hos.response.DriverBreakVO;
import com.transport.hos.service.DriverBreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/breaks")
public class DriverBreakController {

    @Autowired
    private DriverBreakService driverBreakService;

    @PostMapping
    public DriverBreakVO addBreak(@RequestBody DriverBreakVO vo) {
        return driverBreakService.saveBreak(vo);
    }

    @GetMapping("/{id}")
    public DriverBreakVO getBreak(@PathVariable int id) {
        return driverBreakService.getBreakById(id);
    }

    @GetMapping
    public List<DriverBreakVO> getAllBreaks() {
        return driverBreakService.getAllBreaks();
    }

    @DeleteMapping("/{id}")
    public void deleteBreak(@PathVariable int id) {
        driverBreakService.deleteBreak(id);
    }

    @PutMapping("/{id}")
    public DriverBreakVO updateBreak(@PathVariable int id, @RequestBody DriverBreakVO vo) {
        return driverBreakService.updateBreak(id, vo);
    }
}

