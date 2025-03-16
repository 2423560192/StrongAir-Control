package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transport")
public class Transport extends Aircraft {
    private Double cargoCapacity;     // 载货量（吨）
    private Double cargoSpace;        // 货舱容积（立方米）
    private Integer maxRange;         // 最大航程（公里）
} 