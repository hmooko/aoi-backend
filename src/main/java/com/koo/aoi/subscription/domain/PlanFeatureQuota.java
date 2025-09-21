package com.koo.aoi.subscription.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter
public class PlanFeatureQuota {
    @EmbeddedId
    private PlanFeatureQuotaId id;

    @MapsId("plan_id")
    @ManyToOne(fetch = FetchType.LAZY) private Plan plan;

    @MapsId("feature_id")
    @ManyToOne(fetch = FetchType.LAZY) private Feature feature;

    @Column(nullable=false)
    private int quota;
}

