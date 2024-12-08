package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementVisitWith")
@Document(value = "placement_visits")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementVisit {
    @MongoId
    private String id;
    private String placementId;
    private PlacementVisitType placementVisitType;
    private Date startTime;
    private Date endTime;
    private String studentName;
    private String studentEmail;
    private String school;
    private String providerContactName;
    private String providerContactEmail;
}
