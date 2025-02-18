package com.example.mediasoftjavaeecityguide;

import com.example.mediasoftjavaeecityguide.controller.dto.FindCityLocationsRequest;
import com.example.mediasoftjavaeecityguide.controller.dto.FindLocationRequest;
import com.example.mediasoftjavaeecityguide.model.City;
import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.model.LocationCategory;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import com.example.mediasoftjavaeecityguide.repository.UserRepository;
import com.example.mediasoftjavaeecityguide.service.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {LocationService.class})
@ExtendWith(SpringExtension.class)
public class LocationServiceTest {

    public static final String TEST_CITY_NAME = "Test city";
    public static final String TEST_LOCATION_NAME = "Test location";

    @MockitoBean
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        City city = getCity();
        Location location = getLocation(city);

        Mockito.when(locationRepository.findByName(TEST_LOCATION_NAME)).thenReturn(Optional.of(location));
    }

    private City getCity() {
        return new City(TEST_CITY_NAME);
    }

    private Location getLocation(City city) {
        return new Location(TEST_LOCATION_NAME,
                LocationCategory.ARCHITECTURE,
                new GeoPoint(0.0, 0.0),
                0.0,
                0,
                city, new HashSet<>());
    }


    @Test
    void locationService_AddLocationFirstRatingScore_ReturnLocation() {
        double newScore = 5.0;

        locationService.addRatingScore(TEST_LOCATION_NAME, newScore);

        Assertions.assertEquals(locationRepository.findByName(TEST_LOCATION_NAME).get().getRating(), newScore);
    }


    @Test
    void locationService_AddLocationNotFirstRatingScore_ReturnLocation() {
        double initScore = 2.0;
        double newScore = 5.0;
        locationService.addRatingScore(TEST_LOCATION_NAME, initScore);

        locationService.addRatingScore(TEST_LOCATION_NAME, newScore);

        Assertions.assertEquals(locationRepository.findByName(TEST_LOCATION_NAME).get().getRating(), (newScore + initScore) / 2);
    }

    @Test
    void locationService_FindNearestNative_ReturnLocations() {
        GeoPoint currentUserPosition = new GeoPoint(0.0, 0.0);
        Double maxDistanceFilter = 5000.0;
        Integer maxCount = 2;
        LocationCategory categoryFilter = LocationCategory.ARCHITECTURE;
        Double minRatingFilter = 5.0;
        FindLocationRequest request = FindLocationRequest
                .builder()
                .category(categoryFilter)
                .currentUserPosition(currentUserPosition)
                .maxDistanceFilter(maxDistanceFilter)
                .maxCount(maxCount)
                .minRating(minRatingFilter)
                .build();

        Mockito.when(locationRepository.findNearest(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(List.of(getLocation(getCity())));

        List<Location> foundLocations = locationService.findNearestNative(request);

        Assertions.assertFalse(foundLocations.isEmpty());
        Assertions.assertFalse(foundLocations.contains(getLocation(getCity())));
    }

    @Test
    void locationService_FindByCityNameNative_ReturnLocations() {
        City city = getCity();
        String cityName = city.getName();
        GeoPoint currentUserPosition = new GeoPoint(0.0, 0.0);
        Integer maxCount = 2;
        LocationCategory categoryFilter = LocationCategory.ARCHITECTURE;
        Double minRatingFilter = 5.0;
        FindCityLocationsRequest request = FindCityLocationsRequest
                .builder()
                .currentUserPosition(currentUserPosition)
                .category(categoryFilter)
                .cityName(cityName)
                .maxCount(maxCount)
                .minRating(minRatingFilter)
                .build();

        Mockito.when(locationRepository.findByCityName(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(List.of(getLocation(getCity())));


        List<Location> foundLocations = locationService.findByCityNameNative(request);

        Assertions.assertFalse(foundLocations.isEmpty());
        Assertions.assertFalse(foundLocations.contains(getLocation(city)));
    }
}
