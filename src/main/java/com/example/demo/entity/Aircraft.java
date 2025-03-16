package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "aircraft")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AircraftType type;
    
    private Integer quantity;
    private Double flightAltitude;  // 单位：米
    private Double flightSpeed;     // 单位：马赫
    private Integer stealthLevel;   // 隐身等级：1-10
    private String radarModel;      // 雷达型号
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MissionStatus missionStatus = MissionStatus.STANDBY;
} 