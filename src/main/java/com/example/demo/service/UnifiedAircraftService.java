package com.example.demo.service;

import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.dto.MissionRequest;
import com.example.demo.entity.Mission;

import java.util.List;

public interface UnifiedAircraftService {
    UnifiedAircraft save(UnifiedAircraft aircraft);
    UnifiedAircraft findById(Long id);
    List<UnifiedAircraft> findAll();
    List<UnifiedAircraft> findByType(AircraftType type);
    List<UnifiedAircraft> findByMissionStatus(MissionStatus status);
    void deleteById(Long id);
    UnifiedAircraft updateMissionStatus(Long id, MissionStatus status);
    UnifiedAircraft update(Long id, UnifiedAircraft aircraft);
    Mission assignMission(MissionRequest request);
    List<Mission> getAircraftMissions(Long aircraftId);
    Mission recallMission(Long missionId);
} 