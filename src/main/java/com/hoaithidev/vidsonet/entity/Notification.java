package com.hoaithidev.vidsonet.entity;

import com.hoaithidev.vidsonet.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private boolean seen;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // UPLOAD, LIKE_VIDEO, COMMENT,...

    // lưu id của channel nhận thông báo
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Channel recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Channel sender;

    private Long objectId; // id của object liên quan (video, comment, v.v.)




}
