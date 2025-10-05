
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String uid;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeZone timeZone;

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

    public enum TimeZone {
        ASIA_SEOUL("Asia/Seoul");

        private final String timeZone;
        TimeZone(String timeZone) { this.timeZone = timeZone; }
        public String getTimeZoneId() { return timeZone; }
    }

    public enum Role {
        USER, ADMIN
    }
}
