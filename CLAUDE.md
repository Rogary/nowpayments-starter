# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在本仓库中工作时提供指导。

## 项目概述

封装 NOWPayments 加密货币支付 API 的 Spring Boot Starter。GAV: `com.x3:nowpayments-spring-boot-starter:1.0.0`。Java 11，Spring Boot 2.7.18，OkHttp3 发送 HTTP 请求，FastJSON2 处理 JSON 序列化。

## 构建

```bash
mvn clean install
```

本项目暂无测试基础设施。

## 架构

### 自动配置与激活

`NowPaymentsAutoConfiguration` 是入口，通过 `spring.factories`（Boot 2.x）和 `AutoConfiguration.imports`（Boot 3.x）双重注册。

- **激活开关**：必须设置 `nowpayments.api-key`，否则不会创建任何 Bean
- **IPN 回调**：需要设置 `nowpayments.ipn-secret`，并且消费方应用需提供 `NowPaymentsCallbackHandler` Bean
- 所有 Bean 均使用 `@ConditionalOnMissingBean`，消费方可覆盖任意组件

### Bean 装配链

```
api-key 已设置？
  └─ NowPaymentsClient（始终创建）
  └─ ipn-secret 已设置？
       └─ SignatureVerifier（HMAC-SHA512）
       └─ NowPaymentsCallbackHandler Bean 存在？（由消费方提供）
            └─ NowPaymentsCallbackController（POST 端点，路径为 ${nowpayments.callback-path}）
```

### 包结构

- `client/` — `NowPaymentsClient`：所有 REST API 调用（状态、币种、最小金额、估价、发票、支付、支付状态查询）
- `callback/` — IPN Webhook 处理：`SignatureVerifier`（HMAC-SHA512）、`NowPaymentsCallbackHandler`（消费方实现的接口）、`NowPaymentsCallbackController`（自动注册的端点）
- `dto/` — 各 API 端点的请求/响应 POJO
- `exception/` — `NowPaymentsException`，包含可选的 HTTP 状态码

### 关键设计决策

- `NowPaymentsClient` 在请求未设置时，自动从配置属性中填充 `ipnCallbackUrl`、`successUrl` 和 `cancelUrl` —— 参见 `createInvoice()` 和 `createPayment()` 方法
- IPN 回调端点路径通过 SpEL 配置：`${nowpayments.callback-path:/nowpayments/ipn}`
- `NowPaymentsCallbackHandler` 是一个接口（Starter 本身不提供实现）—— 消费方**必须**实现并注册为 Bean，回调控制器才会激活
- 签名验证按 JSON 键排序后计算 HMAC（遵循 NOWPayments IPN 规范）

## 配置属性

所有属性前缀为 `nowpayments.*`：

| 属性 | 必填 | 默认值 |
|---|---|---|
| `api-key` | 是（激活开关） | — |
| `ipn-secret` | IPN 回调时需要 | — |
| `base-url` | 否 | `https://api.nowpayments.io` |
| `callback-path` | 否 | `/nowpayments/ipn` |
| `success-url` | 否 | — |
| `cancel-url` | 否 | — |
| `ipn-callback-url` | 否 | — |
| `connect-timeout` | 否 | 10s |
| `read-timeout` | 否 | 20s |
