package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "channel_subscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subscriber_channel_id", "subscribed_channel_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_channel_id", nullable = false)
    private Channel subscriberChannel; // Kênh thực hiện đăng ký

    @ManyToOne
    @JoinColumn(name = "subscribed_channel_id", nullable = false)
    private Channel subscribedChannel; // Kênh được đăng ký

    @Column(name = "subscribed_at", nullable = false, updatable = false)
    private LocalDateTime subscribedAt;

    @PrePersist
    protected void onSubscribe() {
        this.subscribedAt = LocalDateTime.now();
    }
}
