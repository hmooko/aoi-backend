
package com.koo.aoi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING) // Enum의 이름을 DB에 저장 (EnumType.ORDINAL은 순서를 저장하므로 비권장)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist // 엔티티가 처음 저장될 때 실행
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate // 엔티티가 업데이트될 때 실행
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 사용자 역할 Enum
    public enum Role {
        USER, ADMIN
    }
}
