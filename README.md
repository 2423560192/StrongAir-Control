## 强国战机管控平台(StrongAir-Control)

## 项目概述

强国战机管控平台是一个基于 Spring Boot 构建的后端应用程序，旨在对不同类型的飞机（包括战斗机、运输机和侦察机）及其任务进行高效管理。该系统提供了一系列完整的 RESTful API，涵盖了飞机的增删改查操作、任务分配与召回，以及数据统计分析等功能。

## 技术架构

- **后端框架**：采用 Spring Boot 3.2.3 版本，为系统提供了强大的基础支持。
- **数据库**：使用 MySQL 数据库，用于存储飞机、任务等相关数据。
- **ORM 框架**：结合 Spring Data JPA 和 Hibernate，实现了对象关系映射，简化了数据库操作。
- **缓存机制**：利用 Spring Cache 提升了统计数据查询的性能。
- **构建工具**：通过 Maven 进行项目构建和依赖管理。

## 功能特性

### 1. 飞机管理

- 支持三种类型的飞机：战斗机、运输机和侦察机。
- 提供统一的飞机管理接口，便于操作。
- 每种飞机类型都具备特定的属性，以满足不同的管理需求。
- 实现了飞机的增删改查操作，为飞机管理提供了全面的支持。

### 2. 任务管理

- 支持任务的分配与召回，确保任务的灵活调整。
- 跟踪任务状态，包括进行中、已完成和已中止，便于实时监控任务进展。
- 提供多样化的任务类型，如战斗任务、侦查任务和运输任务等，以适应不同的应用场景。
- 实现了飞机与任务的关联，确保任务分配的准确性和可追溯性。

### 3. 数据统计

- 提供飞机总数、可用数量和任务中数量的统计信息，为管理者提供实时的飞机状态概览。
- 统计不同类型飞机的数量分布，帮助管理者了解飞机资源的配置情况。
- 提供任务总数、活跃任务和已完成任务的统计，为任务管理提供数据支持。
- 统计飞行时长和任务完成率，为绩效评估提供依据。
- 提供任务类型分布和任务趋势统计（按周、月、年），帮助管理者分析任务执行情况和趋势。

### 4. 性能优化

- 通过使用缓存机制，显著提高了统计数据查询的性能。
- 定时清除缓存，确保数据的准确性和实时性。
- 实现事务管理，保障数据的一致性和完整性。

## API 接口

### 飞机管理接口

- `POST /unified-aircraft`：创建新的飞机记录。
- `GET /unified-aircraft`：获取所有飞机的列表。
- `GET /unified-aircraft/{id}`：根据 ID 获取指定的飞机信息。
- `PUT /unified-aircraft/{id}`：更新指定飞机的信息。
- `DELETE /unified-aircraft/{id}`：删除指定的飞机记录。

### 任务管理接口

- `POST /unified-aircraft/mission`：为飞机分配任务。
- `POST /unified-aircraft/mission/{id}/recall`：召回指定的任务。
- `GET /unified-aircraft/{id}/missions`：获取指定飞机的任务列表。
- `GET /unified-aircraft/{id}/status`：获取指定飞机的当前状态。

### 数据统计接口

- `GET /unified-aircraft/stats`：获取飞机和任务的统计数据。

## 快速开始指南

### 环境要求

- JDK 17 或更高版本。
- MySQL 8.0 或更高版本。
- Maven 3.6 或更高版本。

### 数据库配置

## 数据库配置

在 `application.properties` 或 `application.yml` 文件中配置数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

运行项目：

```bash
mvn spring-boot:run
```

项目目录结构：

```plaintext
src/main/java/com/example/demo/
├── config/                # 配置类
├── controller/            # 控制器
├── dto/                   # 数据传输对象
├── entity/                # 实体类
├── exception/             # 异常处理
├── factory/               # 工厂类
├── repository/            # 数据访问层
├── service/               # 服务层
│   └── impl/              # 服务实现
└── DemoApplication.java   # 应用入口
```

## 未来发展规划

- 添加用户认证和授权功能，以增强系统的安全性。
- 添加更多数据分析功能，为决策提供更有力的支持。
- 支持飞机维护记录，完善飞机管理的全生命周期。
- 添加日志记录和审计功能，便于问题追踪和系统监控。

## 前端仓库

前端代码已托管至 [StrongAir-Control-Vue](https://github.com/2423560192/StrongAir-Control-Vue) 仓库，欢迎访问查看。