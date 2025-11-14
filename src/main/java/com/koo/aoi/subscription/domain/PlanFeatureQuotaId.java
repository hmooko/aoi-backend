package com.koo.aoi.subscription.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

// PlanFeatureQuota.java  (복합키)
@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanFeatureQuotaId implements Serializable {
    private Long planId;
    private Long featureId;
}
