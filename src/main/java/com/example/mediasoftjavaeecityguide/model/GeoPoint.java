package com.example.mediasoftjavaeecityguide.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoPoint {

    private Double latitude;

    private Double longitude;
}
