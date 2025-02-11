package com.example.mediasoftjavaeecityguide.model;

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

    @Enumerated
    @Column(name = "category", nullable = false)
    private LocationCategory category;

    @Column(name = "lat", nullable = false)
    private Double latitude;

    @Column(name = "lon", nullable = false)
    private Double longitude;

    @Column(name = "rating", nullable = false)
    @NumberFormat(pattern = "0.00")
    private Double rating;

    @Column(name = "ratungNum", nullable = false)
    private Integer ratingNum;
}
