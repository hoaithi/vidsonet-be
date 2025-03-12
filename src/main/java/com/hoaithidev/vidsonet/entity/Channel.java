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

    @Column(nullable = false)
    private String profileImageUrl; // Ảnh đại diện kênh

    @Column(nullable = false)
    private String bannerImageUrl; // Ảnh bìa kênh

    @Column(nullable = false)
    @Builder.Default
    private Integer subscribersCount = 0; // Số lượng người đăng ký

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user; // Kênh thuộc về một user

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos; // Danh sách video của kênh

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts; // Danh sách bài viết của kênh

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscribers; // Danh sách người đăng ký kênh

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions; // Danh sách kênh mà kênh này đăng ký

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments; // Danh sách comment của kênh

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Playlist> playlists; // Danh sách playlist của kênh

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification>  receivedNotifications; // Danh sách thông báo của kênh nhận được

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> sentNotifications; // Danh sách thông báo mà kênh đã gửi đi

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembershipPlan> membershipPlans; // Danh sách gói đăng ký của kênh

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMember> memberships; // Danh sách gói đăng ký mà kênh đã mua

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMember> soldMemberships; // Danh sách gói đăng ký mà kênh đã bán

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments; // Danh sách thanh toán của kênh

    @OneToMany(mappedBy = "memberChannel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> memberChannelPayments; // Danh sách thanh toán của kênh khác mua gói đăng ký

    @Column(nullable = false)
    private LocalDateTime createdAt; // Ngày tạo kênh

}
