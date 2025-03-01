package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.UserRequest;
import com.hoaithidev.vidsonet.entity.User;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.UserRepository;
import com.hoaithidev.vidsonet.utils.JwtUtil;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String signIn(UserRequest request) throws JOSEException {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!isMatch) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return jwtUtil.generateToken(request.getEmail());
    }
    public boolean introspectToken(String token) throws ParseException, JOSEException {
        return !jwtUtil.isTokenExpired(token)&&jwtUtil.verifyToken(token);
    }
}
