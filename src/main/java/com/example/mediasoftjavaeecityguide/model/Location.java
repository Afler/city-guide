package com.example.mediasoftjavaeecityguide.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "location",
        indexes = {
                @Index(name = "location_id_index", columnList = "id"),
                @Index(name = "location_category_index", columnList = "category"),
                @Index(name = "location_city_id_index", columnList = "city_id")
        })
@AllArgsConstructor
@NoArgsConstructor
public class Location extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
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
    @JoinColumn(name = "city_id")
    private City city;
}
