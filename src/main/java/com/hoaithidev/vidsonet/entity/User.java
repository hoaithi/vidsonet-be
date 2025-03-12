package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 255, unique = true)
    private String email; // Email (phải unique)

    @Column(nullable = false)
    private String password; // Mật khẩu đã mã hóa

    private LocalDateTime createdAt;// Ngày tạo tài khoản

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "role")
    Role role;


}
