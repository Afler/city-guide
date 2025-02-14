package com.example.mediasoftjavaeecityguide.controller;

import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/nearest/native",
            description = "Получить все локации на указанном расстоянии с фильтрацией " +
                          "с помощью нативного SQL запроса."
    )
    public List<Location> findNearestNative(@Valid @RequestBody FindNearestLocationsRequest request) {
        return locationService.findNearestNative(request);
    }

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/nearest/spec",
            description = "Получить все локации на указанном расстоянии с фильтрацией " +
                          "с помощью Spring Specification API."
    )
    public List<Location> findNearestSpec(@Valid @RequestBody FindNearestLocationsRequest request) {
        return locationService.findNearestSpec(request);
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
