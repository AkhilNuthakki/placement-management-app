package com.project.placement_management_app.controller;

import com.project.placement_management_app.dto.request.LoginRequestDto;
import com.project.placement_management_app.dto.request.RegisterUserRequestDto;
import com.project.placement_management_app.dto.response.LoginResponseDto;
import com.project.placement_management_app.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/placement-management/users")
public class UserController {

    @Autowired
    private UserService userService;

@PostMapping(value = "/register")
public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequestDto){
    userService.addUser(registerUserRequestDto);
    return new ResponseEntity<>("User Created", HttpStatus.CREATED);
}

@PostMapping(value = "/login")
public ResponseEntity<LoginResponseDto> validateUser(@Valid @RequestBody LoginRequestDto loginRequestDto){
    return new ResponseEntity<>(userService.validateUser(loginRequestDto), HttpStatus.OK);
}
}
