package com.example.demo.dto;

import lombok.Data;

@Data
public class MissionRequest {
    private String name;        // 飞机名称
    private Integer quantity;   // 派遣数量
    private String missionName; // 任务名称
    private String description; // 任务描述
    private String missionType; // 任务类型
} 