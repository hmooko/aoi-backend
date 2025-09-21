package com.koo.aoi.quota.domain;

import com.koo.aoi.subscription.domain.Plan;
import com.koo.aoi.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UsageCounter {
    @EmbeddedId
    private UsageCounterId id;

    @MapsId("user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @MapsId("plan_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @Column(nullable = false)
    private int usageCount;

    @Column(nullable = false)
    private Date usageDate;
}
