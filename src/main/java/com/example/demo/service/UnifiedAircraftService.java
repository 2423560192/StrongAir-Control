package com.example.demo.service;

import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.dto.MissionRequest;
import com.example.demo.entity.Mission;
import com.example.demo.dto.AircraftStatsDTO;

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
    
    /**
     * 获取飞机的当前任务状态
     * @param id 飞机ID
     * @return 当前正在执行的任务，如果没有则返回null
     */
    Mission getCurrentMission(Long id);

    AircraftStatsDTO getStats();
} 