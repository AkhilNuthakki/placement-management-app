package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.placement_management_app.model.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildAssessmentRequestDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AssessmentRequestDto {
    private String id;
    private String requestId;
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
    private String placementAuthorizationRequestStatus;
    private String authorizationComments;
}
