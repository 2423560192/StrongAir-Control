package com.example.demo.service.impl;

import com.example.demo.entity.UnifiedAircraft;
import com.example.demo.entity.AircraftType;
import com.example.demo.entity.MissionStatus;
import com.example.demo.entity.Mission;
import com.example.demo.entity.MissionType;
import com.example.demo.dto.MissionRequest;
import com.example.demo.dto.AircraftStatsDTO;
import com.example.demo.repository.UnifiedAircraftRepository;
import com.example.demo.repository.MissionRepository;
import com.example.demo.service.UnifiedAircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnifiedAircraftServiceImpl implements UnifiedAircraftService {
    
    @Autowired
    private UnifiedAircraftRepository aircraftRepository;
    
    @Autowired
    private MissionRepository missionRepository;
    
    @Override
    @CacheEvict(value = "aircraftStats", allEntries = true)
    public UnifiedAircraft save(UnifiedAircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }
    
    @Override
    public UnifiedAircraft findById(Long id) {
        return aircraftRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<UnifiedAircraft> findAll() {
        return aircraftRepository.findAll();
    }
    
    @Override
    public List<UnifiedAircraft> findByType(AircraftType type) {
        return aircraftRepository.findByType(type);
    }
    
    @Override
    public List<UnifiedAircraft> findByMissionStatus(MissionStatus status) {
        return aircraftRepository.findByMissionStatus(status);
    }
    
    @Override
    @CacheEvict(value = "aircraftStats", allEntries = true)
    public void deleteById(Long id) {
        aircraftRepository.deleteById(id);
    }
    
    @Override
    public UnifiedAircraft updateMissionStatus(Long id, MissionStatus status) {
        UnifiedAircraft aircraft = findById(id);
        if (aircraft != null) {
            aircraft.setMissionStatus(status);
            return aircraftRepository.save(aircraft);
        }
        return null;
    }
    
    @Override
    @CacheEvict(value = "aircraftStats", allEntries = true)
    public UnifiedAircraft update(Long id, UnifiedAircraft updatedAircraft) {
        UnifiedAircraft aircraft = findById(id);
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
            
            // 更新战斗机特有属性
            if (updatedAircraft.getWeaponCapacity() != null) {
                aircraft.setWeaponCapacity(updatedAircraft.getWeaponCapacity());
            }
            if (updatedAircraft.getWeaponTypes() != null) {
                aircraft.setWeaponTypes(updatedAircraft.getWeaponTypes());
            }
            if (updatedAircraft.getCombatRange() != null) {
                aircraft.setCombatRange(updatedAircraft.getCombatRange());
            }
            
            // 更新运输机特有属性
            if (updatedAircraft.getCargoCapacity() != null) {
                aircraft.setCargoCapacity(updatedAircraft.getCargoCapacity());
            }
            if (updatedAircraft.getCargoSpace() != null) {
                aircraft.setCargoSpace(updatedAircraft.getCargoSpace());
            }
            if (updatedAircraft.getMaxRange() != null) {
                aircraft.setMaxRange(updatedAircraft.getMaxRange());
            }
            
            // 更新侦查机特有属性
            if (updatedAircraft.getReconRange() != null) {
                aircraft.setReconRange(updatedAircraft.getReconRange());
            }
            if (updatedAircraft.getSensorTypes() != null) {
                aircraft.setSensorTypes(updatedAircraft.getSensorTypes());
            }
            if (updatedAircraft.getEndurance() != null) {
                aircraft.setEndurance(updatedAircraft.getEndurance());
            }
            
            return aircraftRepository.save(aircraft);
        }
        return null;
    }
    
    @Override
    @CacheEvict(value = "aircraftStats", allEntries = true)
    public Mission assignMission(MissionRequest request) {
        List<UnifiedAircraft> aircrafts = aircraftRepository.findByName(request.getName());
        if (aircrafts.isEmpty()) {
            throw new RuntimeException("未找到该飞机");
        }
        
        UnifiedAircraft aircraft = aircrafts.get(0);
        if (aircraft.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("飞机数量不足");
        }
        
        // 更新飞机状态
        aircraft.setMissionStatus(MissionStatus.IN_PROGRESS);
        aircraftRepository.save(aircraft);
        
        // 创建任务记录
        Mission mission = new Mission();
        mission.setAircraftId(aircraft.getId());
        mission.setMissionName(request.getMissionName());
        mission.setDescription(request.getDescription());
        mission.setAircraftCount(request.getQuantity());
        mission.setStartTime(LocalDateTime.now());
        mission.setMissionType(MissionType.valueOf(request.getMissionType()));
        mission.setStatus(MissionStatus.IN_PROGRESS);
        
        return missionRepository.save(mission);
    }
    
    @Override
    public List<Mission> getAircraftMissions(Long aircraftId) {
        return missionRepository.findByAircraftId(aircraftId);
    }
    
    @Override
    @CacheEvict(value = "aircraftStats", allEntries = true)
    public Mission recallMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
            .orElseThrow(() -> new RuntimeException("未找到该任务"));
        
        if (mission.getStatus() != MissionStatus.IN_PROGRESS) {
            throw new RuntimeException("只能召回进行中的任务");
        }
        
        // 更新任务状态
        mission.setStatus(MissionStatus.COMPLETED);
        mission.setEndTime(LocalDateTime.now());
        
        // 更新飞机状态
        UnifiedAircraft aircraft = aircraftRepository.findById(mission.getAircraftId())
            .orElseThrow(() -> new RuntimeException("未找到该飞机"));
        aircraft.setMissionStatus(MissionStatus.COMPLETED);
        aircraftRepository.save(aircraft);
        
        return missionRepository.save(mission);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Mission getCurrentMission(Long id) {
        List<Mission> missions = missionRepository.findByAircraftId(id);
        return missions.stream()
            .filter(m -> m.getStatus() == MissionStatus.IN_PROGRESS)
            .findFirst()
            .orElse(null);
    }
    
    @Override
    @Cacheable(value = "aircraftStats")
    public AircraftStatsDTO getStats() {
        AircraftStatsDTO stats = new AircraftStatsDTO();
        
        // 获取所有飞机和任务数据
        List<UnifiedAircraft> allAircraft = aircraftRepository.findAll();
        List<Mission> allMissions = missionRepository.findAll();
        
        // 计算飞机统计数据
        stats.setTotalAircraft(allAircraft.size());
        stats.setInMissionAircraft((int) allAircraft.stream()
            .filter(a -> a.getMissionStatus() == MissionStatus.IN_PROGRESS)
            .count());
        stats.setAvailableAircraft(stats.getTotalAircraft() - stats.getInMissionAircraft());
        
        // 计算飞机类型分布
        stats.setFighterCount((int) allAircraft.stream()
            .filter(a -> a.getType() == AircraftType.战斗机)
            .count());
        stats.setTransportCount((int) allAircraft.stream()
            .filter(a -> a.getType() == AircraftType.运输机)
            .count());
        stats.setReconCount((int) allAircraft.stream()
            .filter(a -> a.getType() == AircraftType.侦查机)
            .count());
        
        // 计算任务统计数据
        stats.setTotalMissions(allMissions.size());
        stats.setActiveMissions((int) allMissions.stream()
            .filter(m -> m.getStatus() == MissionStatus.IN_PROGRESS)
            .count());
        stats.setCompletedMissions(stats.getTotalMissions() - stats.getActiveMissions());
        
        // 计算任务完成率
        if (stats.getTotalMissions() > 0) {
            stats.setMissionSuccessRate((double) stats.getCompletedMissions() / stats.getTotalMissions() * 100);
        }
        
        // 计算本月任务完成率
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        List<Mission> monthlyMissions = allMissions.stream()
            .filter(m -> m.getStartTime().isAfter(monthStart))
            .collect(Collectors.toList());
        
        if (!monthlyMissions.isEmpty()) {
            long monthlyCompleted = monthlyMissions.stream()
                .filter(m -> m.getStatus() == MissionStatus.COMPLETED)
                .count();
            stats.setMonthlySuccessRate((double) monthlyCompleted / monthlyMissions.size() * 100);
        }
        
        // 计算飞行时长
        stats.setTotalFlightHours(calculateTotalFlightHours(allMissions));
        stats.setMonthlyFlightHours(calculateTotalFlightHours(monthlyMissions));
        
        // 设置任务类型分布
        stats.setMissionTypeDistribution(calculateMissionTypeDistribution(allMissions));
        
        // 设置任务趋势
        stats.setMissionTrend(calculateMissionTrend(allMissions));
        
        return stats;
    }
    
    private Integer calculateTotalFlightHours(List<Mission> missions) {
        return missions.stream()
            .filter(m -> m.getEndTime() != null)
            .mapToInt(m -> (int) ChronoUnit.HOURS.between(m.getStartTime(), m.getEndTime()))
            .sum();
    }
    
    private List<AircraftStatsDTO.MissionTypeStats> calculateMissionTypeDistribution(List<Mission> missions) {
        Map<MissionType, Long> distribution = missions.stream()
            .collect(Collectors.groupingBy(Mission::getMissionType, Collectors.counting()));
        
        return distribution.entrySet().stream()
            .map(entry -> {
                AircraftStatsDTO.MissionTypeStats stats = new AircraftStatsDTO.MissionTypeStats();
                stats.setName(entry.getKey().toString());
                stats.setValue(entry.getValue().intValue());
                return stats;
            })
            .collect(Collectors.toList());
    }
    
    private AircraftStatsDTO.TrendStats calculateMissionTrend(List<Mission> missions) {
        AircraftStatsDTO.TrendStats trend = new AircraftStatsDTO.TrendStats();
        
        // 计算周趋势
        LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
        trend.setWeek(calculateDateTrend(missions, weekStart, ChronoUnit.DAYS));
        
        // 计算月趋势
        LocalDateTime monthStart = LocalDateTime.now().minusMonths(1);
        trend.setMonth(calculateDateTrend(missions, monthStart, ChronoUnit.DAYS));
        
        // 计算年趋势
        LocalDateTime yearStart = LocalDateTime.now().minusYears(1);
        trend.setYear(calculateDateTrend(missions, yearStart, ChronoUnit.MONTHS));
        
        return trend;
    }
    
    private List<AircraftStatsDTO.DateCount> calculateDateTrend(
        List<Mission> missions, LocalDateTime start, ChronoUnit unit) {
        
        return missions.stream()
            .filter(m -> m.getStartTime().isAfter(start))
            .collect(Collectors.groupingBy(
                m -> formatDate(m.getStartTime(), unit),
                Collectors.counting()))
            .entrySet().stream()
            .map(entry -> {
                AircraftStatsDTO.DateCount dateCount = new AircraftStatsDTO.DateCount();
                dateCount.setDate(entry.getKey());
                dateCount.setCount(entry.getValue().intValue());
                return dateCount;
            })
            .collect(Collectors.toList());
    }
    
    private String formatDate(LocalDateTime date, ChronoUnit unit) {
        if (unit == ChronoUnit.DAYS) {
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        } else if (unit == ChronoUnit.MONTHS) {
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        } else {
            return date.format(DateTimeFormatter.ofPattern("yyyy"));
        }
    }
    
    // 添加定时任务，每小时清除一次缓存
    @Scheduled(fixedRate = 3600000) // 3600000ms = 1小时
    @CacheEvict(value = "aircraftStats", allEntries = true)
    public void clearStatsCache() {
        // 方法体可以为空，注解会处理缓存的清除
    }
} 