package com.example.mediasoftjavaeecityguide.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public enum LocationSortField {
    DISTANCE("distance", "distance"),
    NAME("name", "name"),
    RATING("rating", "rating"),
    RATING_NUM("ratingNum", "ratingNum"),
    CATEGORY("category", "category"),
    CITY("city", "city_id");

    @Setter
    private String value;

    @Setter
    private String fieldColumnName;

    LocationSortField(String value, String fieldColumnName) {
        this.value = value;
        this.fieldColumnName = fieldColumnName;
    }

    @Override
    public String toString() {
        return value;
    }
}
