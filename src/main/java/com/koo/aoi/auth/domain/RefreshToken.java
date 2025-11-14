package com.koo.aoi.auth.domain;

import com.koo.aoi.user.domain.AoiUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    private Long id;

    @Setter
    @Column(nullable = false)
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private AoiUser user;

    public RefreshToken(AoiUser user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

}
