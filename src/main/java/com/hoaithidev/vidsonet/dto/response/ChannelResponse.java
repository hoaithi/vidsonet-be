package com.hoaithidev.vidsonet.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
