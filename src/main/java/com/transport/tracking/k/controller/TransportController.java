package com.transport.tracking.k.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transport.tracking.k.service.ModifierService;
import com.transport.tracking.k.service.PanelService;
import com.transport.tracking.k.service.TransportService;
import com.transport.tracking.k.service.UserService;
import com.transport.tracking.model.LoadVehStock;
import com.transport.tracking.model.Vehicle;
import com.transport.tracking.model.Trip;
import com.transport.tracking.model.*;
import com.transport.tracking.response.*;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import com.transport.tracking.model.VehRoute;
import com.transport.tracking.model.VehRouteDetail;

@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:8082","http://localhost:8081","http://192.168.1.211:8081","http://192.168.1.211:8082","http://solutions.tema-systems.com:8081","http://solutions.tema-systems.com:8082"})
@RequestMapping ("/api/v1/transport")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @Autowired
    private ModifierService modifierService;

    @Autowired
    private PanelService panelService;

    @Autowired
    private UserService userService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping
    public String ping() {
        return "welcome to transport vehicles";
    }

    @GetMapping("/vehicle")
    public List<VehicleVO> getVehicles(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                       @RequestParam(name = "active", required = false) Boolean active){
        return transportService.listTransports(site, active);
    }




    @GetMapping("/vehicle/panel")
    public VehiclePanelVO getVehiclePanel(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                          @RequestParam(name = "active", required = false) Boolean active,
                                          @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                          @RequestParam(name = "cache", required = false) boolean cache){
        if(StringUtils.isEmpty(site)) {
            return new VehiclePanelVO();
        }
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(cache) {
            return panelService.updateVehiclePanel(site, date);
        }
        return panelService.getVehiclePanel(site, date);
    }

    // mapping for vrdetails to vrscreen   --- by Ashok
    @GetMapping("/vr")
    public VehRoute getVr(AccessTokenVO accessTokenVO, @RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return transportService.VehRouteByID(vrcode);
    }

    @GetMapping("/sitelist")
    public List<Vehicle> getallvehicles(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> sites,
                                    @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)
    {
        return transportService.getVehiclebySite(sites,date);
    }

    @GetMapping("/vrdetails")
    public List<VehRouteDetail> getVrdetails(AccessTokenVO accessTokenVO, @RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return transportService.listVehRouteDetails(vrcode);
    }
