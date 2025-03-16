package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.dto.MissionRequest;
import com.example.demo.dto.MissionDTO;
import com.example.demo.entity.Mission;
import com.example.demo.service.UnifiedAircraftService;
import com.example.demo.factory.MissionDTOFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@RestController
@RequestMapping("/unified-aircraft")
public class UnifiedAircraftController {
    
    @Autowired
    private UnifiedAircraftService aircraftService;
    
    @Autowired
    private MissionDTOFactory missionDTOFactory;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<UnifiedAircraft> save(@RequestBody Map<String, Object> request) {
        try {
            UnifiedAircraft aircraft = createAircraftFromRequest(request);
            return Result.success(aircraftService.save(aircraft));
        } catch (Exception e) {
            return Result.error(400, "保存飞机信息失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public Result<UnifiedAircraft> findById(@PathVariable Long id) {
        UnifiedAircraft aircraft = aircraftService.findById(id);
        if (aircraft == null) {
            return Result.error(404, "未找到该飞机");
        }
        return Result.success(aircraft);
    }
    
    @GetMapping
    public Result<List<UnifiedAircraft>> findAll() {
        List<UnifiedAircraft> aircrafts = aircraftService.findAll();
        return Result.success(aircrafts);
    }
    
    @GetMapping("/type/{type}")
    public Result<List<UnifiedAircraft>> findByType(@PathVariable AircraftType type) {
        List<UnifiedAircraft> aircrafts = aircraftService.findByType(type);
        return Result.success(aircrafts);
    }
    
    @GetMapping("/status/{status}")
    public Result<List<UnifiedAircraft>> findByMissionStatus(@PathVariable MissionStatus status) {
        List<UnifiedAircraft> aircrafts = aircraftService.findByMissionStatus(status);
        return Result.success(aircrafts);
    }
    
    @PutMapping("/{id}")
    public Result<UnifiedAircraft> update(@PathVariable Long id, @RequestBody UnifiedAircraft aircraft) {
        UnifiedAircraft updated = aircraftService.update(id, aircraft);
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
    public Result<List<MissionDTO>> getAircraftMissions(@PathVariable Long id) {
        try {
            UnifiedAircraft aircraft = aircraftService.findById(id);
            if (aircraft == null) {
                return Result.error(404, "未找到该飞机");
            }
            
            List<Mission> missions = aircraftService.getAircraftMissions(id);
            List<MissionDTO> missionDTOs = missions.stream()
                .map(mission -> missionDTOFactory.createDTO(mission))
                .collect(Collectors.toList());
            
            return Result.success(missionDTOs);
        } catch (Exception e) {
            return Result.error(500, "获取任务列表失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/mission")
    public Result<MissionDTO> assignMission(@RequestBody MissionRequest request) {
        try {
            Mission mission = aircraftService.assignMission(request);
            return Result.success(missionDTOFactory.createDTO(mission));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
    
    @PostMapping("/mission/{id}/recall")
    public Result<MissionDTO> recallMission(@PathVariable Long id) {
        try {
            Mission mission = aircraftService.recallMission(id);
            return Result.success(missionDTOFactory.createDTO(mission));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
    
    @GetMapping("/{id}/status")
    public Result<Map<String, Object>> getAircraftStatus(@PathVariable Long id) {
        try {
            UnifiedAircraft aircraft = aircraftService.findById(id);
            if (aircraft == null) {
                return Result.error(404, "未找到该飞机");
            }
            
            Mission currentMission = aircraftService.getCurrentMission(id);
            
            Map<String, Object> status = new HashMap<>();
            status.put("aircraftId", aircraft.getId());
            status.put("name", aircraft.getName());
            status.put("missionStatus", aircraft.getMissionStatus());
            status.put("currentMission", currentMission != null ? 
                missionDTOFactory.createDTO(currentMission) : null);
            
            return Result.success(status);
        } catch (Exception e) {
            return Result.error(500, "获取飞机状态失败：" + e.getMessage());
        }
    }
    
    // 将创建飞机的逻辑抽取为单独的方法
    private UnifiedAircraft createAircraftFromRequest(Map<String, Object> request) {
        UnifiedAircraft aircraft = new UnifiedAircraft();
        
        // 设置基本属性
        aircraft.setName((String) request.get("name"));
        aircraft.setType(AircraftType.valueOf((String) request.get("type")));
        aircraft.setQuantity((Integer) request.getOrDefault("quantity", 1));
        aircraft.setFlightAltitude(Double.valueOf(request.getOrDefault("altitude", 
            request.getOrDefault("flightAltitude", 10000)).toString()));
        aircraft.setFlightSpeed(Double.valueOf(request.getOrDefault("speed", 
            request.getOrDefault("flightSpeed", 1.0)).toString()));
        
        // 处理隐身等级
        if (request.containsKey("stealth")) {
            String stealthStr = (String) request.get("stealth");
            aircraft.setStealthLevel(parseStealthLevel(stealthStr));
        } else {
            aircraft.setStealthLevel((Integer) request.getOrDefault("stealthLevel", 5));
        }
        
        aircraft.setRadarModel((String) request.getOrDefault("radarModel", "标准雷达"));
        aircraft.setMissionStatus(MissionStatus.COMPLETED);
        
        // 设置战斗机特有属性
        aircraft.setWeaponCapacity((Integer) request.getOrDefault("weaponCapacity", 
            request.getOrDefault("payload", 0)));
        aircraft.setWeaponTypes((String) request.getOrDefault("weaponTypes", ""));
        aircraft.setCombatRange((Integer) request.getOrDefault("combatRange", 0));
        
        // 设置运输机特有属性
        aircraft.setCargoCapacity(Double.valueOf(request.getOrDefault("cargoCapacity", 0).toString()));
        aircraft.setCargoSpace(Double.valueOf(request.getOrDefault("cargoSpace", 0).toString()));
        aircraft.setMaxRange((Integer) request.getOrDefault("maxRange", 0));
        
        // 设置侦查机特有属性
        aircraft.setReconRange((Integer) request.getOrDefault("reconRange", 0));
        aircraft.setSensorTypes((String) request.getOrDefault("sensorTypes", ""));
        aircraft.setEndurance((Integer) request.getOrDefault("endurance", 0));
        
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