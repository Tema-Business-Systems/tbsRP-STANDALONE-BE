package com.transport.fleet.controller;

import com.transport.fleet.model.FleetDriver;
import com.transport.fleet.model.FleetVehicle;
import com.transport.fleet.model.Trailer;
import com.transport.fleet.model.VehClass;
import com.transport.fleet.response.FleetDriverVO;
import com.transport.fleet.response.FleetVehicleVO;
import com.transport.fleet.response.TrailerVO;
import com.transport.fleet.response.VehicleClassVO;
import com.transport.fleet.service.FleetService;
import com.transport.tracking.k.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:8082","http://localhost:8081","http://192.168.1.211:8081","http://192.168.1.211:8082","http://solutions.tema-systems.com:8081","http://solutions.tema-systems.com:8082"})
@RequestMapping("/api/v1/fleet")
@SuppressWarnings("unused")
public class FleetController {

    private final FleetService service;

    public FleetController(FleetService service) {
        this.service = service;
    }

    //Vehicle related apis --start
    @PostMapping("/createVehicle")
    public ResponseEntity<Object> createVehicle(@RequestBody FleetVehicleVO vehicle) throws NoSuchFieldException, IllegalAccessException {
        if(vehicle !=null){
            if(!service.checkVehicleExists(vehicle.getCode())) {
                service.createVehicle(vehicle);
                return ResponseEntity.status(201).body(new HashMap<String, Object>() {{
                    put("message", "Vehicle created successfully");
                }});
            }else{
                throw new ApplicationException(400,"vehicle already exists with code:"+vehicle.getCode());
            }
        }
        throw new ApplicationException(400,"error while creating vehicle");
    }

