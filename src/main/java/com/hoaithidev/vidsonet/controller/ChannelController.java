package com.hoaithidev.vidsonet.controller;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.ChannelRequest;
import com.hoaithidev.vidsonet.dto.request.SubscriptionRequest;
import com.hoaithidev.vidsonet.service.ChannelService;
import com.hoaithidev.vidsonet.service.SubscriptionService;
import com.hoaithidev.vidsonet.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelController {
    private final ChannelService channelService;
    private final VideoService videoService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/getAllChannel")
    public ApiResponse<?> getAllChannel(){
        return ApiResponse.builder()
                .data(channelService.getAllChannel())
                .build();
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ApiResponse<?> createChannel(@ModelAttribute ChannelRequest channelRequest) {
        return ApiResponse.builder()
                .data(channelService.create(channelRequest))
                .message("Create channel successfully")
                .build();
    }
    @PostMapping("/subscribe")
    public ApiResponse<?> subscribeChannel(@RequestBody SubscriptionRequest request) {
        return ApiResponse.builder()
                .data(subscriptionService.subscribe(request))
                .message("Subscribe channel successfully")
                .build();
    }

    @PostMapping("/unsubscribe")
    public ApiResponse<?> unsubscribeChannel(@RequestBody SubscriptionRequest request) {
        return ApiResponse.builder()
                .data(subscriptionService.unsubscribe(request))
                .message("Unsubscribe channel successfully")
                .build();
    }
    @GetMapping("/getVideos")
    public ApiResponse<?> getVideos(@RequestParam String channelId) {
        return ApiResponse.builder()
                .data(videoService.getAllVideoByChannelId(channelId))
                .message("Get videos successfully")
                .build();
    }

    @GetMapping("/getSubscribers")
    public ApiResponse<?> getSubscribers(@RequestParam String channelId) {
        return ApiResponse.builder()
                .data(subscriptionService.getSubscribers(channelId))
                .message("Get subscribers successfully")
                .build();
    }


}
