package com.example.mediasoftjavaeecityguide.controller.dto;


import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import com.example.mediasoftjavaeecityguide.model.LocationCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FindLocationRequest {

    @Schema(description = "Фильтр по максимальной дистанции от текущей позиции")
    private Double maxDistanceFilter;

    @NotNull
    @Schema(description = "Текущая позиция пользователя")
    private GeoPoint currentUserPosition;

    @Schema(description = "Максимальное количество возвращаемых локаций.", nullable = true, defaultValue = "10")
    private Integer maxCount = 10;

    @Schema(description = "Фильтр по категории.", nullable = true)
    private LocationCategory category;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    @Schema(description = "Фильтр по минимальному рейтингу локации.",
            nullable = true,
            minimum = "0.0",
            maximum = "5.0",
            defaultValue = "0.0")
    private Double minRating = 0.0;

    @Schema(description = "Поле сортировки.", defaultValue = "DISTANCE", nullable = true)
    private LocationSortField sortBy;
}
