package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildUserWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    @MongoId
    private String id;
    @Indexed(unique = true)
    private String emailId;
    private String password;
    private String firstName;
    private String lastName;
    private String school;
    private Role userRole;
}
