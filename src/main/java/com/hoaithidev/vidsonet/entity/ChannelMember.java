package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "channel_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"buyer_id", "seller_id", "membership_plan_id"}))
public class ChannelMember {
    // bản chất cái class giống như bill, lưu thông tin mua hàng của người dùng và thông tin gói mua
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    LocalDateTime joinedAt;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    Channel buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    Channel seller;

    @ManyToOne
    @JoinColumn(name = "membership_plan_id")
    MembershipPlan membershipPlan;

    LocalDateTime expiresAt;



}
