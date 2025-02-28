package com.hoaithidev.vidsonet.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoResponse {
    String id;
    String url;
    String title;
    String description;
    Integer duration;
    Integer viewsCount;
    Integer likesCount;
    Integer dislikesCount;
    LocalDateTime uploadTime;
    String channelName;
}
