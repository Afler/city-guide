package com.example.mediasoftjavaeecityguide.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FindCityLocationsRequest extends FindLocationRequest {

    @Schema(description = "Фильтр по городу.", nullable = true)
    private String cityName;

}
