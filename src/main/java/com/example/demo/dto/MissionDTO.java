package com.example.demo.dto;

import com.example.demo.entity.Mission;
import com.example.demo.entity.UnifiedAircraft;
import lombok.Data;
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
    
    public static MissionDTO create(Mission mission, UnifiedAircraft aircraft) {
        MissionDTO dto = new MissionDTO();
        dto.setId(mission.getId());
        dto.setAircraftId(mission.getAircraftId());
        
        if (aircraft != null) {
            dto.setAircraftName(aircraft.getName());
            dto.setAircraftType(aircraft.getType().toString());
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