package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "channel_subscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subscriber_id", "channel_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private Channel subscriber; // Kênh thực hiện đăng ký

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel; // Kênh được đăng ký

    @Column(name = "subscribed_at", nullable = false, updatable = false)
    private LocalDateTime subscribedAt;

    @PrePersist
    protected void onSubscribe() {
        this.subscribedAt = LocalDateTime.now();
    }
}
