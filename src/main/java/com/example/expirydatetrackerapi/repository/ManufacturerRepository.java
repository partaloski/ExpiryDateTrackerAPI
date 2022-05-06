package com.example.expirydatetrackerapi.repository;

import com.example.expirydatetrackerapi.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
}
