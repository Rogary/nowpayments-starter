# NOWPayments Spring Boot Starter

封装 [NOWPayments](https://nowpayments.io/) 加密货币支付 API 的 Spring Boot Starter。

## 功能特性

- NOWPayments REST API 客户端（状态查询、币种、估价、发票、支付）
- IPN（即时支付通知）回调，支持 HMAC-SHA512 签名验证
- 基于 `@ConditionalOnProperty` 的自动配置
- 所有 Bean 均可通过 `@ConditionalOnMissingBean` 覆盖

## 环境要求

- Java 11+
- Spring Boot 2.7+

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.gary</groupId>
    <artifactId>nowpayments-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置属性

```yaml
nowpayments:
  api-key: your-api-key
  # 可选：用于 IPN 回调签名验证
  ipn-secret: your-ipn-secret
  # 可选：请求中的默认 URL
  ipn-callback-url: https://your-domain.com/nowpayments/ipn
  success-url: https://your-domain.com/payment/success
  cancel-url: https://your-domain.com/payment/cancel
```

### 3. 使用客户端

```java
@Autowired
private NowPaymentsClient nowPaymentsClient;

// 查询 API 状态
ApiStatusResponse status = nowPaymentsClient.getApiStatus();

// 获取可用币种
CurrencyResponse currencies = nowPaymentsClient.getAvailableCurrencies();

// 获取估算价格
EstimatePriceResponse estimate = nowPaymentsClient.getEstimatePrice(100.0, "usd", "btc");

// 创建发票
InvoiceRequest invoiceReq = new InvoiceRequest();
invoiceReq.setPriceAmount(100.0);
invoiceReq.setPriceCurrency("usd");
InvoiceResponse invoice = nowPaymentsClient.createInvoice(invoiceReq);

// 创建支付
PaymentRequest paymentReq = new PaymentRequest();
paymentReq.setPriceAmount(100.0);
paymentReq.setPriceCurrency("usd");
paymentReq.setPayCurrency("btc");
PaymentStatusResponse payment = nowPaymentsClient.createPayment(paymentReq);

// 查询支付状态
PaymentStatusResponse paymentStatus = nowPaymentsClient.getPaymentStatus("payment-id");
```

### 4. 处理 IPN 回调（可选）

实现 `NowPaymentsCallbackHandler` 接口并注册为 Spring Bean：

```java
@Component
public class MyPaymentCallbackHandler implements NowPaymentsCallbackHandler {

    @Override
    public void onPaymentUpdate(PaymentStatusResponse payment) {
        // 处理支付状态更新
    }

    @Override
    public void onSignatureFailure(String rawBody) {
        // 处理签名验证失败（可选）
    }
}
```

回调端点自动注册在 `/nowpayments/ipn`（可通过 `nowpayments.callback-path` 配置）。

## 配置参考

| 属性 | 必填 | 默认值 | 说明 |
|---|---|---|---|
| `nowpayments.api-key` | 是 | — | NOWPayments API 密钥（激活开关） |
| `nowpayments.ipn-secret` | 否 | — | IPN HMAC-SHA512 签名验证密钥 |
| `nowpayments.base-url` | 否 | `https://api.nowpayments.io` | API 基础地址 |
| `nowpayments.callback-path` | 否 | `/nowpayments/ipn` | IPN 回调端点路径 |
| `nowpayments.success-url` | 否 | — | 默认支付成功跳转 URL |
| `nowpayments.cancel-url` | 否 | — | 默认支付取消跳转 URL |
| `nowpayments.ipn-callback-url` | 否 | — | 默认 IPN 回调 URL |
| `nowpayments.connect-timeout` | 否 | `10s` | HTTP 连接超时 |
| `nowpayments.read-timeout` | 否 | `20s` | HTTP 读取超时 |

## 构建

```bash
mvn clean install
```
