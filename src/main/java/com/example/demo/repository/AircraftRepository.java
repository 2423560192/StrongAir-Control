package com.example.demo.repository;

import com.example.demo.entity.Aircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftRepository<T extends Aircraft> extends JpaRepository<T, Long> {
    List<T> findByType(AircraftType type);
    List<T> findByMissionStatus(MissionStatus status);
    List<T> findByName(String name);
} 