
# 智易在线判题微服务系统技术文档

[English](README.md)| 中文

## 技术选型一览

| 技术类别         | 技术/框架                | 说明/用途                         |
|------------------|--------------------------|-----------------------------------|
| 语言/基础        | Java 8                   | 主体开发语言                      |
| 构建工具         | Maven                    | 依赖管理与构建                    |
| 主框架           | Spring Boot 2.6.x        | 微服务基础框架                    |
| 微服务治理       | Spring Cloud 2021.x      | 微服务注册、配置、负载均衡        |
| 注册中心         | Nacos                    | 服务注册与发现                    |
| 配置中心         | Nacos                    | 配置管理                          |
| 网关             | Spring Cloud Gateway     | 统一API网关，路由与安全            |
| 熔断限流         | Sentinel                 | 服务容错、限流、降级              |
| ORM              | MyBatis-Plus            | 数据库操作                        |
| 数据库           | MySQL                    | 关系型数据库                      |
| 缓存/会话        | Redis                    | 分布式缓存、Session存储           |
| 消息队列         | RabbitMQ                 | 服务间异步通信                    |
| 文档/接口测试    | Knife4j (Swagger)        | API文档与调试                     |
| 远程调用         | OpenFeign                | 微服务间声明式HTTP调用            |
| 工具包           | Hutool, Apache Commons   | 工具类库                          |
| 代码沙箱         | 自研+远程沙箱接口        | 代码安全执行与判题                |
| 容器化           | Docker, Docker Compose   | 服务容器化部署                    |
| 单元测试         | JUnit, Spring Boot Test  | 单元与集成测试                    |
| 其他             | Lombok, EasyExcel, Gson  | 简化开发、Excel处理、JSON序列化   |

---

## 架构设计说明

本项目采用**Spring Cloud Alibaba 微服务架构**，实现了高内聚、低耦合的判题系统，主要包括以下核心模块：

1. **API 网关（zhiyi-gateway）**
    - 统一入口，负责路由转发、权限校验（JWT/自定义）、CORS跨域处理、限流与熔断。
    - 通过 Nacos 实现服务发现，动态路由到后端微服务。
    - Knife4j 聚合各服务 API 文档，便于前后端联调。

2. **用户服务（zhiyi-user-service）**
    - 提供用户注册、登录、信息管理等功能。
    - 支持 Redis 分布式 Session，MySQL 持久化存储。
    - 通过 MyBatis-Plus 简化数据库操作。

3. **题目服务（zhiyi-question-service）**
    - 负责题目的增删改查、题目配置、判题用例管理等。
    - 支持 RabbitMQ 消息队列，实现与判题服务的异步解耦通信。

4. **判题服务（zhiyi-judge-service）**
    - 接收判题请求，调用自研或远程代码沙箱安全执行用户代码，返回判题结果。
    - 支持多语言、多判题策略，具备高扩展性。
    - 通过 RabbitMQ 与题目服务解耦。

5. **服务客户端（zhiyi-service-client）**
    - 封装 OpenFeign 客户端，简化服务间远程调用。

6. **通用模块（zhiyi-common, zhiyi-model）**
    - 公共工具类、常量、异常、通用响应体、数据模型等，提升代码复用性。

7. **注册与配置中心**
    - 所有服务通过 Nacos 注册，支持动态扩缩容与配置热更新。

8. **中间件与基础设施**
    - Redis 用于缓存与分布式 Session。
    - MySQL 作为主数据存储。
    - RabbitMQ 实现服务间异步消息通信。
    - Sentinel 实现服务熔断、限流、降级，保障系统稳定性。

9. **容器化与部署**
    - 所有服务均支持 Docker 容器化，使用 Docker Compose 一键编排部署，便于本地开发与生产环境上线。

---

## 典型架构流程

- 用户通过 API 网关发起请求，网关完成鉴权、路由、限流等处理后转发至对应微服务。
- 用户服务、题目服务、判题服务均为独立微服务，互相通过 OpenFeign 或消息队列通信。
- 判题服务通过远程沙箱接口安全执行代码，返回判题结果。
- 所有服务注册到 Nacos，实现服务发现与健康检查。
- 统一 API 文档聚合，便于前后端协作。

---

![image-20250725205701626](.\img\image-20250725205701626.png)

![image-20250725205729575](.\img\\image-20250725205729575.png)
