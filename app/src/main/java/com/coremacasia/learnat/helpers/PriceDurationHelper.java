package com.coremacasia.learnat.helpers;

public class PriceDurationHelper {
    private String duration_unit, price, duration;

    public String getDuration_unit() {
        return duration_unit;
    }

    public PriceDurationHelper() {

    }

    public String getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public PriceDurationHelper(String duration_unit, String price, String duration) {
        this.duration_unit = duration_unit;
        this.price = price;
        this.duration = duration;
    }
}
