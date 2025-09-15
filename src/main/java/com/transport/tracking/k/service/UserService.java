package com.transport.tracking.k.service;

import com.sun.istack.NotNull;
import com.transport.tracking.dto.UserDTO;
import com.transport.tracking.model.User;
import com.transport.tracking.repository.UserRepository;
import com.transport.tracking.response.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Value("${transport.path}")
    private String contextPath;

    @Autowired
    private EntityManager entityManager;

    @Value("${db.schema}")
    private String dbSchema;

    public UserVO login(@NotNull UserVO userVO, HttpServletResponse response) {
        User user = this.userRepository.findByXloginAndXpswd(userVO.getXlogin(), userVO.getXpswd());
//        User user = this.userRepository.findByXloginAndXpswdAndXact(userVO.getXlogin(), userVO.getXpswd(),2);
        if(Objects.isNull(user)) {
            throw new RuntimeException("User not found or password is wrong");
        }
        if(user.getXact()!=2) {
            throw new RuntimeException("User is not active");
        }
        userVO = new UserVO();
        userVO.setXusrname(user.getXusrname());
        userVO.setXlogin(user.getXlogin());
        userVO.setRemovePicktcktflg(user.getRouteplannerflg());
        userVO.setSchedulerflg(user.getSchedulerflg());
        userVO.setCalendarrpflg(user.getCalendarrpflg());
        userVO.setMapviewrpflg(user.getMapviewrpflg());
        userVO.setScreportsflg(user.getScreportsflg());
        userVO.setFleetmgmtflg(user.getFleetmgmtflg());
        userVO.setUsermgmtflg(user.getUsermgmtflg());
        userVO.setRemovePicktcktflg(user.getRemovePicktcktflg());
        userVO.setAddPicktcktflg(user.getAddPicktcktflg());
        userVO.setXact(user.getXact());
        //Set<Role> roles = user.getRoles();

        List<String> permissions = new ArrayList();
        permissions.add("Admin");

        //System.out.println(user.getRoles());

        userVO.setAccessToken(this.tokenService.generateAccessToken(permissions, user.getXusrname()));
      //  userVO.setSite(user.getSite());

        try {
            response.addCookie(this.generateCookie("token", userVO.getAccessToken()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return userVO;
    }

    private Cookie generateCookie(final String key, final String value) {
        final Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
        cookie.setPath(contextPath);
        return cookie;
    }

    public void logOut(HttpServletResponse response) {
        final Cookie cookie = new Cookie("token", "value");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath(contextPath);
        try {
            response.addCookie(cookie);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<UserDTO> listUsers() {

        List<User> users = userRepository.findAll(); // Fetch all users
        return users.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setXlogin(user.getXlogin());
                    dto.setXusrname(user.getXusrname());
                    dto.setXact(user.getXact());
                    dto.setEmail(user.getEmail());
                    dto.setLngmain(user.getLngmain());
                    dto.setLansec(user.getLansec());
                    dto.setRole(1);

                    // Set other fields as needed
                    return dto;
                })
                .collect(Collectors.toList());

    }


    // Create user with aligned sites
    public User createUserWithAlignedSites(User user) {
        user.setCredattim(new Date());
        user.setUpddattim(new Date());
        user.getAlignedSites().forEach(site -> site.setUser(user) );

        return userRepository.save(user);
    }




    // Get user with their aligned sites
    public User getUserWithAlignedSites(String xlogin) {

        return userRepository.findByXlogin(xlogin)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Map<String, String> createUserWithAlignedSites2(User user) {
        user.setCredattim(new Date());
        user.setUpddattim(new Date());
        user.getAlignedSites().forEach(site -> site.setUser(user) );

        userRepository.save(user);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }

    // Update user and their aligned sites
    public User updateUserWithAlignedSites(String xlogin, User userDetails) {

        User user = userRepository.findByXlogin(xlogin)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user fields
        user.setXusrname(userDetails.getXusrname());
        user.setEmail(userDetails.getEmail());
        user.setAddPicktcktflg(userDetails.getAddPicktcktflg());
        user.setLansec(userDetails.getLansec());
        user.setLngmain(userDetails.getLngmain());
        user.setBpadd(userDetails.getBpadd());
        user.setBpadd1(userDetails.getBpadd1());
        user.setBpadd2(userDetails.getBpadd2());
        user.setCalendarrpflg(userDetails.getCalendarrpflg());
        user.setCity(userDetails.getCity());
        user.setCountry(userDetails.getCountry());
        user.setFleetmgmtflg(userDetails.getFleetmgmtflg());
        user.setMapviewrpflg(userDetails.getMapviewrpflg());
        user.setPhone(userDetails.getPhone());
        user.setPincode(userDetails.getPincode());
        user.setRemovePicktcktflg(userDetails.getRemovePicktcktflg());
        user.setRouteplannerflg(userDetails.getRouteplannerflg());
        user.setSchedulerflg(userDetails.getSchedulerflg());
        user.setScreportsflg(userDetails.getScreportsflg());
        user.setState(userDetails.getState());
        user.setTel(userDetails.getTel());
        user.setUsermgmtflg(userDetails.getUsermgmtflg());
        user.setXpswd(userDetails.getXpswd());
        user.setXact(userDetails.getXact());
        user.setUpddattim(new Date());


        List<String> existingSites = new ArrayList<>();
        user.getAlignedSites().forEach(site->{
            existingSites.add(site.getFcy());
        });

        List<String> alignedSites = new ArrayList<>();
        userDetails.getAlignedSites().forEach(site->{
            alignedSites.add(site.getFcy());
        });

        if(!existingSites.isEmpty()){
            existingSites.forEach(fcy->{
                if(!alignedSites.contains(fcy)){
                    deleteSiteForUserUpdate(userDetails.getXlogin(), fcy);
                }
            });
        }

        // Clear and update aligned sites
        user.getAlignedSites().clear();
        userDetails.getAlignedSites().forEach(site -> site.setUser(user));
        user.getAlignedSites().addAll(userDetails.getAlignedSites());

        return userRepository.save(user);
    }

    private void deleteSiteForUserUpdate(String user, String fcy) {
        entityManager.createNativeQuery(MessageFormat
                .format("delete from {0}.{1} where XAUS_0= ''{2}'' and XFCY_0= ''{3}'' ",
                        dbSchema, "XX10CUSERD", user, fcy)).executeUpdate();
    }

    // Delete user and their aligned sites
    public void deleteUserWithAlignedSites(String xlogin) {
        User user = userRepository.findByXlogin(xlogin)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    // List all users
    public List<User> listfullUsers() {
        return userRepository.findAll();
    }


    public boolean checkUserExists(String xlogin) {
        User user = userRepository.findByXlogin(xlogin).orElse(null);
        return user!=null;
    }


}
