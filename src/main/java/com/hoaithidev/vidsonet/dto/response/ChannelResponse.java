package com.hoaithidev.vidsonet.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChannelResponse {
    String id;
    String name;
    String userId;
    String profileImageUrl;
    String bannerImageUrl;
    Integer subscribersCount;
    List<VideoResponse> videos;

    LocalDateTime createdAt;
}
