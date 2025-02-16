package com.example.mediasoftjavaeecityguide.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FindNearestLocationsRequest extends FindLocationRequest {

    @Schema(description = "Фильтр по максимальной дистанции от текущей позиции")
    private Double maxDistanceFilter;

}
