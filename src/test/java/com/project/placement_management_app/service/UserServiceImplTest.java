package com.project.placement_management_app.service;

import com.project.placement_management_app.dto.request.LoginRequestDto;
import com.project.placement_management_app.dto.request.RegisterProviderRequestDto;
import com.project.placement_management_app.dto.request.RegisterUserRequestDto;
import com.project.placement_management_app.dto.response.LoginResponseDto;
import com.project.placement_management_app.exception.PasswordNotMatchedException;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.Role;
import com.project.placement_management_app.model.User;
import com.project.placement_management_app.repository.UserRepository;
import com.project.placement_management_app.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    private static final RegisterUserRequestDto registerUserRequestDto = RegisterUserRequestDto.buildRegisterUserRequestDtoWith()
            .firstName("firstName")
            .lastName("lastName")
            .role(Role.STUDENT.toString())
            .emailId("XXX15@student.le.ac.uk").build();

    private static final User user = User.buildUserWith()
            .firstName("firstName")
            .lastName("lastName")
            .userRole(Role.STUDENT)
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .build();

    private static final LoginRequestDto loginRequestDto = LoginRequestDto.buildLoginRequestDto()
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .build();

    private static final RegisterProviderRequestDto registerProviderRequestDto =
            RegisterProviderRequestDto.buildRegisterUserRequestDtoWith()
                    .userName("firstName LastName")
                    .userEmailId("XXX15@student.le.ac.uk")
                    .build();


    @Test
    void givenAlreadyRegisteredUserDetailsForRegistrationThenReturnException(){
        when(userRepository.existsUserByEmailId(anyString())).thenReturn(1);
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.addUser(registerUserRequestDto));
    }

    @Test
    void givenValidUserDetailsForRegistrationThenSaveUser(){
        when(userRepository.existsUserByEmailId(anyString())).thenReturn(0);
        when(userRepository.save(any())).thenReturn(user);
        userService.addUser(registerUserRequestDto);
        verify(userRepository).save(any());
    }

    @Test
    void givenInvalidUserDetailsForLoginThenReturnNotFoundException(){
        when(userRepository.findUserByEmailId(anyString())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> userService.validateUser(loginRequestDto));
    }

    @Test
    void givenInvalidUserDetailsForLoginThenReturnPasswordMisMatchException(){
        when(userRepository.findUserByEmailId(anyString())).thenReturn(user);
        LoginRequestDto loginRequest = LoginRequestDto.buildLoginRequestDto()
                .emailId("XXX15@student.le.ac.uk")
                .password("PASS12345")
                .build();
        assertThrows(PasswordNotMatchedException.class, () -> userService.validateUser(loginRequest));
    }

    @Test
    void givenValidUserDetailsForLoginThenReturnUser(){
        when(userRepository.findUserByEmailId(anyString())).thenReturn(user);
        LoginResponseDto loginResponseDto = userService.validateUser(loginRequestDto);
        assertEquals(loginResponseDto.getEmailId(), loginRequestDto.getEmailId());
    }

    @Test
    void givenInValidUserDetailsForProviderRegistrationThenReturnException(){
        when(userRepository.existsUserByEmailId(anyString())).thenReturn(1);
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.registerProvider(registerProviderRequestDto.getUserEmailId(), registerProviderRequestDto.getUserName()));
    }

    @Test
    void givenValidUserDetailsForProviderRegistrationThenSaveUser(){
        when(userRepository.existsUserByEmailId(anyString())).thenReturn(0);
        when(userRepository.save(any())).thenReturn(user);
        User user = userService.registerProvider(registerProviderRequestDto.getUserEmailId(), registerProviderRequestDto.getUserName());
        assertEquals(user.getEmailId(), registerProviderRequestDto.getUserEmailId());
    }


}
