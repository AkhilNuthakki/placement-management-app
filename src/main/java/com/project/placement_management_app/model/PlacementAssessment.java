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
@Document(value = "placement_assessments")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementAssessment {

    @MongoId
    private String id;
    private String userId;
    private String studentName;
    private ProviderAssessment providerAssessment;
    private InfoMatchAssessment infoMatchAssessment;
    private RoleAssessment roleAssessment;
    private WorkFactorAssessment workFactorAssessment;
    private TravelFactorAssessment travelFactorAssessment;
    private LocationFactorAssessment locationFactorAssessment;
    private HealthFactorAssessment healthFactorAssessment;
    private PersonalFactorAssessment personalFactorAssessment;
    private PoliciesAndInsuranceAssessment policiesAndInsuranceAssessment;
    private UniversityAccessSupportAssessment universityAccessSupportAssessment;
    private PlacementAuthorizationRequestStatus status;
    private Date statusDate;
    private PlacementAuthorizationRequestStatus placementAuthorizationRequestStatus;
    private Date authorizationRequestStatusDate;
    private String authorizationComments;

}
