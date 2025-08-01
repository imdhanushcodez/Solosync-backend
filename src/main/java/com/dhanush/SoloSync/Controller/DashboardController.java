package com.dhanush.SoloSync.Controller;

import com.dhanush.SoloSync.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    //Getting dashboard data
    @GetMapping
    public ResponseEntity<Map<String,Object>> getDashBoardData(){
        Map<String,Object> objectMap = dashboardService.getDashboardData();
        return ResponseEntity.ok(objectMap);
    }
}
