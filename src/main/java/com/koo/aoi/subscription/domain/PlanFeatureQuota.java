package com.koo.aoi.subscription.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "plan_feature_quota")
public class PlanFeatureQuota {
    @EmbeddedId
    private PlanFeatureQuotaId id;

    @MapsId("planId")
    @ManyToOne(fetch = FetchType.LAZY) private Plan plan;

    @MapsId("featureId")
    @ManyToOne(fetch = FetchType.LAZY) private Feature feature;

    @Column(nullable=false)
    private int quota;
}
