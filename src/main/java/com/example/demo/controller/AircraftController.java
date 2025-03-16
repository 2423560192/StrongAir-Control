package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Aircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.entity.Fighter;
import com.example.demo.entity.Transport;
import com.example.demo.entity.Reconnaissance;
import com.example.demo.dto.MissionRequest;
import com.example.demo.dto.AircraftDTO;
import com.example.demo.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    
    @Autowired
    private AircraftService aircraftService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<AircraftDTO> save(@RequestBody Map<String, Object> request) {
        try {
            Aircraft aircraft = aircraftService.save(createAircraftFromRequest(request));
            return Result.success(AircraftDTO.fromEntity(aircraft));
        } catch (Exception e) {
            return Result.error(400, "保存飞机信息失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public Result<AircraftDTO> findById(@PathVariable Long id) {
        Aircraft aircraft = aircraftService.findById(id);
        if (aircraft == null) {
            return Result.error(404, "未找到该飞机");
        }
        return Result.success(AircraftDTO.fromEntity(aircraft));
    }
    
    @GetMapping
    public Result<List<AircraftDTO>> findAll() {
        List<Aircraft> aircrafts = aircraftService.findAll();
        List<AircraftDTO> dtos = aircrafts.stream()
            .map(AircraftDTO::fromEntity)
            .toList();
        return Result.success(dtos);
    }
    
    @GetMapping("/type/{type}")
    public Result<List<Aircraft>> findByType(@PathVariable AircraftType type) {
        List<Aircraft> aircrafts = aircraftService.findByType(type);
        return Result.success(aircrafts);
    }
    
    @GetMapping("/status/{status}")
    public Result<List<Aircraft>> findByMissionStatus(@PathVariable MissionStatus status) {
        List<Aircraft> aircrafts = aircraftService.findByMissionStatus(status);
        return Result.success(aircrafts);
    }
    
    @PutMapping("/{id}")
    public Result<Aircraft> update(@PathVariable Long id, @RequestBody Aircraft aircraft) {
        Aircraft updated = aircraftService.update(id, aircraft);
        if (updated == null) {
            return Result.error(404, "未找到该飞机");
        }
        return Result.success(updated);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        try {
            aircraftService.deleteById(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(404, "删除失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/missions")
    public Result<Map<String, Object>> getMissionStatus(@PathVariable Long id) {
        try {
            Aircraft aircraft = aircraftService.findById(id);
            if (aircraft == null) {
                return Result.error(404, "未找到该飞机");
            }
            
            Map<String, Object> missionInfo = new HashMap<>();
            missionInfo.put("aircraftId", aircraft.getId());
            missionInfo.put("aircraftName", aircraft.getName());
            missionInfo.put("missionStatus", aircraft.getMissionStatus().toString());
            missionInfo.put("quantity", aircraft.getQuantity());
            
            return Result.success(missionInfo);
        } catch (Exception e) {
            return Result.error(500, "获取任务状态失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/mission")
    public Result<Map<String, String>> assignMission(@RequestBody MissionRequest request) {
        try {
            String message = aircraftService.assignMission(request);
            return Result.success(Map.of("message", message));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
    
    // 将创建飞机的逻辑抽取为单独的方法
    private Aircraft createAircraftFromRequest(Map<String, Object> request) {
        // 原来的创建飞机逻辑
        Aircraft aircraft;
        String type = (String) request.get("type");
        
        switch (AircraftType.valueOf(type)) {
            case 战斗机:
                aircraft = new Fighter();
                break;
            case 运输机:
                aircraft = new Transport();
                break;
            case 侦查机:
                aircraft = new Reconnaissance();
                break;
            default:
                throw new IllegalArgumentException("未知的飞机类型");
        }
        
        // 设置通用属性
        aircraft.setName((String) request.get("name"));
        aircraft.setType(AircraftType.valueOf(type));
        aircraft.setQuantity((Integer) request.get("quantity"));
        aircraft.setFlightAltitude(Double.valueOf(request.getOrDefault("altitude", 
            request.get("flightAltitude")).toString()));
        aircraft.setFlightSpeed(Double.valueOf(request.getOrDefault("speed", 
            request.get("flightSpeed")).toString()));
        aircraft.setStealthLevel(request.get("stealth") != null ? 
            parseStealthLevel((String) request.get("stealth")) : 
            (Integer) request.getOrDefault("stealthLevel", 5));
        aircraft.setRadarModel((String) request.get("radarModel"));
        
        // 设置特定属性
        if (aircraft instanceof Fighter) {
            Fighter fighter = (Fighter) aircraft;
            fighter.setWeaponCapacity((Integer) request.getOrDefault("payload", 
                request.getOrDefault("weaponCapacity", 30)));
            fighter.setWeaponTypes((String) request.getOrDefault("weaponTypes", "标准武器"));
            fighter.setCombatRange((Integer) request.getOrDefault("combatRange", 1000));
        } else if (aircraft instanceof Transport) {
            Transport transport = (Transport) aircraft;
            transport.setCargoCapacity(Double.valueOf(request.getOrDefault("cargoCapacity", 50.0).toString()));
            transport.setCargoSpace(Double.valueOf(request.getOrDefault("cargoSpace", 100.0).toString()));
            transport.setMaxRange((Integer) request.getOrDefault("maxRange", 3000));
        } else if (aircraft instanceof Reconnaissance) {
            Reconnaissance reconnaissance = (Reconnaissance) aircraft;
            reconnaissance.setReconRange((Integer) request.getOrDefault("reconRange", 2000));
            reconnaissance.setSensorTypes((String) request.getOrDefault("sensorTypes", "标准传感器"));
            reconnaissance.setEndurance((Integer) request.getOrDefault("endurance", 10));
        }
        
        return aircraft;
    }
    
    // 添加一个辅助方法来解析隐身等级
    private Integer parseStealthLevel(String stealth) {
        switch (stealth.toLowerCase()) {
            case "优秀":
                return 9;
            case "良好":
                return 7;
            case "一般":
                return 5;
            case "较差":
                return 3;
            default:
                return 5;
        }
    }
} 