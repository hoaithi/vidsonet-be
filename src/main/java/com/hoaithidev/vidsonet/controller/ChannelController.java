package com.hoaithidev.vidsonet.controller;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.ChannelRequest;
import com.hoaithidev.vidsonet.dto.request.SubscribeRequest;
import com.hoaithidev.vidsonet.service.ChannelService;
import com.hoaithidev.vidsonet.service.ChannelSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelController {
    private final ChannelService channelService;
    private final ChannelSubscriptionService channelSubscriptionService;

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
    public ApiResponse<?> subscribeChannel(@RequestBody SubscribeRequest subscribeRequest) {
        return ApiResponse.builder()
                .data(channelSubscriptionService.subscribe(subscribeRequest))
                .message("Subscribe channel successfully")
                .build();
    }

    @PostMapping("/unsubscribe")
    public ApiResponse<?> unsubscribeChannel(@RequestBody SubscribeRequest subscribeRequest) {
        return ApiResponse.builder()
                .data(channelSubscriptionService.unsubscribe(subscribeRequest))
                .message("Subscribe channel successfully")
                .build();
    }

}
