package com.transport.tracking.k.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping ("/home")
    public String getHome() {
        return "index.html";
    }
}
