package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildRequestCountGroupByStatusAndCourseWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestCountGroupByStatusAndCourse {
    private String course;
    private List<RequestCountGroupByStatus> statuses;
}
