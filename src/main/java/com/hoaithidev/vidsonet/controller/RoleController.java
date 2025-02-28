package com.hoaithidev.vidsonet.controller;

import com.hoaithidev.vidsonet.dto.ApiResponse;
import com.hoaithidev.vidsonet.dto.request.RoleRequest;
import com.hoaithidev.vidsonet.dto.response.RoleResponse;
import com.hoaithidev.vidsonet.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ApiResponse<Boolean> create (@RequestBody RoleRequest request) {
        boolean isSuccess = roleService.create(request);
        return ApiResponse.<Boolean>builder()
                .data(isSuccess)
                .message(isSuccess ? "Role created successfully" : "Error creating role")
                .build();
    }

    @GetMapping("")
    public ApiResponse<Set<RoleResponse>> getAll() {
        Set<RoleResponse> roleResponses = roleService.getAll();
        return ApiResponse.<Set<RoleResponse>>builder()
                .data(roleResponses)
                .build();
    }

    @DeleteMapping("/delete/{name}")
    public ApiResponse<Boolean> delete(@PathVariable String name) throws Exception {
        roleService.delete(name);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .message("Role deleted successfully")
                .build();
    }


}
