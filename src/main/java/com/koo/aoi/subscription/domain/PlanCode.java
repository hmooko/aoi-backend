package com.koo.aoi.subscription.domain;

public enum PlanCode {
    FREE("FREE"),
    PREMIUM_MONTH("PREMIUM_MONTH");

    private final String code;
    PlanCode(String code) { this.code = code; }
    public String code(){ return code; }
}
