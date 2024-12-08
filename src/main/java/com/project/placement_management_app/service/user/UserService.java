package com.project.placement_management_app.service.user;

import com.project.placement_management_app.dto.request.LoginRequestDto;
import com.project.placement_management_app.dto.request.RegisterUserRequestDto;
import com.project.placement_management_app.dto.response.LoginResponseDto;
import com.project.placement_management_app.model.Role;
import com.project.placement_management_app.model.User;

import java.util.List;

public interface UserService {

    void addUser(RegisterUserRequestDto registerUserRequestDto);
    LoginResponseDto validateUser(LoginRequestDto loginRequestDto);
    User getUserById(String userId);
    User getUserByEmailId(String userEmailId);
    User registerProvider(String emailId, String userName);
    List<User> getUsersBySchoolAndRole(String school, Role role);

}
