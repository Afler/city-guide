package com.example.mediasoftjavaeecityguide.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class GeoPoint {

    private Double latitude;

    private Double longitude;
}
