package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reconnaissance")
public class Reconnaissance extends Aircraft {
    private Integer reconRange;        // 侦查范围（公里）
    private String sensorTypes;        // 传感器类型
    private Integer endurance;         // 续航时间（小时）
} 