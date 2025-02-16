package com.example.mediasoftjavaeecityguide.service;

import com.example.mediasoftjavaeecityguide.controller.dto.FindCityLocationsRequest;
import com.example.mediasoftjavaeecityguide.controller.dto.FindNearestLocationsRequest;
import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import com.example.mediasoftjavaeecityguide.repository.UserRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.query.criteria.JpaFunction;
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder;
import org.hibernate.spatial.criteria.JTSSpatialCriteriaBuilder;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

    @Transactional
    public Location addRatingScore(String locationName, @DecimalMin("1.0") @DecimalMax("5.0") Double score) {
        Location location = locationRepository.findByName(locationName).orElseThrow();
        Double currentRating = location.getRating();
        Integer currentRatingNum = location.getRatingNum();

        Double newRating = (currentRating * currentRatingNum + score) / (currentRatingNum + 1);

        location.setRating(newRating);
        location.setRatingNum(currentRatingNum + 1);

        return locationRepository.save(location);
    }

    public List<Location> findNearestNative(FindNearestLocationsRequest findNearestLocationsRequest) {
        return locationRepository.findNearest(findNearestLocationsRequest.getMaxDistanceFilter(),
                findNearestLocationsRequest.getCurrentUserPosition().getLatitude(),
                findNearestLocationsRequest.getCurrentUserPosition().getLongitude(),
                findNearestLocationsRequest.getMaxCount(),
                findNearestLocationsRequest.getMinRating(),
                findNearestLocationsRequest.getCategory() == null ? null : findNearestLocationsRequest.getCategory().toString());
    }

    public List<Location> findByCityNameNative(FindCityLocationsRequest findNearestLocationsInCityRequest) {
        return locationRepository.findByCityName(
                findNearestLocationsInCityRequest.getCityName(),
                findNearestLocationsInCityRequest.getCurrentUserPosition().getLatitude(),
                findNearestLocationsInCityRequest.getCurrentUserPosition().getLongitude(),
                findNearestLocationsInCityRequest.getMaxCount(),
                findNearestLocationsInCityRequest.getMinRating(),
                findNearestLocationsInCityRequest.getCategory() == null ? null : findNearestLocationsInCityRequest.getCategory().toString());
    }

    public List<Location> findNearestSpec(FindNearestLocationsRequest findNearestLocationsRequest) {
        throw new NotImplementedException();
//        return locationRepository.findAll(Specification.where(hasMaxDistance(findNearestLocationsRequest)));
    }

    private Specification<Location> hasMaxDistance(FindNearestLocationsRequest findNearestLocationsRequest) {
        return (root, query, criteriaBuilder) -> {
            JTSSpatialCriteriaBuilder jtsBuilder = ((SqmCriteriaNodeBuilder) criteriaBuilder).unwrap(JTSSpatialCriteriaBuilder.class);
            if (findNearestLocationsRequest.getMaxDistanceFilter() == null) return null;
            Path<Double> latitude = root.get("coordinates").get("latitude");
            Path<Double> longitude = root.get("coordinates").get("longitude");

//            jtsBuilder.durationByUnit()

            // TODO использование PostGis функций в API спецификации
            Expression<Geometry> coordsAsGeometry = criteriaBuilder.function("ST_Point", Geometry.class, latitude, longitude);
            GeoPoint currentUserPosition = findNearestLocationsRequest.getCurrentUserPosition();
            JpaFunction<Geometry> stPoint = jtsBuilder.function("ST_Point", Geometry.class, criteriaBuilder.literal(currentUserPosition.getLatitude()), criteriaBuilder.literal(currentUserPosition.getLongitude()));

//            jtsBuilder.distanceWithin(coordsAsGeometry, )

            return criteriaBuilder.and();
        };
    }

}
