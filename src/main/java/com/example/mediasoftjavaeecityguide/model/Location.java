package com.example.mediasoftjavaeecityguide.model;

import com.example.mediasoftjavaeecityguide.utils.GeoUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@Entity
@Table(name = "location")
public class Location extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private LocationCategory category;

    @Embedded
    @Column(name = "coordinates", nullable = false)
    private GeoPoint coordinates;

    @Column(name = "rating", nullable = false)
    @NumberFormat(pattern = "0.00")
    private Double rating;

    @Column(name = "ratingNum", nullable = false)
    private Integer ratingNum;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_name")
    private City city;

    public double calculateDistance(Location toLocation) {
        return GeoUtils.calculateDistance(coordinates, toLocation.getCoordinates());
    }

    public double calculateDistance(GeoPoint toPoint) {
        return GeoUtils.calculateDistance(coordinates, toPoint);
    }
}
