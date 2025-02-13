package com.example.mediasoftjavaeecityguide.controller;

import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import com.example.mediasoftjavaeecityguide.model.LocationCategory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class FindNearestRequest {

    @NotNull
    private GeoPoint currentUserPosition;

    private Integer maxCount;

    private LocationCategory category;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double minRating;

    private Double maxDistanceFilter;
}
