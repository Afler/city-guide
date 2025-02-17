package com.example.mediasoftjavaeecityguide.service;

import com.example.mediasoftjavaeecityguide.controller.dto.FindCityLocationsRequest;
import com.example.mediasoftjavaeecityguide.controller.dto.FindLocationRequest;
import com.example.mediasoftjavaeecityguide.controller.dto.LocationSortField;
import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.model.LocationCategory;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import com.example.mediasoftjavaeecityguide.repository.UserRepository;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<Location> findNearestNative(FindLocationRequest findLocationRequest) {
        return locationRepository.findNearest(
                findLocationRequest.getMaxDistanceFilter(),
                findLocationRequest.getCurrentUserPosition().getLatitude(),
                findLocationRequest.getCurrentUserPosition().getLongitude(),
                findLocationRequest.getMaxCount(),
                findLocationRequest.getMinRating(),
                findLocationRequest.getCategory() == null ? null : findLocationRequest.getCategory().toString(),
                findLocationRequest.getSortBy() == null ? null : findLocationRequest.getSortBy().toString());
    }

    public List<Location> findByCityNameNative(FindCityLocationsRequest findNearestLocationsInCityRequest) {
        return locationRepository.findByCityName(
                findNearestLocationsInCityRequest.getMaxDistanceFilter(),
                findNearestLocationsInCityRequest.getCityName(),
                findNearestLocationsInCityRequest.getCurrentUserPosition().getLatitude(),
                findNearestLocationsInCityRequest.getCurrentUserPosition().getLongitude(),
                findNearestLocationsInCityRequest.getMaxCount(),
                findNearestLocationsInCityRequest.getMinRating(),
                findNearestLocationsInCityRequest.getCategory() == null ? null : findNearestLocationsInCityRequest.getCategory().toString(),
                findNearestLocationsInCityRequest.getSortBy() == null ? null : findNearestLocationsInCityRequest.getSortBy().toString());
    }

    public Page<Location> findNearestSpec(FindLocationRequest findLocationRequest) {
        Sort sortRule = Sort.by(findLocationRequest.getSortBy().toString());
        PageRequest pagination = PageRequest.of(0, findLocationRequest.getMaxCount(), sortRule);

        return locationRepository.findAll(Specification.where(hasMaxDistance(findLocationRequest))
                        .and(hasCategory(findLocationRequest.getCategory()))
                        .and(hasMinimumRating(findLocationRequest.getMinRating()))
                        .and(orderBy(findLocationRequest.getSortBy())),
                pagination);
    }

    public Page<Location> findByCitySpec(FindCityLocationsRequest findNearestLocationsInCityRequest) {
        Sort sortRule = Sort.by(findNearestLocationsInCityRequest.getSortBy().toString());
        PageRequest pagination = PageRequest.of(0, findNearestLocationsInCityRequest.getMaxCount(), sortRule);

        return locationRepository.findAll(Specification.where(hasCity(findNearestLocationsInCityRequest.getCityName()))
                        .and(hasMaxDistance(findNearestLocationsInCityRequest))
                        .and(hasCategory(findNearestLocationsInCityRequest.getCategory()))
                        .and(hasMinimumRating(findNearestLocationsInCityRequest.getMinRating()))
                        .and(orderBy(findNearestLocationsInCityRequest.getSortBy())),
                pagination);
    }

    private static Specification<Location> orderBy(LocationSortField sortField) {
        return (root, query, criteriaBuilder) -> {
            if (sortField.equals(LocationSortField.DISTANCE)) {
                //TODO сортировка по вычисляемому столбцу
                List<Order> orderList = query.getOrderList();
                query.orderBy(orderList.get(0));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(sortField.toString())));
            }
            return null;
        };
    }

    private static Specification<Location> hasMinimumRating(Double minRating) {
        return (root, query, criteriaBuilder) -> {
            if (minRating == null) return null;

            return criteriaBuilder.ge(root.get("rating"), criteriaBuilder.literal(minRating));
        };
    }

    private static Specification<Location> hasCategory(LocationCategory category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;

            Path<Object> locationCategory = root.get("category");

            return criteriaBuilder.equal(locationCategory, criteriaBuilder.literal(category.toString()));
        };
    }

    private static Specification<Location> hasCity(String cityName) {
        return (root, query, criteriaBuilder) -> {
            if (cityName == null || cityName.isBlank()) return null;

            Join<Object, Object> locationCity = root.join("city_id");
            return criteriaBuilder.equal(locationCity.get("name"), criteriaBuilder.literal(cityName));
        };
    }

    private static Specification<Location> hasMaxDistance(FindLocationRequest findLocationRequest) {
        return (root, query, criteriaBuilder) -> {
            Double maxDistanceFilter = findLocationRequest.getMaxDistanceFilter();
            if (maxDistanceFilter == null) return null;

            Path<Double> locationLat = root.get("coordinates").get("latitude");
            Path<Double> locationLon = root.get("coordinates").get("longitude");
            Expression<Geometry> locationCoords = criteriaBuilder.function("ST_Point", Geometry.class, locationLat, locationLon);

            Expression<Double> currentUserLat = criteriaBuilder.literal(findLocationRequest.getCurrentUserPosition().getLatitude());
            Expression<Double> currentUserLon = criteriaBuilder.literal(findLocationRequest.getCurrentUserPosition().getLongitude());
//            JTSSpatialCriteriaBuilder jtsBuilder = ((SqmCriteriaNodeBuilder) criteriaBuilder).unwrap(JTSSpatialCriteriaBuilder.class);
            Expression<Geometry> userCoords = criteriaBuilder.function("ST_Point", Geometry.class, currentUserLat, currentUserLon);
            Expression<Double> distanceToUser = criteriaBuilder.function("ST_DistanceSphere", Double.class, locationCoords, userCoords);

            return criteriaBuilder.le(distanceToUser, maxDistanceFilter);
        };
    }

}
