package com.example.demo.repository;

import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnifiedAircraftRepository extends JpaRepository<UnifiedAircraft, Long> {
    List<UnifiedAircraft> findByType(AircraftType type);
    List<UnifiedAircraft> findByMissionStatus(MissionStatus status);
    List<UnifiedAircraft> findByName(String name);
} 