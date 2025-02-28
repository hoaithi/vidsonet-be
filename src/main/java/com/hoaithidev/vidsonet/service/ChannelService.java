package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.ChannelRequest;
import com.hoaithidev.vidsonet.dto.response.ChannelResponse;
import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.ChannelRepository;
import com.hoaithidev.vidsonet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public boolean create(ChannelRequest request) {
        if(channelRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CHANNEL_ALREADY_EXISTS);
        }
        fileStorageService.storeFile(request.getProfileImageUrl());
        fileStorageService.storeFile(request.getBannerImageUrl());

        Channel channel = Channel.builder()
                .name(request.getName())
                .user(userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .createdAt(LocalDateTime.now())
                .profileImageUrl(request.getProfileImageUrl().getOriginalFilename())
                .bannerImageUrl(request.getBannerImageUrl().getOriginalFilename())
                .build();
        return channelRepository.save(channel).getId() != null;
    }
    public Set<ChannelResponse> getAllChannel() {
        Set<ChannelResponse> channelResponses = new HashSet<>();
        channelRepository.findAll().forEach(channel -> {
            channelResponses.add(ChannelResponse.builder()
                    .id(channel.getId())
                    .name(channel.getName())
                    .userId(channel.getUser().getId())
                    .createdAt(channel.getCreatedAt())
                    .build());
        });

        return channelResponses;
    }

    public Set<ChannelResponse> getAllChannelByUserId(String id) {
        Set<ChannelResponse> channelResponses = new HashSet<>();
        channelRepository.findAllByUserId(id).forEach(channel -> {
            channelResponses.add(ChannelResponse.builder()
                    .id(channel.getId())
                    .name(channel.getName())
                    .userId(channel.getUser().getId())
                    .createdAt(channel.getCreatedAt())
                    .build());
        });
        return channelResponses;
    }
}
