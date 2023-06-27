package com.example.offerservice.dtos;

public enum OfferProperty {
    PRICE("price"),
    AREA("area"),
    DATE("date");

    private final String property;

    OfferProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
