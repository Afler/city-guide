package com.example.mediasoftjavaeecityguide.controller;

import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping(value = "/nearest/native")
    public List<Location> findNearestNative(@Valid @RequestBody FindNearestRequest request) {
        return locationService.findNearestNative(request);
    }

    @PostMapping(value = "/nearest/spec")
    public List<Location> findNearestSpec(@Valid @RequestBody FindNearestRequest request) {
        return locationService.findNearestSpec(request);
    }
}