// end of vrscreen

    // mapping for vrdetails to vrscreen   --- by Ashok
    @GetMapping("/loadvehstk")
    public LoadVehStock getloadvehstock(AccessTokenVO accessTokenVO, @RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return transportService.LoadVehstockByVR(vrcode);
    }


    @GetMapping("/routecodes")
    public List<RouteCode> getRouteCodes(AccessTokenVO accessTokenVO){
        return transportService.getRouteCodes();
    }



    @PostMapping ("/validate")
    public @ResponseBody Map<String, String> SubmitVR(@RequestBody TripVO request) throws Exception {
      //  log.info("inside Validate Controller");
        return  transportService.ValidateTrips(request);
    }



    @PostMapping ("/groupvalidate")
    public @ResponseBody Map<String, String> SubmitVRs(@RequestBody List<TripVO> request) throws Exception {
        //  log.info("inside Validate Controller");
        return  transportService.ValidateListofTrips(request);
    }


    @PostMapping ("/nonvalidate")
    public @ResponseBody Map<String, String> SubmitValidatedVR(@RequestBody TripVO request) throws Exception {
        //  log.info("inside Validate Controller");
        return  transportService.NonValidateTrips(request);
    }



    @PostMapping ("/validateonly")
    public @ResponseBody Map<String, String> SubmitValidationonly(@RequestBody TripVO request) throws Exception {
        //  log.info("inside Validate Controller");
        return  transportService.ValidateTrips(request);
    }



    @PostMapping ("/trips")
    public @ResponseBody Map<String, String> submitResponse(@RequestBody List<TripVO> request) throws Exception {
        return transportService.saveTrip(request);
    }

    @PostMapping ("/lock/trips")
    public @ResponseBody Map<String, String> lockTrip(AccessTokenVO accessTokenVO, @RequestBody List<TripVO> request) throws JsonProcessingException {
        transportService.lockTrip(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }

    @PostMapping ("/unlock/multipletrips")
    public @ResponseBody Map<String, String> unlockTrips(AccessTokenVO accessTokenVO, @RequestBody List<TripVO> request) throws JsonProcessingException {
        transportService.unlockTrips(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }


    @PostMapping ("/lock/multipletrips")
    public @ResponseBody Map<String, String> lockTrips(AccessTokenVO accessTokenVO, @RequestBody List<TripVO> request) throws JsonProcessingException {
        transportService.lockTrips(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }
    @PostMapping ("/delete/trips")
    public @ResponseBody Map<String, String> deleteTrips(AccessTokenVO accessTokenVO, @RequestBody List<TripVO> request) throws JsonProcessingException {
        transportService.deleteTrips(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }


    @PostMapping("/update/jobid")
    public @ResponseBody Map<String, String> updateDeletedDoc(@RequestBody Map<String, String> requestData) {
        Map<String, String> response = new HashMap<>();

        try {
            String tripId = requestData.get("tripid");
            String jobId = requestData.get("jobid");

            if (tripId == null || jobId == null) {
                response.put("status", "error");
                response.put("message", "tripid and jobid are required");
                return response;
            }

            transportService.updateJobIDByTripID(tripId, jobId); // Call service method

            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }



    @PostMapping ("/update/deldoc")
    public @ResponseBody Map<String, String> UpdatedeletedDoc(AccessTokenVO accessTokenVO, @RequestBody List<DocsVO> request) throws JsonProcessingException {
        transportService.UpdatedeletedDocument(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }

    @GetMapping ("/tripswithRange")
    public @ResponseBody List<TripVO> getTripswithRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                                        @RequestParam(name = "sdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                        @RequestParam(name = "edate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                        @RequestParam(name = "active", required = false) Boolean active) throws JsonProcessingException {
        if(Objects.isNull(sdate)) {
            String dateFormate = format.format(new Date());
            try {
                sdate = format.parse(dateFormate);
            }catch (Exception e) {
            }
        }
        if(StringUtils.isEmpty(site)) {
            List<TripVO> tripList = new ArrayList<>();
            return new ArrayList<>();
        }
        //System.out.println("date == "+date);
        return transportService.getTripsWithRange(site, sdate, edate);
    }

    @PostMapping ("/delete/trip")
    public @ResponseBody Map<String, String> deleteTrip(AccessTokenVO accessTokenVO, @RequestBody List<TripVO> request) throws JsonProcessingException {
        transportService.deleteTrip(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }



    @GetMapping ("/trips")
    public @ResponseBody List<TripVO> getTrips(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                               @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                               @RequestParam(name = "active", required = false) Boolean active) throws JsonProcessingException {
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(StringUtils.isEmpty(site)) {
            List<TripVO> tripList = new ArrayList<>();
            return new ArrayList<>();
        }

        //System.out.println("date == "+date);



        return transportService.getTrips(site, date);
    }

    @GetMapping("/drops/panel")
    public DropsPanelVO getDropsPanel(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                          @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                          @RequestParam(name = "active", required = false) Boolean active){
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(StringUtils.isEmpty(site)) {
            return new DropsPanelVO();
        }

        //System.out.println("date == "+date);



        return panelService.getDropsPanel(site, date);
    }

    @GetMapping("/sites")
    public List<SiteVO> getSites(AccessTokenVO accessTokenVO){
        return transportService.getSites();
    }


    @GetMapping("/usrsites")
    public List<SiteVO> getusrSites(AccessTokenVO accessTokenVO, @RequestParam(name = "user", required = false) String user)
    {
        return transportService.getuserSites(user);
    }


    @GetMapping("/drop")
    public List<DropsVO> getDrops(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                  @RequestParam(name = "active", required = false) Boolean active){
        return transportService.listDrops(site);
    }

    @GetMapping("/pickup")
    public List<PickUPVO> getPickups(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                     @RequestParam(name = "active", required = false) Boolean active){
        return transportService.listPickups(site);
    }

    @GetMapping("/prevtrpsite")
    public Trip getPrevSiteVehicles(AccessTokenVO accessTokenVO, @RequestParam(name = "veh", required = false) String veh,
                                      @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        return transportService.getArrivalSiteforVehice(veh,date);
    }


    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles()
    {
        return transportService.listAllTransports();
    }

    @GetMapping("/operators")
    public List<Driver> getAllOperators()
    {
        return transportService.listAllOperators();
    }


    @GetMapping ("/tripslist")
    public @ResponseBody List<Trip_ReportVO> getTripList(AccessTokenVO accessTokenVO,
                                               @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                               @RequestParam(name = "active", required = false) Boolean active) throws JsonProcessingException {
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }
        }
        return transportService.getTripsList(date);
    }


    @GetMapping("/drops/panelwithRange")
    public DropsPanelVO getDropsPanelwithRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                               @RequestParam(name = "sdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                               @RequestParam(name = "edate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                               @RequestParam(name = "active", required = false) Boolean active){
        if(Objects.isNull(sdate)) {
            String dateFormate = format.format(new Date());
            try {
                sdate = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(Objects.isNull(edate)) {
            String dateFormate = format.format(new Date());
            try {
                edate = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }

        if(StringUtils.isEmpty(site)) {
            return new DropsPanelVO();
        }

        //System.out.println("date == "+date);


        return panelService.getDropsPanelwithRange(site, sdate,edate);
    }


      // submit for lvs allocation  into specific table
    @PostMapping ("/allocation")
    public @ResponseBody Map<String, String> SubmitforAllocation(@RequestBody List<LVSAllocationVO> request) throws Exception {
        //  log.info("inside Validate Controller");
        transportService.SubmitforAlocation(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }


    
    // get openDocuments for Deletion

    @GetMapping("/opendocsBySiteAndDateRange")
    public List<OpenDocsVO> getDocumentswithRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                               @RequestParam(name = "sdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                               @RequestParam(name = "edate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                               @RequestParam(name = "active", required = false) Boolean active){
        List<OpenDocsVO> docsVo = new ArrayList<>();
        if(Objects.isNull(sdate)) {
            String dateFormate = format.format(new Date());
            try {
                sdate = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(Objects.isNull(edate)) {
            String dateFormate = format.format(new Date());
            try {
                edate = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(StringUtils.isEmpty(site) || site.size() == 0 ) {
             return docsVo;
        }
        else {

        }

        //System.out.println("date == "+date);


        return modifierService.getOpenDocsWithRange(site, sdate,edate);
    }


    @PostMapping ("/OpenDocs/deleteDocs")
    public @ResponseBody Map<String, String> deleteOpenDocs(AccessTokenVO accessTokenVO, @RequestBody List<OpenDocsVO> request) throws JsonProcessingException {
        transportService.deleteOpenDocs(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }



    // get openDocuments for Deletion

    @GetMapping("/opentoadddocsBySiteAndDateRange")
    public List<OpenDocsRoutesVO> getToAddDocumentswithRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                                  @RequestParam(name = "sdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                  @RequestParam(name = "edate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                  @RequestParam(name = "active", required = false) Boolean active){
        List<OpenDocsRoutesVO> docsVo = new ArrayList<>();
        if(Objects.isNull(sdate)) {
            String dateFormate = format.format(new Date());
            try {
                sdate = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(Objects.isNull(edate)) {
            String dateFormate = format.format(new Date());
            try {
                edate = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(StringUtils.isEmpty(site) || site.size() == 0 ) {
            return docsVo;
        }
        else {

        }

        //System.out.println("date == "+date);


        return modifierService.getTripsVOwithRangeofOpenDocs(site, sdate,edate);
    }


    @GetMapping("/tripDetails/vr")
    public TripVO getTripdetailsByTripCode(AccessTokenVO accessTokenVO, @RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return transportService.getTripDetailsByVRCode(vrcode);
    }

    @PostMapping ("/openDocs/addDocs")
    public @ResponseBody Map<String, String> addDocstoRouteByOpenDocs(AccessTokenVO accessTokenVO, @RequestBody List<OpenDocsRoutesVO> request) throws JsonProcessingException {
        transportService.addDocstoRoutes(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }

    @PostMapping ("/createuser")
    public ResponseEntity submitResponse2(@RequestBody User user) throws Exception {
        Map<String, String> map = new HashMap<>();
        if(userService.checkUserExists(user.getXlogin())){
            map.put("Error", "User already exists");
            return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(map);
        }
        userService.createUserWithAlignedSites2(user);
        map.put("success", "success");
        return ResponseEntity.status(HttpStatus.SC_OK).body(map);
    }





}
