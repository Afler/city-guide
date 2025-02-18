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
import org.springdoc.webmvc.core.service.RequestService;
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

    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

    @Transactional
    public Location addRatingScore(String locationName, Double score) {
        Location location = locationRepository.findByName(locationName).orElseThrow(LocationNotFoundException::new);
        Double currentRating = location.getRating();
        Integer currentRatingNum = location.getRatingNum();

        Double newRating = (currentRating * currentRatingNum + score) / (currentRatingNum + 1);

        location.setRating(newRating);
        location.setRatingNum(currentRatingNum + 1);

        return locationRepository.save(location);
    }

    public List<Location> findNearestNative(FindLocationRequest findLocationRequest) {
        LocationSortField sortField = findLocationRequest.getSortBy();
        Sort sortRule = getSortRule(sortField);
        PageRequest pagination = PageRequest.of(0, findLocationRequest.getMaxCount(), sortRule);

        return locationRepository.findNearest(
                findLocationRequest.getMaxDistanceFilter(),
                findLocationRequest.getCurrentUserPosition().getLatitude(),
                findLocationRequest.getCurrentUserPosition().getLongitude(),
                findLocationRequest.getMaxCount(),
                findLocationRequest.getMinRating(),
                findLocationRequest.getCategory() == null ? null : findLocationRequest.getCategory().toString(),
                pagination);
    }

    public List<Location> findByCityNameNative(FindCityLocationsRequest findNearestLocationsInCityRequest) {
        LocationSortField sortField = findNearestLocationsInCityRequest.getSortBy();
        Sort sortRule = getSortRule(sortField);
        PageRequest pagination = PageRequest.of(0, findNearestLocationsInCityRequest.getMaxCount(), sortRule);

        return locationRepository.findByCityName(
                findNearestLocationsInCityRequest.getMaxDistanceFilter(),
                findNearestLocationsInCityRequest.getCityName(),
                findNearestLocationsInCityRequest.getCurrentUserPosition().getLatitude(),
                findNearestLocationsInCityRequest.getCurrentUserPosition().getLongitude(),
                findNearestLocationsInCityRequest.getMaxCount(),
                findNearestLocationsInCityRequest.getMinRating(),
                findNearestLocationsInCityRequest.getCategory() == null ? null : findNearestLocationsInCityRequest.getCategory().toString(),
                pagination);
    }

    private static Sort getSortRule(LocationSortField sortField) {
        Sort sortRule;
        if (sortField == null || sortField.toString().isBlank()) {
            sortRule = Sort.unsorted();
        } else {
            sortRule = Sort.by(Sort.Direction.ASC, sortField.getFieldColumnName());
        }
        return sortRule;
    }

    public Page<Location> findNearestSpec(FindLocationRequest findLocationRequest) {
        PageRequest pagination = PageRequest.of(0, findLocationRequest.getMaxCount());

        return locationRepository.findAll(
                Specification.where(hasMaxDistanceWithSort(findLocationRequest))
                        .and(hasCategory(findLocationRequest.getCategory()))
                        .and(hasMinimumRating(findLocationRequest.getMinRating()))
                        .and(orderBy(findLocationRequest.getSortBy())),
                pagination);
    }

    public Page<Location> findByCitySpec(FindCityLocationsRequest findNearestLocationsInCityRequest) {
        PageRequest pagination = PageRequest.of(0, findNearestLocationsInCityRequest.getMaxCount());

        return locationRepository.findAll(
                Specification.where(hasCity(findNearestLocationsInCityRequest.getCityName()))
                        .and(hasMaxDistanceWithSort(findNearestLocationsInCityRequest))
                        .and(hasCategory(findNearestLocationsInCityRequest.getCategory()))
                        .and(hasMinimumRating(findNearestLocationsInCityRequest.getMinRating()))
                        .and(orderBy(findNearestLocationsInCityRequest.getSortBy())),
                pagination);
    }

    private static Specification<Location> orderBy(LocationSortField sortField) {
        return (root, query, criteriaBuilder) -> {
            if (sortField != null && !sortField.equals(LocationSortField.DISTANCE)) {
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

            Join<Object, Object> locationCity = root.join("city");
            return criteriaBuilder.equal(locationCity.get("name"), criteriaBuilder.literal(cityName));
        };
    }

    private static Specification<Location> hasMaxDistanceWithSort(FindLocationRequest findLocationRequest) {
        return (root, query, criteriaBuilder) -> {
            Double maxDistanceFilter = findLocationRequest.getMaxDistanceFilter();
            LocationSortField sortField = findLocationRequest.getSortBy();
            if (maxDistanceFilter == null && sortField != LocationSortField.DISTANCE) return null;

            Expression<Double> distanceToUser = getDistanceToUser(findLocationRequest, root, criteriaBuilder);

            if (sortField == LocationSortField.DISTANCE)
                query.orderBy(criteriaBuilder.asc(distanceToUser));

            if (maxDistanceFilter != null)
                return criteriaBuilder.le(distanceToUser, maxDistanceFilter);
            else return null;
        };
    }

    private static Expression<Double> getDistanceToUser(FindLocationRequest findLocationRequest, Root<Location> root, CriteriaBuilder criteriaBuilder) {
        Path<Double> locationLat = root.get("coordinates").get("latitude");
        Path<Double> locationLon = root.get("coordinates").get("longitude");
        Expression<Geometry> locationCoords = criteriaBuilder.function("ST_Point", Geometry.class, locationLat, locationLon);

        Expression<Double> currentUserLat = criteriaBuilder.literal(findLocationRequest.getCurrentUserPosition().getLatitude());
        Expression<Double> currentUserLon = criteriaBuilder.literal(findLocationRequest.getCurrentUserPosition().getLongitude());
//            JTSSpatialCriteriaBuilder jtsBuilder = ((SqmCriteriaNodeBuilder) criteriaBuilder).unwrap(JTSSpatialCriteriaBuilder.class);
        Expression<Geometry> userCoords = criteriaBuilder.function("ST_Point", Geometry.class, currentUserLat, currentUserLon);
        return criteriaBuilder.function("ST_DistanceSphere", Double.class, locationCoords, userCoords);
    }

}
