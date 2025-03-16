package com.example.demo.service.impl;

import com.example.demo.entity.Aircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.dto.MissionRequest;
import com.example.demo.repository.AircraftRepository;
import com.example.demo.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AircraftServiceImpl implements AircraftService {
    
    @Autowired
    private AircraftRepository<Aircraft> aircraftRepository;
    
    @Override
    public Aircraft save(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }
    
    @Override
    public Aircraft findById(Long id) {
        return aircraftRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Aircraft> findAll() {
        return aircraftRepository.findAll();
    }
    
    @Override
    public List<Aircraft> findByType(AircraftType type) {
        return aircraftRepository.findByType(type);
    }
    
    @Override
    public List<Aircraft> findByMissionStatus(MissionStatus status) {
        return aircraftRepository.findByMissionStatus(status);
    }
    
    @Override
    public void deleteById(Long id) {
        aircraftRepository.deleteById(id);
    }
    
    @Override
    public Aircraft updateMissionStatus(Long id, MissionStatus status) {
        Aircraft aircraft = findById(id);
        if (aircraft != null) {
            aircraft.setMissionStatus(status);
            return aircraftRepository.save(aircraft);
        }
        return null;
    }
    
    @Override
    public Aircraft update(Long id, Aircraft updatedAircraft) {
        Aircraft aircraft = findById(id);
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
            return aircraftRepository.save(aircraft);
        }
        return null;
    }
    
    @Override
    @Transactional
    public String assignMission(MissionRequest request) {
        List<Aircraft> aircrafts = aircraftRepository.findByName(request.getName());
        if (aircrafts.isEmpty()) {
            throw new RuntimeException("未找到该飞机");
        }
        
        Aircraft aircraft = aircrafts.get(0);
        if (aircraft.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("飞机数量不足");
        }
        
        aircraft.setMissionStatus(MissionStatus.IN_MISSION);
        aircraftRepository.save(aircraft);
        
        return String.format("派出了%d架%s%s进行战斗任务执行", 
            request.getQuantity(), 
            aircraft.getName(), 
            aircraft.getType().toString());
    }
} 