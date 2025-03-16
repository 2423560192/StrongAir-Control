package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class AircraftStatsDTO {
    // 飞机统计
    private Integer totalAircraft;
    private Integer availableAircraft;
    private Integer inMissionAircraft;
    
    // 飞机类型分布
    private Integer fighterCount;
    private Integer transportCount;
    private Integer reconCount;
    
    // 任务统计
    private Integer totalMissions;
    private Integer activeMissions;
    private Integer completedMissions;
    
    // 飞行时长统计
    private Integer totalFlightHours;
    private Integer monthlyFlightHours;
    
    // 任务完成率
    private Double missionSuccessRate;
    private Double monthlySuccessRate;
    
    // 任务类型分布
    private List<MissionTypeStats> missionTypeDistribution;
    
    // 任务趋势
    private TrendStats missionTrend;
    
    @Data
    public static class MissionTypeStats {
        private String name;
        private Integer value;
    }
    
    @Data
    public static class TrendStats {
        private List<DateCount> week;
        private List<DateCount> month;
        private List<DateCount> year;
    }
    
    @Data
    public static class DateCount {
        private String date;
        private Integer count;
    }
} 