package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 255, unique = true)
    private String name; // Tên kênh (phải duy nhất)

    @Column(length = 500)
    private String description; // Mô tả kênh

//    @Column(nullable = false)
    private String profileImageUrl; // Ảnh đại diện kênh

//    @Column(nullable = false)
    private String bannerImageUrl; // Ảnh bìa kênh

    @Column(nullable = false)
    @Builder.Default
    private Integer subscribersCount = 0; // Số lượng người đăng ký

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Chủ sở hữu kênh (1 user chỉ có 1 channel)

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>(); // Danh sách video của kênh

    @Column(nullable = false)
    private LocalDateTime createdAt; // Ngày tạo kênh

    public int increaseSubscribersCount() {
        return ++subscribersCount;
    }
}
