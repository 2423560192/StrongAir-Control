package com.example.demo.service.impl;

import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.entity.Mission;
import com.example.demo.entity.MissionType;
import com.example.demo.dto.MissionRequest;
import com.example.demo.repository.UnifiedAircraftRepository;
import com.example.demo.repository.MissionRepository;
import com.example.demo.service.UnifiedAircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UnifiedAircraftServiceImpl implements UnifiedAircraftService {
    
    @Autowired
    private UnifiedAircraftRepository aircraftRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Override
    public UnifiedAircraft save(UnifiedAircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }
    
    @Override
    public UnifiedAircraft findById(Long id) {
        return aircraftRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<UnifiedAircraft> findAll() {
        return aircraftRepository.findAll();
    }
    
    @Override
    public List<UnifiedAircraft> findByType(AircraftType type) {
        return aircraftRepository.findByType(type);
    }
    
    @Override
    public List<UnifiedAircraft> findByMissionStatus(MissionStatus status) {
        return aircraftRepository.findByMissionStatus(status);
    }
    
    @Override
    public void deleteById(Long id) {
        aircraftRepository.deleteById(id);
    }
    
    @Override
    public UnifiedAircraft updateMissionStatus(Long id, MissionStatus status) {
        UnifiedAircraft aircraft = findById(id);
        if (aircraft != null) {
            aircraft.setMissionStatus(status);
            return aircraftRepository.save(aircraft);
        }
        return null;
    }
    
    @Override
    public UnifiedAircraft update(Long id, UnifiedAircraft updatedAircraft) {
        UnifiedAircraft aircraft = findById(id);
        if (aircraft != null) {
            // 只更新非空字段
            if (updatedAircraft.getName() != null) {
                aircraft.setName(updatedAircraft.getName());
            }
            if (updatedAircraft.getType() != null) {
                aircraft.setType(updatedAircraft.getType());
            }
            if (updatedAircraft.getQuantity() != null) {
                aircraft.setQuantity(updatedAircraft.getQuantity());
            }
            if (updatedAircraft.getFlightAltitude() != null) {
                aircraft.setFlightAltitude(updatedAircraft.getFlightAltitude());
            }
            if (updatedAircraft.getFlightSpeed() != null) {
                aircraft.setFlightSpeed(updatedAircraft.getFlightSpeed());
            }
            if (updatedAircraft.getStealthLevel() != null) {
                aircraft.setStealthLevel(updatedAircraft.getStealthLevel());
            }
            if (updatedAircraft.getRadarModel() != null) {
                aircraft.setRadarModel(updatedAircraft.getRadarModel());
            }
            
            // 更新战斗机特有属性
            if (updatedAircraft.getWeaponCapacity() != null) {
                aircraft.setWeaponCapacity(updatedAircraft.getWeaponCapacity());
            }
            if (updatedAircraft.getWeaponTypes() != null) {
                aircraft.setWeaponTypes(updatedAircraft.getWeaponTypes());
            }
            if (updatedAircraft.getCombatRange() != null) {
                aircraft.setCombatRange(updatedAircraft.getCombatRange());
            }
            
            // 更新运输机特有属性
            if (updatedAircraft.getCargoCapacity() != null) {
                aircraft.setCargoCapacity(updatedAircraft.getCargoCapacity());
            }
            if (updatedAircraft.getCargoSpace() != null) {
                aircraft.setCargoSpace(updatedAircraft.getCargoSpace());
            }
            if (updatedAircraft.getMaxRange() != null) {
                aircraft.setMaxRange(updatedAircraft.getMaxRange());
            }
            
            // 更新侦查机特有属性
            if (updatedAircraft.getReconRange() != null) {
                aircraft.setReconRange(updatedAircraft.getReconRange());
            }
            if (updatedAircraft.getSensorTypes() != null) {
                aircraft.setSensorTypes(updatedAircraft.getSensorTypes());
            }
            if (updatedAircraft.getEndurance() != null) {
                aircraft.setEndurance(updatedAircraft.getEndurance());
            }
            
            return aircraftRepository.save(aircraft);
        }
        return null;
    }
    
    @Override
    @Transactional
    public Mission assignMission(MissionRequest request) {
        List<UnifiedAircraft> aircrafts = aircraftRepository.findByName(request.getName());
        if (aircrafts.isEmpty()) {
            throw new RuntimeException("未找到该飞机");
        }
        
        UnifiedAircraft aircraft = aircrafts.get(0);
        if (aircraft.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("飞机数量不足");
        }
        
        // 更新飞机状态
        aircraft.setMissionStatus(MissionStatus.IN_PROGRESS);
        aircraftRepository.save(aircraft);
        
        // 创建任务记录
        Mission mission = new Mission();
        mission.setAircraftId(aircraft.getId());
        mission.setMissionName(request.getMissionName());
        mission.setDescription(request.getDescription());
        mission.setAircraftCount(request.getQuantity());
        mission.setStartTime(LocalDateTime.now());
        mission.setMissionType(MissionType.valueOf(request.getMissionType()));
        mission.setStatus(MissionStatus.IN_PROGRESS);
        
        return missionRepository.save(mission);
    }
    
    @Override
    public List<Mission> getAircraftMissions(Long aircraftId) {
        return missionRepository.findByAircraftId(aircraftId);
    }
    
    @Override
    @Transactional
    public Mission recallMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
            .orElseThrow(() -> new RuntimeException("未找到该任务"));
        
        if (mission.getStatus() != MissionStatus.IN_PROGRESS) {
            throw new RuntimeException("只能召回进行中的任务");
        }
        
        // 更新任务状态
        mission.setStatus(MissionStatus.COMPLETED);
        mission.setEndTime(LocalDateTime.now());
        
        // 更新飞机状态
        UnifiedAircraft aircraft = aircraftRepository.findById(mission.getAircraftId())
            .orElseThrow(() -> new RuntimeException("未找到该飞机"));
        aircraft.setMissionStatus(MissionStatus.COMPLETED);
        aircraftRepository.save(aircraft);
        
        return missionRepository.save(mission);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Mission getCurrentMission(Long id) {
        List<Mission> missions = missionRepository.findByAircraftId(id);
        return missions.stream()
            .filter(m -> m.getStatus() == MissionStatus.IN_PROGRESS)
            .findFirst()
            .orElse(null);
    }
} 