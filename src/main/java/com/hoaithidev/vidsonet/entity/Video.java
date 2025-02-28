package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    private String url; // Link video (có thể là S3, Firebase, v.v.)

    private Integer duration; // Lưu bằng số giây

    @Column(nullable = false)
    @Builder.Default
    private Integer viewsCount = 0; // Lượt xem mặc định là 0

    @Column(nullable = false)
    @Builder.Default
    private Integer likesCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer dislikesCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime uploadTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel; // Video thuộc về một channel



}
