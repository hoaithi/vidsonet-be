package com.hoaithidev.vidsonet.controller;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.UserRequest;
import com.hoaithidev.vidsonet.service.UserService;
import com.hoaithidev.vidsonet.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final SubscriptionService userSubscriptionService;

    @PostMapping("/create")
    public ApiResponse<Boolean> create (@RequestBody UserRequest request) {
        boolean isSuccess = userService.create(request);
        return ApiResponse.<Boolean>builder()
                .data(isSuccess)
                .message(isSuccess ? "User created successfully" : "Error creating user")
                .build();
    }
//    @PostMapping("/{userId}/subscribe/{channelId}")
//    public ApiResponse<Void> subscribeToChannel(@PathVariable String userId, @PathVariable String channelId) {
//        // Thêm kênh vào danh sách kênh đã đăng ký của người dùng
//        userSubscriptionService.subscribeToChannel(userId, channelId);
//        return ApiResponse.<Void>builder()
//                .message("Subscribed to channel successfully")
//                .build();
//    }
    @GetMapping("getAllUser")
    public ApiResponse<?> getAllUser(){
        return ApiResponse.builder()
                .data(userService.getAllUser())
                .build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ApiResponse.<Void>builder()
                .message("User deleted successfully")
                .build();
    }
    @DeleteMapping("/deleteAll")
    public ApiResponse<Void> deleteAll() {
        userService.deleteAll();
        return ApiResponse.<Void>builder()
                .message("User deleted successfully")
                .build();
    }

}
