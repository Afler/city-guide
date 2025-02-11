package com.example.mediasoftjavaeecityguide.repository;

import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.model.LocationCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByCategory(LocationCategory category, Pageable pageable);

}
