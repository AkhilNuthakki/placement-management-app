package com.project.placement_management_app.service.user;

import com.project.placement_management_app.dto.request.LoginRequestDto;
import com.project.placement_management_app.dto.request.RegisterUserRequestDto;
import com.project.placement_management_app.dto.response.LoginResponseDto;
import com.project.placement_management_app.exception.PasswordNotMatchedException;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.Role;
import com.project.placement_management_app.model.User;
import com.project.placement_management_app.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    public void addUser(RegisterUserRequestDto registerUserRequestDto){
        LOG.info("checking if the user already exists");
        if(userRepository.existsUserByEmailId(registerUserRequestDto.getEmailId()) > 0){
            LOG.error("User already exists");
            throw new ResourceAlreadyExistsException("User already exists");
        }
        User user = User.buildUserWith().emailId(registerUserRequestDto.getEmailId())
                .firstName(registerUserRequestDto.getFirstName())
                .lastName(registerUserRequestDto.getLastName())
                .password(registerUserRequestDto.getPassword())
                .school(registerUserRequestDto.getSchool())
                .userRole(Role.valueOf(registerUserRequestDto.getRole().toUpperCase())).build();
        userRepository.save(user);
        LOG.info("User is registered");
    }

    public LoginResponseDto validateUser(LoginRequestDto loginRequestDto){

        User user = userRepository.findUserByEmailId(loginRequestDto.getEmailId());

        if(user == null){
            LOG.error("EmailId provided is Invalid");
            throw new ResourceNotFoundException("EmailId provided is Invalid");
        }

        if(!user.getPassword().equals(loginRequestDto.getPassword())){
            LOG.error("Password didn't match");
            throw new PasswordNotMatchedException("Password didn't match");
        }
        return  LoginResponseDto.buildLoginResponseDto()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailId(user.getEmailId())
                .userRole(user.getUserRole().toString())
                .school(user.getSchool())
                .build();
    }

    public User getUserById(String userId){
        return userRepository.findUserById(userId);
    }

    public User getUserByEmailId(String userEmailId) { return userRepository.findUserByEmailId(userEmailId);}

    public User registerProvider (String emailId, String userName){
        LOG.info("checking if the user already exists");
        if(userRepository.existsUserByEmailId(emailId) > 0){
            LOG.error("User already exists");
            throw new ResourceAlreadyExistsException("User already exists");
        }

        String[] name = userName.split(" ");

        User user = User.buildUserWith().emailId(emailId)
                .firstName(name[0])
                .lastName(name.length > 1 ? name[name.length-1] : "")
                .password(RandomStringUtils.randomAlphanumeric(10))
                .userRole(Role.PROVIDER).build();

        return userRepository.save(user);
    }

    public List<User> getUsersBySchoolAndRole(String school, Role role){
        return userRepository.findUserBySchoolAndRole(school,role);
    }
}
