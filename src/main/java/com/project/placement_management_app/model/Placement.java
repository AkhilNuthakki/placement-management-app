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
@Builder(builderMethodName = "buildPlacementWith")
@Document(value = "placements")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Placement {

    @MongoId
    private String id;
    private String requestId;
    private String roleTitle;
    private Date startDate;
    private Date endDate;
    private Student student;
    private PlacementProvider placementProvider;
}
