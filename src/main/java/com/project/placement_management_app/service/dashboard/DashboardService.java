package com.project.placement_management_app.service.dashboard;

import com.project.placement_management_app.model.RequestCountGroupByStatus;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndCourse;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndProviderName;

import java.util.List;

public interface DashboardService {

    List<RequestCountGroupByStatus> getRequestsFromSchoolGroupByStatus(String schoolName);
    List<RequestCountGroupByStatusAndCourse> getRequestsFromSchoolGroupByStatusAndCourse(String schoolName);
    List<RequestCountGroupByStatusAndProviderName> getRequestsFromSchoolGroupByStatusAndProviderName(String schoolName);
}
