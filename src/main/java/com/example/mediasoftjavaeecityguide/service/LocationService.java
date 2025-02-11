package com.example.mediasoftjavaeecityguide.service;

import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;


    public List<Location> findAll(){
        return locationRepository.findAll();
    }


}
