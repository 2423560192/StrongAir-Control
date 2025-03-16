package com.example.demo.dto;

import com.example.demo.entity.Aircraft;
import com.example.demo.entity.Fighter;
import lombok.Data;

@Data
public class AircraftDTO {
    private Long id;
    private String name;
    private String type;
    private Integer quantity;
    private Integer altitude;
    private Double speed;
    private String stealth;
    private String radarModel;
    private Integer payload;

    public static AircraftDTO fromEntity(Aircraft aircraft) {
        AircraftDTO dto = new AircraftDTO();
        dto.setId(aircraft.getId());
        dto.setName(aircraft.getName());
        dto.setType(aircraft.getType().toString());
        dto.setQuantity(aircraft.getQuantity());
        dto.setAltitude(aircraft.getFlightAltitude().intValue());
        dto.setSpeed(aircraft.getFlightSpeed());
        dto.setStealth(convertStealthLevelToString(aircraft.getStealthLevel()));
        dto.setRadarModel(aircraft.getRadarModel());
        
        if (aircraft instanceof Fighter) {
            dto.setPayload(((Fighter) aircraft).getWeaponCapacity());
        }
        
        return dto;
    }
    
    private static String convertStealthLevelToString(Integer level) {
        if (level >= 9) return "优秀";
        if (level >= 7) return "良好";
        if (level >= 5) return "一般";
        return "较差";
    }
} 