package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.VideoRequest;
import com.hoaithidev.vidsonet.dto.response.VideoResponse;
import com.hoaithidev.vidsonet.entity.Video;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.ChannelRepository;
import com.hoaithidev.vidsonet.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    FileStorageService fileStorageService;
    VideoRepository videoRepository;
    ChannelRepository channelRepository;

    public void uploadVideo(VideoRequest request) {
        String fileName = fileStorageService.storeFile(request.getFile());
        Video video = Video.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(fileName)
                .duration(0)
                .channel(channelRepository.findById(request.getChannelId())
                        .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND)))
                .build();
        videoRepository.save(video);
    }

    public List<VideoResponse> getAllVideo() {
        List<Video> videos = videoRepository.findAll();
        List<VideoResponse> videoResponses = new ArrayList<>();
        for (Video video : videos) {
            VideoResponse videoResponse = VideoResponse.builder()
                    .id(video.getId())
                    .title(video.getTitle())
                    .description(video.getDescription())
                    .url(video.getUrl())
                    .duration(video.getDuration())
                    .channelName(video.getChannel().getName())
                    .uploadTime(video.getUploadTime())
                    .likesCount(video.getLikesCount())
                    .dislikesCount(video.getDislikesCount())
                    .viewsCount(video.getViewsCount())
                    .build();
            videoResponses.add(videoResponse);
        }
        return videoResponses;
    }

    public VideoResponse getVideoById(String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));
        return VideoResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .url(video.getUrl())
                .duration(video.getDuration())
                .channelName(video.getChannel().getName())
                .uploadTime(video.getUploadTime())
                .likesCount(video.getLikesCount())
                .dislikesCount(video.getDislikesCount())
                .viewsCount(video.getViewsCount())
                .build();
    }
}
