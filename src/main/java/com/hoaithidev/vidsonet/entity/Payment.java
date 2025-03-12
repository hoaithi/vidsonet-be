package com.hoaithidev.vidsonet.entity;

import com.hoaithidev.vidsonet.enums.PaymentMethod;
import com.hoaithidev.vidsonet.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "member_channel_id", nullable = false)
    private Channel memberChannel;

    @ManyToOne
    @JoinColumn(name = "membership_plan_id", nullable = false)
    private MembershipPlan membershipPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(unique = true, nullable = false)
    private String transactionId;  // Mã giao dịch duy nhất
}



//    payment_id SERIAL PRIMARY KEY,
//    channel_id INT NOT NULL,
//    member_channel_id INT NOT NULL,
//    membership_plan_id INT NOT NULL,
//    payment_method ENUM('paypal') NOT NULL,
//    amount DECIMAL(10, 2) NOT NULL,
//    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    status ENUM('pending', 'completed', 'failed') NOT NULL,
//    FOREIGN KEY (channel_id) REFERENCES Channels(channel_id),
//    FOREIGN KEY (member_channel_id) REFERENCES Channels(channel_id),
//    FOREIGN KEY (membership_plan_id) REFERENCES MembershipPlans(plan_id)

