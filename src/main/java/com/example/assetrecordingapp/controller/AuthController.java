package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.model.User;
import com.example.assetrecordingapp.payload.LoginRequest;
import com.example.assetrecordingapp.payload.LoginResponse;
import com.example.assetrecordingapp.payload.RegisterRequest;
import com.example.assetrecordingapp.payload.RegisterResponse;
import com.example.assetrecordingapp.service.impl.UserService;
import com.example.assetrecordingapp.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public HttpResponse<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.createUser(registerRequest);
        String token = jwtTokenUtil.generateToken(user);

        RegisterResponse response = new RegisterResponse(
                user.getUserId(),
                user.getUsername(),
                user.getPhone(),
                token
        );

        return HttpResponse.success(response);
    }

    @PostMapping("/login")
    public HttpResponse<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest.getPhone(), loginRequest.getPassword());
        String token = jwtTokenUtil.generateToken(user);

        LoginResponse response = new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getPhone(),
                token
        );

        return HttpResponse.success(response);
    }
}
