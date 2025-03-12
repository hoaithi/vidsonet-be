package com.hoaithidev.vidsonet.mapper;


import com.hoaithidev.vidsonet.dto.response.VideoResponse;
import com.hoaithidev.vidsonet.entity.Video;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    VideoResponse toVideoResponse(Video video);
    List<VideoResponse> toVideoResponses(List<Video> videos);
}
