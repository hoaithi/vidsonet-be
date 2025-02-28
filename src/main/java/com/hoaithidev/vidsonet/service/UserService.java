package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.ChannelRequest;
import com.hoaithidev.vidsonet.dto.request.UserRequest;
import com.hoaithidev.vidsonet.dto.response.UserResponse;
import com.hoaithidev.vidsonet.entity.User;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public boolean create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("User already exists");
            return false;
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
        return user.getId() != null;
    }

    public Set<UserResponse> getAllUser() {
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
