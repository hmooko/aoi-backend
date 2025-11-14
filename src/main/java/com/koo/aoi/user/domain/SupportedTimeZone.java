package com.koo.aoi.user.domain;

public enum SupportedTimeZone {
    ASIA_SEOUL("Asia/Seoul");

    private final String timeZone;
    SupportedTimeZone(String timeZone) { this.timeZone = timeZone; }
    public String getTimeZoneId() { return timeZone; }
}
