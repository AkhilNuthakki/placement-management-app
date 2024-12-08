package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.ProviderForm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProviderFormRepository extends MongoRepository<ProviderForm, String> {

    @Query(value = "{_id : '?0'}")
    ProviderForm findProviderFormById(String id);
}
