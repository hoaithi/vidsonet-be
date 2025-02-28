package com.hoaithidev.vidsonet.dto.request;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelRequest {
    private String name;
    private String description;
    private MultipartFile profileImageUrl;
    private MultipartFile bannerImageUrl;
    private String userId;
}
