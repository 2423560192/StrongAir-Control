package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "unified_aircraft")
public class UnifiedAircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 基本属性
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
    private MissionStatus missionStatus = MissionStatus.COMPLETED;
    
    // 战斗机特有属性
    private Integer weaponCapacity;    // 载弹量
    private String weaponTypes;        // 武器类型
    private Integer combatRange;       // 作战半径（公里）
    
    // 运输机特有属性
    private Double cargoCapacity;     // 载货量（吨）
    private Double cargoSpace;        // 货舱容积（立方米）
    private Integer maxRange;         // 最大航程（公里）
    
    // 侦查机特有属性
    private Integer reconRange;        // 侦查范围（公里）
    private String sensorTypes;        // 传感器类型
    private Integer endurance;         // 续航时间（小时）
} 