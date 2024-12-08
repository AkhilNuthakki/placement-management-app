package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.EmailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {
    @Query(value = "{templateId : '?0'}")
    EmailTemplate findEmailTemplateById(String emailTemplateId);
}
