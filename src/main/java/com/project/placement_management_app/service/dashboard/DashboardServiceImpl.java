package com.project.placement_management_app.service.dashboard;

import com.project.placement_management_app.model.RequestCountGroupByStatus;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndCourse;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndProviderName;
import com.project.placement_management_app.service.placementAuthRequest.PlacementAuthRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService{
    @Autowired
    private PlacementAuthRequestService placementAuthRequestService;

    public List<RequestCountGroupByStatus> getRequestsFromSchoolGroupByStatus(String schoolName) {
        return placementAuthRequestService.getRequestsFromSchoolGroupByStatus(schoolName);
    }


    public List<RequestCountGroupByStatusAndCourse> getRequestsFromSchoolGroupByStatusAndCourse(String schoolName) {
        return placementAuthRequestService.getRequestsFromSchoolGroupByStatusAndCourse(schoolName);
    }


    public List<RequestCountGroupByStatusAndProviderName> getRequestsFromSchoolGroupByStatusAndProviderName(String schoolName) {
        return placementAuthRequestService.getRequestsFromSchoolGroupByStatusAndProviderName(schoolName);
    }
}
