package com.example.demo.dto;

import com.example.demo.entity.Aircraft;
import com.example.demo.entity.Fighter;
import com.example.demo.entity.Transport;
import com.example.demo.entity.Reconnaissance;
import lombok.Data;

@Data
public class AircraftDTO {
    // 基本属性
    private Long id;
    private String name;
    private String type;
    private Integer quantity;
    private Integer altitude;
    private Double speed;
    private String stealth;
    private String radarModel;
    private String missionStatus;
    
    // 战斗机特有属性
    private Integer weaponCapacity;
    private String weaponTypes;
    private Integer combatRange;
    
    // 运输机特有属性
    private Double cargoCapacity;
    private Double cargoSpace;
    private Integer maxRange;
    
    // 侦查机特有属性
    private Integer reconRange;
    private String sensorTypes;
    private Integer endurance;

    public static AircraftDTO fromEntity(Aircraft aircraft) {
        AircraftDTO dto = new AircraftDTO();
        // 设置基本属性
        dto.setId(aircraft.getId());
        dto.setName(aircraft.getName());
        dto.setType(aircraft.getType().toString());
        dto.setQuantity(aircraft.getQuantity());
        dto.setAltitude(aircraft.getFlightAltitude().intValue());
        dto.setSpeed(aircraft.getFlightSpeed());
        dto.setStealth(convertStealthLevelToString(aircraft.getStealthLevel()));
        dto.setRadarModel(aircraft.getRadarModel());
        dto.setMissionStatus(aircraft.getMissionStatus().toString());
        
        // 根据飞机类型设置特有属性
        if (aircraft instanceof Fighter) {
            Fighter fighter = (Fighter) aircraft;
            dto.setWeaponCapacity(fighter.getWeaponCapacity());
            dto.setWeaponTypes(fighter.getWeaponTypes());
            dto.setCombatRange(fighter.getCombatRange());
            
            // 对于战斗机，明确设置其他类型的属性为空字符串或0，而不是null
            dto.setCargoCapacity(0.0);
            dto.setCargoSpace(0.0);
            dto.setMaxRange(0);
            dto.setReconRange(0);
            dto.setSensorTypes("");
            dto.setEndurance(0);
        } else if (aircraft instanceof Transport) {
            Transport transport = (Transport) aircraft;
            dto.setCargoCapacity(transport.getCargoCapacity());
            dto.setCargoSpace(transport.getCargoSpace());
            dto.setMaxRange(transport.getMaxRange());
            
            // 对于运输机，设置其他类型的属性为默认值
            dto.setWeaponCapacity(0);
            dto.setWeaponTypes("");
            dto.setCombatRange(0);
            dto.setReconRange(0);
            dto.setSensorTypes("");
            dto.setEndurance(0);
        } else if (aircraft instanceof Reconnaissance) {
            Reconnaissance recon = (Reconnaissance) aircraft;
            dto.setReconRange(recon.getReconRange());
            dto.setSensorTypes(recon.getSensorTypes());
            dto.setEndurance(recon.getEndurance());
            
            // 对于侦查机，设置其他类型的属性为默认值
            dto.setWeaponCapacity(0);
            dto.setWeaponTypes("");
            dto.setCombatRange(0);
            dto.setCargoCapacity(0.0);
            dto.setCargoSpace(0.0);
            dto.setMaxRange(0);
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