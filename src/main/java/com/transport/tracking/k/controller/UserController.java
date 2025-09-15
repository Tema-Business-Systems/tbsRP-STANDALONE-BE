package com.transport.tracking.k.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transport.tracking.dto.UserDTO;
import com.transport.tracking.k.annotation.Anonymous;
import com.transport.tracking.k.service.UserService;
import com.transport.tracking.model.User;
import com.transport.tracking.response.AccessTokenVO;
import com.transport.tracking.response.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:8082","http://localhost:8081","http://192.168.1.211:8081","http://192.168.1.211:8082","http://solutions.tema-systems.com:8081","http://solutions.tema-systems.com:8082"})
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping ("/login")
    @Anonymous
    public @ResponseBody ResponseEntity<Object> login(@RequestBody UserVO userVO, HttpServletResponse response) {
        log.info("UserVO  ======== ", userVO);
        try {
            UserVO result = userService.login(userVO, response);
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            Map<String, String> error= new HashMap<>();
            error.put("message",ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping ("/logout")
    @Anonymous
    public @ResponseBody ResponseEntity<Object> logout(HttpServletResponse response) {
        userService.logOut(response);
        return ResponseEntity.status(HttpStatus.OK).body("sucess");
    }


    @GetMapping("/getusers")
    public ResponseEntity<List<UserDTO>> getUserList(AccessTokenVO accessTokenVO) {
        List<UserDTO> users = userService.listUsers();
        return ResponseEntity.ok(users); // Return DTOs instead of User entities
    }



    @PostMapping ("/create")
    public @ResponseBody Map<String, String> createUsers(AccessTokenVO accessTokenVO, @RequestBody User user) throws JsonProcessingException {
        User createdUser = userService.createUserWithAlignedSites(user);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }

    @GetMapping("/{xlogin}")
    public ResponseEntity<User> getUser(@PathVariable String xlogin) {
        User user = userService.getUserWithAlignedSites(xlogin);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{xlogin}")
    public ResponseEntity<User> updateUser(@PathVariable String xlogin, @RequestBody User userDetails) {
        User updatedUser = userService.updateUserWithAlignedSites(xlogin, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{xlogin}")
    public ResponseEntity<String> deleteUser(@PathVariable String xlogin) {
        userService.deleteUserWithAlignedSites(xlogin);
        return ResponseEntity.ok("User and aligned sites deleted successfully");
    }

    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.listfullUsers();
    }
}
