package com.hoaithidev.vidsonet.controller;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.VideoRequest;
import com.hoaithidev.vidsonet.dto.response.VideoResponse;
import com.hoaithidev.vidsonet.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/upload")
    public ApiResponse<Boolean> uploadVideo
            (@ModelAttribute VideoRequest request) {
         videoService.uploadVideo(request);
            return ApiResponse.<Boolean>builder()
                    .data(true)
                    .message("Video uploaded successfully")
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

}
