package com.example.mediasoftjavaeecityguide.utils;

import com.example.mediasoftjavaeecityguide.model.GeoPoint;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GeoUtils {

    public static final double EARTH_RADIUS = 6_371_000.0;

    public double calculateDistance(GeoPoint fromPoint, GeoPoint toPoint) {

        double arg = Math.cos(Math.PI * (toPoint.getLongitude() - fromPoint.getLongitude()) / 180.0)
                     * Math.cos(Math.PI * fromPoint.getLatitude() / 180.0) * Math.cos(Math.PI * toPoint.getLatitude() / 180.0)
                     + Math.sin(Math.PI * fromPoint.getLatitude() / 180.0) * Math.sin(Math.PI * toPoint.getLatitude() / 180.0);

        double sqrtv = arg > 1 ? 0 : Math.sqrt(0.5 * (1.0 - arg));

        double distanceInMeters = 2.0 * EARTH_RADIUS * Math.asin(sqrtv);

        return distanceInMeters;
    }
}
