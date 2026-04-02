# NOWPayments Spring Boot Starter

Spring Boot Starter for [NOWPayments](https://nowpayments.io/) crypto payment API.

## Features

- NOWPayments REST API client (status, currencies, estimate, invoice, payment)
- IPN (Instant Payment Notification) callback with HMAC-SHA512 signature verification
- Auto-configuration with `@ConditionalOnProperty` activation
- All beans can be overridden via `@ConditionalOnMissingBean`

## Requirements

- Java 11+
- Spring Boot 2.7+

## Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>com.x3</groupId>
    <artifactId>nowpayments-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Configure Properties

```yaml
nowpayments:
  api-key: your-api-key
  # Optional: for IPN callback verification
  ipn-secret: your-ipn-secret
  # Optional: default URLs applied to requests
  ipn-callback-url: https://your-domain.com/nowpayments/ipn
  success-url: https://your-domain.com/payment/success
  cancel-url: https://your-domain.com/payment/cancel
```

### 3. Use the Client

```java
@Autowired
private NowPaymentsClient nowPaymentsClient;

// Check API status
ApiStatusResponse status = nowPaymentsClient.getApiStatus();

// Get available currencies
CurrencyResponse currencies = nowPaymentsClient.getAvailableCurrencies();

// Get estimated price
EstimatePriceResponse estimate = nowPaymentsClient.getEstimatePrice(100.0, "usd", "btc");

// Create invoice
InvoiceRequest invoiceReq = new InvoiceRequest();
invoiceReq.setPriceAmount(100.0);
invoiceReq.setPriceCurrency("usd");
InvoiceResponse invoice = nowPaymentsClient.createInvoice(invoiceReq);

// Create payment
PaymentRequest paymentReq = new PaymentRequest();
paymentReq.setPriceAmount(100.0);
paymentReq.setPriceCurrency("usd");
paymentReq.setPayCurrency("btc");
PaymentStatusResponse payment = nowPaymentsClient.createPayment(paymentReq);

// Query payment status
PaymentStatusResponse paymentStatus = nowPaymentsClient.getPaymentStatus("payment-id");
```

### 4. Handle IPN Callbacks (Optional)

Implement `NowPaymentsCallbackHandler` and register it as a Spring bean:

```java
@Component
public class MyPaymentCallbackHandler implements NowPaymentsCallbackHandler {

    @Override
    public void onPaymentUpdate(PaymentStatusResponse payment) {
        // Handle payment status update
    }

    @Override
    public void onSignatureFailure(String rawBody) {
        // Handle signature verification failure (optional)
    }
}
```

The callback endpoint is auto-registered at `/nowpayments/ipn` (configurable via `nowpayments.callback-path`).

## Configuration Reference

| Property | Required | Default | Description |
|---|---|---|---|
| `nowpayments.api-key` | Yes | — | NOWPayments API key (activation gate) |
| `nowpayments.ipn-secret` | No | — | IPN secret for HMAC-SHA512 signature verification |
| `nowpayments.base-url` | No | `https://api.nowpayments.io` | API base URL |
| `nowpayments.callback-path` | No | `/nowpayments/ipn` | IPN callback endpoint path |
| `nowpayments.success-url` | No | — | Default success redirect URL |
| `nowpayments.cancel-url` | No | — | Default cancel redirect URL |
| `nowpayments.ipn-callback-url` | No | — | Default IPN callback URL for requests |
| `nowpayments.connect-timeout` | No | `10s` | HTTP connect timeout |
| `nowpayments.read-timeout` | No | `20s` | HTTP read timeout |

## Build

```bash
mvn clean install
```
