package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.Role;
import com.project.placement_management_app.model.User;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{emailId:'?0'}")
    User findUserByEmailId(String emailId);

    @CountQuery(value = "{emailId:'?0'}")
    int existsUserByEmailId(String emailId);
    @Query(value = "{_id:'?0'}")
    User findUserById(String userId);

    @Query(value = "{$and : [{school:'?0'},{userRole:'?1'}]}")
    List<User> findUserBySchoolAndRole(String school, Role role);
}
