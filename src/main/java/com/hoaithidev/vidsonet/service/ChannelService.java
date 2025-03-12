package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.ChannelRequest;
import com.hoaithidev.vidsonet.dto.response.ChannelResponse;
import com.hoaithidev.vidsonet.dto.response.VideoResponse;
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
import java.util.stream.Collectors;

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
    public boolean create(String name, String userId, String profileImageUrl, String bannerImageUrl) {
        if(channelRepository.existsByName(name)) {
            throw new AppException(ErrorCode.CHANNEL_ALREADY_EXISTS);
        }
        Channel channel = Channel.builder()
                .name(name)
                .user(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .createdAt(LocalDateTime.now())
                .profileImageUrl(profileImageUrl)
                .bannerImageUrl(bannerImageUrl)
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
                    .profileImageUrl(channel.getProfileImageUrl())
                    .bannerImageUrl(channel.getBannerImageUrl())
                    .subscribersCount(channel.getSubscribersCount())
                    .videos(channel.getVideos().stream().map(video -> VideoResponse.builder()
                            .id(video.getId())
                            .title(video.getTitle())
                            .description(video.getDescription())
                            .url(video.getUrl())
                            .dislikesCount(video.getDislikesCount())
                            .likesCount(video.getLikesCount())
                            .uploadTime(video.getUploadTime())
                            .viewsCount(video.getViewsCount())
                            .build()).collect(Collectors.toList()))
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
                    .profileImageUrl(channel.getProfileImageUrl())
                    .bannerImageUrl(channel.getBannerImageUrl())
                    .subscribersCount(channel.getSubscribersCount())
                    .videos(channel.getVideos().stream().map(video -> VideoResponse.builder()
                                    .id(video.getId())
                                    .title(video.getTitle())
                                    .description(video.getDescription())
                                    .url(video.getUrl())
                                    .dislikesCount(video.getDislikesCount())
                                    .likesCount(video.getLikesCount())
                                    .uploadTime(video.getUploadTime())
                                    .viewsCount(video.getViewsCount())
                                    .build()).collect(Collectors.toList()))
                    .build());
        });
        return channelResponses;
    }


}