    @GetMapping("/getAllVehicles")
    public ResponseEntity<Object> getAllVehicles() throws IllegalAccessException {
        List<FleetVehicleVO> vehicles = service.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/getVehicleByCodeyve")
    public ResponseEntity<Object> getVehicleByCodeyve(@RequestParam String codeyve) throws IllegalAccessException {
        FleetVehicleVO vehicle = service.getVehicleByCodeyve(codeyve);
        if(vehicle!=null) {
            return ResponseEntity.ok(vehicle);
        }else{
            throw new ApplicationException(400,"No vehicle found with codeyve: "+codeyve);
        }
    }

    @PutMapping("/updateVehicle")
    public ResponseEntity<Object> updateVehicle(@RequestBody FleetVehicleVO vehicle) throws NoSuchFieldException, IllegalAccessException {
        if(vehicle!=null){
            if(service.checkVehicleExists(vehicle.getCode())) {
                service.updateVehicle(vehicle);
                return ResponseEntity.status(200).body(new HashMap<String, Object>() {{
                    put("message", "vehicle updated successfully");
                    put("vehicle", vehicle);
                }});
            }else{
                throw new ApplicationException(400,"vehicle doesn't exist with code: "+vehicle.getCode());
            }
        }
        throw new ApplicationException(400,"error while updating vehicle");
    }

    @DeleteMapping("/deleteVehicleByCodeyve")
    public ResponseEntity<Object> deleteVehicle(@RequestParam String codeyve){
        if(!service.checkVehicleExists(codeyve)){
            throw new ApplicationException(400,"Vehicle doesn't exist with code :"+codeyve);
        }
        String result = service.deleteVehicleByCodeyve(codeyve);
        if(result.equalsIgnoreCase("success"))
            return ResponseEntity.status(200).body("vehicle deleted successfully");
        else
            throw new ApplicationException(400,"error occurred while deleting the vehicle");
    }
    //Vehicle related apis --end

    //Trailer related apis --start
    @PostMapping("/createTrailer")
    public ResponseEntity<Object> createTrailer(@RequestBody TrailerVO trailer) throws NoSuchFieldException, IllegalAccessException {
        if(trailer !=null){
            if(!service.checkTrailerExists(trailer.getTrailer())) {
                Trailer createdTrailer = service.createTrailer(trailer);
                return ResponseEntity.status(201).body(new HashMap<String, Object>() {{
                    put("message", "Trailer created successfully");
                    put("vehicle", createdTrailer);
                }});
            }else{
                throw new ApplicationException(400,"trailer already exists with code:"+trailer.getTrailer());
            }
        }
        throw new ApplicationException(400,"error while creating trailer");
    }

    @GetMapping("/getAllTrailers")
    public ResponseEntity<Object> getAllTrailers() throws IllegalAccessException {
        List<TrailerVO> trailers = service.getAllTrailers();
        return ResponseEntity.ok(trailers);
    }

    @GetMapping("/getTrailerByTrailerCode")
    public ResponseEntity<Object> getTrailerByTrailerCode(@RequestParam String trailer) throws IllegalAccessException {
        TrailerVO trailerObj = service.getTrailerByTrailerCode(trailer);
        if(trailerObj!=null) {
            return ResponseEntity.ok(trailerObj);
        }else{
            throw new ApplicationException(400,"No trailer found with code: "+trailer);
        }
    }

    @PutMapping("/updateTrailer")
    public ResponseEntity<Object> updateTrailer(@RequestBody TrailerVO trailerVO) throws NoSuchFieldException, IllegalAccessException {
        if(trailerVO!=null){
            if(service.checkTrailerExists(trailerVO.getTrailer())) {
                service.updateTrailer(trailerVO);
                return ResponseEntity.status(200).body(new HashMap<String, Object>() {{
                    put("message", "trailer updated successfully");
                    put("vehicle", trailerVO);
                }});
            }else{
                throw new ApplicationException(400,"trailer doesn't exist with code :"+trailerVO.getTrailer());
            }
        }
        throw new ApplicationException(400,"error while updating trailer");
    }

    @DeleteMapping("/deleteTrailerByTrailerCode")
    public ResponseEntity<Object> deleteTrailer(@RequestParam String trailer){
        if(!service.checkTrailerExists(trailer)){
            throw new ApplicationException(400,"Trailer doesn't exist with name :"+trailer);
        }
        String result = service.deleteTrailerByTrailerCode(trailer);
        if(result.equalsIgnoreCase("success"))
            return ResponseEntity.status(200).body("trailer deleted successfully");
        else
            throw new ApplicationException(400,"error occurred while deleting the trailer");

    }
    //Trailer related apis --end

    //Vehicle Class apis --start
    @PostMapping("/createVehicleClass")
    public ResponseEntity<Object> createVehicleClass(@RequestBody VehicleClassVO vehicleClass){
        if(vehicleClass !=null && !vehicleClass.getClassName().isEmpty()){
            if(!service.checkVehicleClassExists(vehicleClass.getClassName())) {
                VehClass createdVehClass = service.createVehicleClass(vehicleClass);
                return ResponseEntity.status(201).body(new HashMap<String, Object>() {{
                    put("message", "vehicleClass created successfully");
                    put("vehicle class", createdVehClass);
                }});
            }else{
                throw new ApplicationException(400,"vehicleClass already exists with class name:"+vehicleClass.getClassName());
            }
        }
        throw new ApplicationException(400,"error while creating vehicleClass");
    }

    @GetMapping("/getAllVehicleClassList")
    public ResponseEntity<Object> getAllVehicleClassList(){
        List<VehicleClassVO> vehicleClassList = service.getAllVehicleClassList();
        return ResponseEntity.ok(vehicleClassList);
    }

    @GetMapping("/getVehicleClassByClass")
    public ResponseEntity<Object> getVehicleClassByClass(@RequestParam String className){
        VehicleClassVO vehicleClass = service.getVehicleClassByClass(className);
        if(vehicleClass!=null) {
            return ResponseEntity.ok(vehicleClass);
        }else{
            throw new ApplicationException(400,"No vehicle class found with name: "+className);
        }
    }

    @PutMapping("/updateVehicleClass")
    public ResponseEntity<Object> updateVehicleClass(@RequestBody VehicleClassVO vehicleClassVO){
        if(vehicleClassVO!=null){
            if(service.checkVehicleClassExists(vehicleClassVO.getClassName())) {
                service.updateVehicleClass(vehicleClassVO);
                return ResponseEntity.status(200).body(new HashMap<String, Object>() {{
                    put("message", "vehicleClass updated successfully");
                    put("vehicleClass", vehicleClassVO);
                }});
            }
            else{
                throw new ApplicationException(400,"vehicleClass doesn't exist with class name :"+vehicleClassVO.getClassName());
            }
        }
        throw new ApplicationException(400,"error while updating vehicleClass");
    }

    @DeleteMapping("/deleteVehicleClassByClass")
    public ResponseEntity<Object> deleteVehicleClassByClass(@RequestParam String className){
        if(!service.checkVehicleClassExists(className)){
            throw new ApplicationException(400,"vehicle class doesn't exist with class name :"+className);
        }
        String result = service.deleteVehicleClassByClass(className);
        if(result.equalsIgnoreCase("success"))
            return ResponseEntity.status(200).body("vehicleClass deleted successfully");
        else
            throw new ApplicationException(400,"error occurred while deleting vehicleClass");
    }
    //Vehicle Class apis --end

    //Driver related apis --start
    @PostMapping("/createDriver")
    public ResponseEntity<Object> createDriver(@RequestBody FleetDriverVO driverVO) throws NoSuchFieldException, IllegalAccessException {
        if(driverVO !=null){
            if(!service.checkDriverExists(driverVO.getDriverId())) {
                FleetDriver createdDriver = service.createDriver(driverVO);
                return ResponseEntity.status(201).body(new HashMap<String, Object>() {{
                    put("message", "driver created successfully");
                    put("driver", createdDriver);
                }});
            }else{
                throw new ApplicationException(400,"driver already exists with id:"+driverVO.getDriverId());
            }
        }
        throw new ApplicationException(400,"error while creating driver");
    }

    @GetMapping("/getAllDriversList")
    public ResponseEntity<Object> getAllDriversList() throws IllegalAccessException {
        List<FleetDriverVO> driversList = service.getAllDriversList();
        return ResponseEntity.ok(driversList);
    }

    @GetMapping("/getDriverById")
    public ResponseEntity<Object> getDriverById(@RequestParam String driverId) throws IllegalAccessException {
        FleetDriverVO driver = service.getDriverById(driverId);
        if(driver!=null) {
            return ResponseEntity.ok(driver);
        }else{
            throw new ApplicationException(400,"No driver found with id: "+driverId);
        }
    }

    @PutMapping("/updateDriver")
    public ResponseEntity<Object> updateDriver(@RequestBody FleetDriverVO driverVO) throws NoSuchFieldException, IllegalAccessException {
        if(driverVO!=null){
            if(service.checkDriverExists(driverVO.getDriverId())) {
                service.updateDriver(driverVO);
                return ResponseEntity.status(200).body(new HashMap<String, Object>() {{
                    put("message", "driver updated successfully");
                    put("driver", driverVO);
                }});
            }
            else{
                throw new ApplicationException(400,"driver doesn't exist with the id: "+driverVO.getDriverId());
            }
        }
        throw new ApplicationException(400,"error while updating driver");
    }

    @DeleteMapping("/deleteDriverById")
    public ResponseEntity<Object> deleteDriverById(@RequestParam String driverId){
        if(!service.checkDriverExists(driverId)){
            throw new ApplicationException(400,"driver doesn't exist with id :"+driverId);
        }
        String result = service.deleteDriverById(driverId);
        if(result.equalsIgnoreCase("success"))
            return ResponseEntity.status(200).body("driver deleted successfully");
        else
            throw new ApplicationException(400,"error occurred while deleting driver");
    }
    //Driver related apis --end

    @GetMapping("/getCommonData")
    public ResponseEntity<Object> getCommonData(){
        return ResponseEntity.ok(service.getCommonData());
    }

    @GetMapping("/getVehicleCommonData")
    public ResponseEntity<Object> getVehicleCommonData(){
        return ResponseEntity.ok(service.getVehicleCommonData());
    }

    @GetMapping("/getVehicleClassCommonData")
    public ResponseEntity<Object> getVehicleClassCommonData(){
        return ResponseEntity.ok(service.getVehicleClassCommonData());
    }

    @GetMapping("/getTrailerCommonData")
    public ResponseEntity<Object> getTrailerCommonData(){
        return ResponseEntity.ok(service.getTrailerCommonData());
    }

    @GetMapping("/getDriverCommonData")
    public ResponseEntity<Object> getDriverCommonData(){
        return ResponseEntity.ok(service.getDriverCommonData());
    }


    @GetMapping("/getPostalData")
    public ResponseEntity<Object> getPostalData(@RequestParam String country){
        return ResponseEntity.ok(service.getPostalData(country));
    }

    @GetMapping("/greeting")
    public ResponseEntity<Object> greeting() {
        return ResponseEntity.ok("Bye World");
    }

}