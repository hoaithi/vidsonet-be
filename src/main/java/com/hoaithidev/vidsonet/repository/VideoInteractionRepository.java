package com.hoaithidev.vidsonet.repository;

import com.hoaithidev.vidsonet.entity.VideoInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoInteractionRepository extends JpaRepository<VideoInteraction, Long> {

    Optional<VideoInteraction> findByChannelIdAndVideoId(String channelId, String videoId);
}
