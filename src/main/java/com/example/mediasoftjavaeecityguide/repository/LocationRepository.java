package com.example.mediasoftjavaeecityguide.repository;

import com.example.mediasoftjavaeecityguide.model.Location;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    @Query(value = """
            with locations_by_distance AS
            (SELECT id, name, category, rating, latitude, longitude, rating_num, city_id,
            (ST_DistanceSphere(ST_Point(loc.latitude, loc.longitude), ST_Point(:fromLat, :fromLon))) as distance
            from location as loc
            where (:category IS NULL OR category = :category)
            and (:minRating IS NULL OR rating >= :minRating)
            order by :sortField
            limit :lim)
            select * from locations_by_distance
            where (:dist IS NULL OR distance <= :dist)
            """, nativeQuery = true)
    List<Location> findNearest(@Param("dist") Double distance,
                               @Param("fromLat") Double fromLat,
                               @Param("fromLon") Double fromLon,
                               @Param("lim") Integer limit,
                               @Param("minRating") @DecimalMin("0.0") @DecimalMax("5.0") Double minRating,
                               @Param("category") String category,
                               @Param("sortField") String sortField);

    @Query(value = """
            with locations_by_distance AS
            (SELECT loc.id, loc.name, category, rating, latitude, longitude, rating_num, city_id,
            (ST_DistanceSphere(ST_Point(loc.latitude, loc.longitude), ST_Point(:fromLat, :fromLon))) as distance
            from location as loc join city on loc.city_id = city.id
            where (:cityName is NULL OR city.name = :cityName)
            and (:category IS NULL OR category = :category)
            and (:minRating IS NULL OR rating >= :minRating)
            order by :sortField
            limit :lim)
            select * from locations_by_distance
            where (:dist IS NULL OR distance <= :dist)
            """, nativeQuery = true)
    List<Location> findByCityName(@Param("dist") Double distance,
                                  @Param("cityName") String cityName,
                                  @Param("fromLat") Double fromLat,
                                  @Param("fromLon") Double fromLon,
                                  @Param("lim") Integer limit,
                                  @Param("minRating") @DecimalMin("0.0") @DecimalMax("5.0") Double minRating,
                                  @Param("category") String category,
                                  @Param("sortField") String sortField);

    Optional<Location> findByName(String name);
}
