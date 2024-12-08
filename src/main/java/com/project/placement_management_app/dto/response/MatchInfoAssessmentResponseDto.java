package com.project.placement_management_app.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildMatchInfoAssessmentResponseDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MatchInfoAssessmentResponseDto {
    private String fieldName;
    private String responseByStudent;
    private String responseByProvider;
    private boolean isMatched;
}
