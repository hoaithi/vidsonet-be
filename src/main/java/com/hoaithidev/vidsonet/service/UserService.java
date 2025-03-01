package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.ChannelRequest;
import com.hoaithidev.vidsonet.dto.request.UserRequest;
import com.hoaithidev.vidsonet.dto.response.UserResponse;
import com.hoaithidev.vidsonet.entity.Role;
import com.hoaithidev.vidsonet.entity.User;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.RoleRepository;
import com.hoaithidev.vidsonet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ChannelService channelService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${channel.banner-image-url}")
    String bannerImageUrl;
    @Value("${channel.profile-image-url}")
    String profileImageUrl;


    @Transactional
    public boolean create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("User already exists");
            return false;
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findByName("USER"))
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
        channelService.create(user.getEmail(),user.getId(),profileImageUrl,bannerImageUrl);
        return user.getId() != null;
    }


    @PreAuthorize("hasRole('USER')")
    public Set<UserResponse> getAllUser() {
        var authention = SecurityContextHolder.getContext().getAuthentication();
        log.info("authention: "+authention.getName());
        authention.getAuthorities().stream().map(GrantedAuthority::getAuthority).forEach(log::info);
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .channels(channelService.getAllChannelByUserId(user.getId()))
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toSet());
    }

    public void delete(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.delete(user);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
