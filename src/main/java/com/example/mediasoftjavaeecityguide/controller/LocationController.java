package com.example.mediasoftjavaeecityguide.controller;

import com.example.mediasoftjavaeecityguide.controller.dto.FindCityLocationsRequest;
import com.example.mediasoftjavaeecityguide.controller.dto.FindLocationRequest;
import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/nearest/native",
            description = "Получить все локации на указанном расстоянии с фильтрацией " +
                          "с помощью нативного SQL запроса."
    )
    public List<Location> findNearestNative(@Valid @RequestBody FindLocationRequest request) {
        return locationService.findNearestNative(request);
    }

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/nearest/spec",
            description = "Получить все локации на указанном расстоянии с фильтрацией " +
                          "с помощью Spring Data JPA Specification API. " +
                          "Для отключения фильтрации по конкретному параметру его необходимо указать как null или удалить из тела запроса."
    )
    public Page<Location> findNearestSpec(@Valid @RequestBody FindLocationRequest request) {
        return locationService.findNearestSpec(request);
    }

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/byCityName/native",
            description = "Получить все локации в указанном городе с фильтрацией " +
                          "с помощью нативного SQL запроса. " +
                          "Для отключения фильтрации по конкретному параметру его необходимо указать как null или удалить из тела запроса."
    )
    public List<Location> findByCityNameNative(@Valid @RequestBody FindCityLocationsRequest request) {
        return locationService.findByCityNameNative(request);
    }

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/byCityName/spec",
            description = "Получить все локации в указанном городе с фильтрацией " +
                          "с помощью Spring Data JPA Specification API. " +
                          "Для отключения фильтрации по конкретному параметру его необходимо указать как null или удалить из тела запроса."
    )
    public Page<Location> findByCitySpec(@Valid @RequestBody FindCityLocationsRequest request) {
        return locationService.findByCitySpec(request);
    }

    @SpringSwaggerEndpoint(
            method = RequestMethod.GET,
            path = "/findByName",
            description = "Найти локацию по названию."
    )
    public Location addLocationRatingScore(@RequestParam String locationName) {
        return locationService.findByName(locationName).orElse(null);
    }

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/addScore",
            description = "Выставить оценку локации."
    )
    public Location addLocationRatingScore(@RequestParam String locationName,
                                           @RequestParam @DecimalMin("0.0") @DecimalMax("5.0") Double score) {
        return locationService.addRatingScore(locationName, score);
    }

}
