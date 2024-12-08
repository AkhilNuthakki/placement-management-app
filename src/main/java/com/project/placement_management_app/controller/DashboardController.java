package com.project.placement_management_app.controller;

import com.project.placement_management_app.model.RequestCountGroupByStatus;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndCourse;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndProviderName;
import com.project.placement_management_app.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/placement-management/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    @GetMapping(value = "/request-count-by-status/{schoolName}")
    public ResponseEntity<List<RequestCountGroupByStatus>> getRequestsFromSchoolGroupByStatus(@PathVariable String schoolName){
       return new ResponseEntity<>(dashboardService.getRequestsFromSchoolGroupByStatus(schoolName), HttpStatus.OK);
    }

    @GetMapping(value = "/request-count-by-status-and-course/{schoolName}")
    public ResponseEntity<List<RequestCountGroupByStatusAndCourse>> getRequestsFromSchoolGroupByStatusAndCourse(@PathVariable String schoolName){
        return new ResponseEntity<>(dashboardService.getRequestsFromSchoolGroupByStatusAndCourse(schoolName), HttpStatus.OK);
    }

    @GetMapping(value = "/request-count-by-status-and-provider-name/{schoolName}")
    public ResponseEntity<List<RequestCountGroupByStatusAndProviderName>> getRequestsFromSchoolGroupByStatusAndProviderName(@PathVariable String schoolName){
        return new ResponseEntity<>(dashboardService.getRequestsFromSchoolGroupByStatusAndProviderName(schoolName), HttpStatus.OK);
    }
}
