package com.example.demo.factory;

import com.example.demo.dto.MissionDTO;
import com.example.demo.entity.Mission;
import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.repository.UnifiedAircraftRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MissionDTOFactory {
    private final UnifiedAircraftRepository aircraftRepository;
    
    public MissionDTOFactory(UnifiedAircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }
    
    @Transactional(readOnly = true)
    public MissionDTO createDTO(Mission mission) {
        UnifiedAircraft aircraft = aircraftRepository.findById(mission.getAircraftId()).orElse(null);
        return MissionDTO.create(mission, aircraft);
    }
} 