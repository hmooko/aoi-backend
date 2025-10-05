package com.koo.aoi.subscription.domain;

public enum FeatureCode {
    AI_PROBLEM("AI_PROBLEM");

    private final String code;
    FeatureCode(String code) { this.code = code; }
    public String code(){ return code; }
}
