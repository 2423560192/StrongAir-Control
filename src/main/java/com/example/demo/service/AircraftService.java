package com.example.demo.service;

import com.example.demo.entity.Aircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.dto.MissionRequest;
import com.example.demo.entity.Mission;

import java.util.List;

public interface AircraftService {
    Aircraft save(Aircraft aircraft);
    Aircraft findById(Long id);
    List<Aircraft> findAll();
    List<Aircraft> findByType(AircraftType type);
    List<Aircraft> findByMissionStatus(MissionStatus status);
    void deleteById(Long id);
    Aircraft updateMissionStatus(Long id, MissionStatus status);
    Aircraft update(Long id, Aircraft aircraft);
    Mission assignMission(MissionRequest request);
    List<Mission> getAircraftMissions(Long aircraftId);
    Mission recallMission(Long missionId);
} 