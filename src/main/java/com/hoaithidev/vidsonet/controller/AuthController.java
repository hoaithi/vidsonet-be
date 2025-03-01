package com.hoaithidev.vidsonet.controller;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.IntrospectRequest;
import com.hoaithidev.vidsonet.dto.request.UserRequest;
import com.hoaithidev.vidsonet.service.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signIn")
    public ApiResponse<?> signIn (@RequestBody UserRequest request) throws JOSEException {
        return ApiResponse.<String>builder()
                .data(authService.signIn(request))
                .message("User sign in successfully")
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<?> introspect (@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        boolean isSuccessful = authService.introspectToken(request.getToken());
        String auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("auth: " + auth);
        return ApiResponse.<Boolean>builder()
                .data(isSuccessful)
                .message(isSuccessful? "User sign in successfully" : "User sign in failed")
                .build();
    }
}
