package com.dhanush.SoloSync.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HemoController {

    @GetMapping("/status")
    public String home(){
        return "Application is Running";
    }

    @GetMapping("/test1")
    public String test(){
        return "testing the jwt token";
    }
}
