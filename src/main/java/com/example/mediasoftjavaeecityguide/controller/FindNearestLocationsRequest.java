package com.example.mediasoftjavaeecityguide.controller;

import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import com.example.mediasoftjavaeecityguide.model.LocationCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FindNearestLocationsRequest {

    @NotNull
    @Schema(description = "Текущая позиция пользователя")
    private GeoPoint currentUserPosition;

    @Schema(description = "Фильтр по максимальной дистанции от текущей позиции")
    private Double maxDistanceFilter;

    @Schema(description = "Максимальное количество возвращаемых локаций")
    private Integer maxCount;

    @Schema(description = "Фильтр по категории")
    private LocationCategory category;

    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Schema(description = "Фильтр по минимальному рейтингу локации")
    private Double minRating;
}
