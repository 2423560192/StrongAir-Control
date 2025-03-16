package com.example.demo.dto;

import com.example.demo.entity.Mission;
import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.repository.UnifiedAircraftRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
public class MissionDTO {
    private Long id;
    private Long aircraftId;
    private String aircraftName;
    private String aircraftType;
    private String missionName;
    private String description;
    private Integer aircraftCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String missionType;
    private String status;
    
    private static UnifiedAircraftRepository aircraftRepository;
    
    @Autowired
    public void setAircraftRepository(UnifiedAircraftRepository repository) {
        MissionDTO.aircraftRepository = repository;
    }
    
    public static MissionDTO fromEntity(Mission mission) {
        MissionDTO dto = new MissionDTO();
        dto.setId(mission.getId());
        dto.setAircraftId(mission.getAircraftId());
        
        // 获取飞机信息
        if (aircraftRepository != null) {
            UnifiedAircraft aircraft = aircraftRepository.findById(mission.getAircraftId()).orElse(null);
            if (aircraft != null) {
                dto.setAircraftName(aircraft.getName());
                dto.setAircraftType(aircraft.getType().toString());
            }
        }
        
        dto.setMissionName(mission.getMissionName());
        dto.setDescription(mission.getDescription());
        dto.setAircraftCount(mission.getAircraftCount());
        dto.setStartTime(mission.getStartTime());
        dto.setEndTime(mission.getEndTime());
        dto.setMissionType(mission.getMissionType().toString());
        dto.setStatus(mission.getStatus().toString());
        return dto;
    }
} 