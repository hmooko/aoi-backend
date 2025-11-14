package com.koo.aoi.subscription.domain;

import com.koo.aoi.user.domain.AoiUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Table(name = "usage_counter")
public class UsageCounter {
    @EmbeddedId
    private UsageCounterId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private AoiUser user;

    @MapsId("featureId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Feature feature;

    @Column(nullable = false)
    private int usageCount;

    @Column(nullable = false)
    private LocalDate usageDate;
}
