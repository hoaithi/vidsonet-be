package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.VideoRequest;
import com.hoaithidev.vidsonet.dto.response.VideoResponse;
import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.entity.Video;
import com.hoaithidev.vidsonet.entity.VideoInteraction;
import com.hoaithidev.vidsonet.enums.InteractionType;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.mapper.VideoMapper;
import com.hoaithidev.vidsonet.repository.ChannelRepository;
import com.hoaithidev.vidsonet.repository.VideoInteractionRepository;
import com.hoaithidev.vidsonet.repository.VideoRepository;
import com.hoaithidev.vidsonet.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    FileStorageService fileStorageService;
    VideoRepository videoRepository;
    ChannelRepository channelRepository;
    VideoMapper videoMapper;
    VideoInteractionRepository videoInteractionRepository;
    JwtUtil jwtUtil;

    public void uploadVideo(VideoRequest request) throws ParseException {
        String channelId = jwtUtil.getChannelId(jwtUtil.getTokenFromRequest());
        String fileName = fileStorageService.storeFile(request.getFile());
        Video video = Video.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(fileName)
                .duration(0)
                .owner(channelRepository.findById(channelId)
                        .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND)))
                .build();
        videoRepository.save(video);
    }

    public List<VideoResponse> getAllVideo() {
        List<Video> videos = videoRepository.findAll();
        List<VideoResponse> videoResponses = new ArrayList<>();
        for (Video video : videos) {
            VideoResponse videoResponse = videoMapper.toVideoResponse(video);
            videoResponse.setChannelName(video.getOwner().getName());
            videoResponses.add(videoResponse);
        }
        return videoResponses;
    }
    public List<VideoResponse> getAllVideoByChannelId(String channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));
        List<Video> videos = videoRepository.findAllByOwnerId(channelId);
        List<VideoResponse> videoResponses = videoMapper.toVideoResponses(videos);
        for (VideoResponse videoResponse : videoResponses) {
            videoResponse.setChannelName(videos.getFirst().getOwner().getName());
        }
        return videoResponses;
    }

    public VideoResponse getVideoById(String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));
        return videoMapper.toVideoResponse(video);
    }

    public List<VideoResponse> getVideoByChannelId(String channelId) {
        List<Video> videos = videoRepository.findAllByOwnerId(channelId);
        return videoMapper.toVideoResponses(videos);
    }

    @Transactional
    public void likeVideo(String channelId, String videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));
        Optional<VideoInteraction> existingInteraction = videoInteractionRepository.findByChannelIdAndVideoId(channelId, videoId);
        if (existingInteraction.isPresent()) {
            VideoInteraction videoInteraction = existingInteraction.get();
            if(videoInteraction.getInteractionType().equals(InteractionType.LIKE)) {
                video.setLikesCount(Math.max(0, video.getLikesCount() - 1));
                videoInteractionRepository.delete(videoInteraction);
            } else if(videoInteraction.getInteractionType().equals(InteractionType.DISLIKE)) {
                video.setDislikesCount(Math.max(0, video.getDislikesCount() - 1));
                video.setLikesCount(Math.max(0, video.getLikesCount() + 1));
                videoInteraction.setInteractionType(InteractionType.LIKE);
                videoInteractionRepository.save(videoInteraction);
            }

        }else{
            video.setLikesCount(Math.max(0, video.getLikesCount() + 1));
            VideoInteraction videoInteraction = VideoInteraction.builder()
                    .video(video)
                    .channel(channel)
                    .interactionType(InteractionType.LIKE)
                    .build();
            videoInteractionRepository.save(videoInteraction);
        }
        videoRepository.save(video);
    }

//    @Transactional
//    public void dislikeVideo(String channelId, String videoId) {
//        Video video = videoRepository.findById(videoId)
//                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));
//        Channel channel = channelRepository.findById(channelId)
//                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));
//
//        Optional<VideoInteraction> existingInteraction
//                = videoInteractionRepository.findByChannelIdAndVideoId(channelId, videoId);
//        if (existingInteraction.isPresent()) {
//            VideoInteraction videoInteraction = existingInteraction.get();
//            if(videoInteraction.getInteractionType().equals(InteractionType.DISLIKE)) {
//                video.setDislikesCount(Math.max(0, video.getDislikesCount() - 1));
//                videoInteractionRepository.delete(videoInteraction);
//            } else if(videoInteraction.getInteractionType().equals(InteractionType.LIKE)) {
//                video.setDislikesCount(video.getDislikesCount() + 1);
//                video.setLikesCount(Math.max(0, video.getLikesCount() - 1));
//                videoInteraction.setInteractionType(InteractionType.DISLIKE);
//                videoInteractionRepository.save(videoInteraction);
//            }
//        }else{
//            video.setDislikesCount(video.getDislikesCount() + 1);
//            VideoInteraction videoInteraction = VideoInteraction.builder()
//                    .video(video)
//                    .channel(channel)
//                    .interactionType(InteractionType.DISLIKE)
//                    .build();
//            videoInteractionRepository.save(videoInteraction);
//        }
//        videoRepository.save(video);
//    }
@Transactional
public void dislikeVideo(String channelId, String videoId) {
    // Tìm video và channel, ném ngoại lệ nếu không tìm thấy
    Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));

    Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

    // Kiểm tra nếu đã có tương tác với video này từ channel
    Optional<VideoInteraction> existingInteraction =
            videoInteractionRepository.findByChannelIdAndVideoId(channelId, videoId);

    if (existingInteraction.isPresent()) {
        VideoInteraction interaction = existingInteraction.get();

        // Nếu đã dislike trước đó, thì undislike (xóa tương tác)
        if (interaction.getInteractionType().equals(InteractionType.DISLIKE)) {
            // Giảm số lượng dislike của video
            video.setDislikesCount(Math.max(0, video.getDislikesCount() - 1));
            // Xóa tương tác
            videoInteractionRepository.delete(interaction);
        }
        // Nếu đã like trước đó, chuyển thành dislike
        else if (interaction.getInteractionType().equals(InteractionType.LIKE)) {
            // Giảm số lượng like và tăng số lượng dislike
            video.setLikesCount(Math.max(0, video.getLikesCount() - 1));
            video.setDislikesCount(video.getDislikesCount() + 1);
            // Cập nhật loại tương tác
            interaction.setInteractionType(InteractionType.DISLIKE);
            videoInteractionRepository.save(interaction);
        }
    } else {
        // Chưa có tương tác, tạo mới tương tác DISLIKE
        VideoInteraction newInteraction = VideoInteraction.builder()
                .video(video)
                .channel(channel)
                .interactionType(InteractionType.DISLIKE)
                .build();

        // Tăng số lượng dislike của video
        video.setDislikesCount(video.getDislikesCount() + 1);

        // Lưu tương tác mới
        videoInteractionRepository.save(newInteraction);
    }

    // Lưu thay đổi của video
    videoRepository.save(video);
}

    public void deleteVideo(String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));
        videoRepository.delete(video);
    }
}
