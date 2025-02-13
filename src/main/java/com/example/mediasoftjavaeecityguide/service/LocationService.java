package com.example.mediasoftjavaeecityguide.service;

import com.example.mediasoftjavaeecityguide.controller.FindNearestRequest;
import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.JpaFunction;
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder;
import org.hibernate.spatial.criteria.JTSSpatialCriteriaBuilder;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public List<Location> findNearestNative(FindNearestRequest findNearestRequest) {
        return locationRepository.findNearest(findNearestRequest.getMaxDistanceFilter(),
                findNearestRequest.getCurrentUserPosition().getLatitude(),
                findNearestRequest.getCurrentUserPosition().getLongitude(),
                findNearestRequest.getMaxCount() == null ? 10 : findNearestRequest.getMaxCount(),
                findNearestRequest.getMinRating() == null ? 0 : findNearestRequest.getMinRating(),
                findNearestRequest.getCategory() == null ? null : findNearestRequest.getCategory().toString());
    }

    public List<Location> findNearestSpec(FindNearestRequest findNearestRequest) {
        return locationRepository.findAll(Specification.where(hasMaxDistance(findNearestRequest)));
    }

    private Specification<Location> hasMaxDistance(FindNearestRequest findNearestRequest) {
        return (root, query, criteriaBuilder) -> {
            JTSSpatialCriteriaBuilder jtsBuilder = ((SqmCriteriaNodeBuilder) criteriaBuilder).unwrap(JTSSpatialCriteriaBuilder.class);
            if (findNearestRequest.getMaxDistanceFilter() == null) return null;
            Path<Double> latitude = root.get("coordinates").get("latitude");
            Path<Double> longitude = root.get("coordinates").get("longitude");

//            jtsBuilder.durationByUnit()

            //TODO использование PostGis функций в спецификации
            Expression<Geometry> coordsAsGeometry = criteriaBuilder.function("ST_Point", Geometry.class, latitude, longitude);
            GeoPoint currentUserPosition = findNearestRequest.getCurrentUserPosition();
            JpaFunction<Geometry> stPoint = jtsBuilder.function("ST_Point", Geometry.class, criteriaBuilder.literal(currentUserPosition.getLatitude()), criteriaBuilder.literal(currentUserPosition.getLongitude()));

//            jtsBuilder.distanceWithin(coordsAsGeometry, )

            return criteriaBuilder.and();
        };
    }

}
