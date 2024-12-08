package com.project.placement_management_app.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildEmailTemplateWith")
@Document(value = "email_templates")
public class EmailTemplate {

    @Id
    private String id;
    private String templateId;
    private String subject;
    private boolean isContentHTML;
    private String emailContent;

}
