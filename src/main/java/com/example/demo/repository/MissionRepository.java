package com.example.demo.repository;

import com.example.demo.entity.Mission;
import com.example.demo.entity.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByAircraftId(Long aircraftId);
    List<Mission> findByStatus(MissionStatus status);
} 