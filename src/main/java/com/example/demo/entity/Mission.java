package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long aircraftId;
    
    private String missionName;
    private String description;
    private Integer aircraftCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MissionType missionType;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MissionStatus status = MissionStatus.IN_PROGRESS;
} 