
package com.koo.aoi.user.domain;

import com.koo.aoi.subscription.domain.Plan;
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
public class AoiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportedTimeZone timeZone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

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

    public AoiUser update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }
}
