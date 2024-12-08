package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(value = "placement_authorization_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(builderMethodName = "buildPlacementAuthorizationRequestWith")
public class PlacementAuthorizationRequest {
    @MongoId
    private String id;
    private String userId;
    private Student student;
    private PlacementProvider placementProvider;
    private PlacementRole placementRole;
    private WorkFactor workFactor;
    private TravelFactor travelFactor;
    private LocationAndRegionFactor locationAndRegionFactor;
    private HealthFactor healthFactor;
    private PersonalFactor personalFactor;
    private PoliciesAndInsuranceFactor policiesAndInsuranceFactor;
    private PlacementAuthorizationRequestStatus requestSubmissionStatus;
    private Date requestSubmissionDate;
    private PlacementAuthorizationRequestStatus providerRegistrationStatus;
    private Date providerNotifiedDate;
    private PlacementAuthorizationRequestStatus providerFormSubmissionStatus;
    private Date providerFormSubmissionDate;
    private String providerFormId;
    private PlacementAuthorizationRequestStatus tutorAssessmentSubmissionStatus;
    private Date tutorAssessmentSubmissionDate;
    private String tutorAssessmentId;
    private PlacementAuthorizationRequestStatus placementAuthorizationRequestStatus;
    private Date authorizationRequestStatusDate;
}
