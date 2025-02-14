package com.example.mediasoftjavaeecityguide;

import com.example.mediasoftjavaeecityguide.model.City;
import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.model.LocationCategory;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import com.example.mediasoftjavaeecityguide.service.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest(classes = {LocationService.class})
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
        City city = new City(TEST_CITY_NAME);
        Location location = new Location(TEST_LOCATION_NAME,
                LocationCategory.ARCHITECTURE,
                new GeoPoint(0.0, 0.0),
                0.0,
                0,
                city);

        Mockito.when(locationRepository.findByName(TEST_LOCATION_NAME))
                .thenReturn(Optional.of(location));
    }

    @Test
    void addInitialRatingScore() {
        double newScore = 5.0;

        locationService.addRatingScore(TEST_LOCATION_NAME, newScore);

        Assertions.assertTrue(locationService.findByName(TEST_LOCATION_NAME).isPresent());
        Assertions.assertEquals(locationService.findByName(TEST_LOCATION_NAME).get().getRating(), newScore);
    }

    @Test
    void addRatingScoreToNotZeroScore() {
        double initScore = 2.0;
        double newScore = 5.0;

        locationService.addRatingScore(TEST_LOCATION_NAME, initScore);
        locationService.addRatingScore(TEST_LOCATION_NAME, newScore);

        Assertions.assertTrue(locationService.findByName(TEST_LOCATION_NAME).isPresent());
        Assertions.assertEquals(locationService.findByName(TEST_LOCATION_NAME).get().getRating(), (newScore + initScore) / 2);
    }

}
