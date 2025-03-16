package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "fighter")
public class Fighter extends Aircraft {
    private Integer weaponCapacity;    // 载弹量
    private String weaponTypes;        // 武器类型
    private Integer combatRange;       // 作战半径（公里）
} 