package com.hoaithidev.vidsonet.controller;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.VideoRequest;
import com.hoaithidev.vidsonet.dto.response.VideoResponse;
import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.service.VideoService;
import com.hoaithidev.vidsonet.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;
    private final JwtUtil jwtUtil;

    @PostMapping("/upload")
    public ApiResponse<Boolean> uploadVideo
            (@ModelAttribute VideoRequest request) throws ParseException {
         videoService.uploadVideo(request);
            return ApiResponse.<Boolean>builder()
                    .data(true)
                    .message("Video uploaded successfully")
                    .build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
        return ApiResponse.<Void>builder()
                .message("Video deleted successfully")
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<VideoResponse>> getAllVideo() {
        return ApiResponse.<List<VideoResponse>>builder()
                .data(videoService.getAllVideo())
                .message("Get all video successfully")
                .build();
    }

    @GetMapping("/getVideoById/{id}")
    public ApiResponse<VideoResponse> getVideoById(@PathVariable String id) {
        return ApiResponse.<VideoResponse>builder()
                .data(videoService.getVideoById(id))
                .message("Get video by id successfully")
                .build();
    }
    @GetMapping("/getVideoByChannelId/{channelId}")
    public ApiResponse<List<VideoResponse>> getVideoByChannelId(@PathVariable String channelId) {
        return ApiResponse.<List<VideoResponse>>builder()
                .data(videoService.getVideoByChannelId(channelId))
                .message("Get video by channel id successfully")
                .build();
    }
    @PostMapping("/{videoId}/like")
    public ApiResponse<Void> likeVideo(@PathVariable String videoId) throws ParseException {
        String token = jwtUtil.getTokenFromRequest();
        String channelId = jwtUtil.getChannelId(token);
        videoService.likeVideo(channelId, videoId);
        return ApiResponse.<Void>builder()
                .message("Liked video successfully")
                .build();
    }
    @PostMapping("/{videoId}/dislike")
    public ApiResponse<Void> dislikeVideo(@PathVariable String videoId) throws ParseException {
        String token = jwtUtil.getTokenFromRequest();
        String channelId = jwtUtil.getChannelId(token);
        videoService.dislikeVideo(channelId, videoId);
        return ApiResponse.<Void>builder()
                .message("Dislike video successfully")
                .build();
    }




}
