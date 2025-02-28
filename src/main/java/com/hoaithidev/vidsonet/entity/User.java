package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Column(nullable = false)
    private LocalDateTime createdAt; // Ngày tạo tài khoản

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Channel> channels; // Mỗi user có nhiều channel

    @ManyToOne
    @JoinColumn(name = "role")
    Role role;


}
