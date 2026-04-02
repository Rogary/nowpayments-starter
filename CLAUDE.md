# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot Starter wrapping the NOWPayments crypto payment API. GAV: `com.x3:nowpayments-spring-boot-starter:1.0.0`. Java 11, Spring Boot 2.7.18, OkHttp3 for HTTP, FastJSON2 for JSON serialization.

## Build

```bash
mvn clean install
```

No test infrastructure exists in this project.

## Architecture

### Auto-Configuration & Activation

`NowPaymentsAutoConfiguration` is the entry point, registered via both `spring.factories` (Boot 2.x) and `AutoConfiguration.imports` (Boot 3.x).

- **Activation gate**: `nowpayments.api-key` must be set — without it, no beans are created
- **IPN callbacks**: require `nowpayments.ipn-secret` to be set AND the consuming application to provide a `NowPaymentsCallbackHandler` bean
- All beans use `@ConditionalOnMissingBean` — consumers can override any component

### Bean Wiring Chain

```
api-key set?
  └─ NowPaymentsClient (always created)
  └─ ipn-secret set?
       └─ SignatureVerifier (HMAC-SHA512)
       └─ NowPaymentsCallbackHandler bean exists? (provided by consumer)
            └─ NowPaymentsCallbackController (POST endpoint at ${nowpayments.callback-path})
```

### Package Layout

- `client/` — `NowPaymentsClient`: all REST API calls (status, currencies, min-amount, estimate, invoice, payment, payment-status)
- `callback/` — IPN webhook handling: `SignatureVerifier` (HMAC-SHA512), `NowPaymentsCallbackHandler` (interface consumers implement), `NowPaymentsCallbackController` (auto-registered endpoint)
- `dto/` — Request/response POJOs for each API endpoint
- `exception/` — `NowPaymentsException` with optional HTTP status code

### Key Design Decisions

- `NowPaymentsClient` auto-fills `ipnCallbackUrl`, `successUrl`, and `cancelUrl` from properties if not set on individual requests — check `createInvoice()` and `createPayment()` methods
- The IPN callback endpoint path is configurable via SpEL: `${nowpayments.callback-path:/nowpayments/ipn}`
- `NowPaymentsCallbackHandler` is an interface (not provided by the starter) — the consuming application **must** implement and register it as a bean for the callback controller to activate
- Signature verification uses sorted JSON keys for HMAC computation (NOWPayments IPN spec)

## Configuration Properties

All under prefix `nowpayments.*`:

| Property | Required | Default |
|---|---|---|
| `api-key` | Yes (activation gate) | — |
| `ipn-secret` | For IPN callbacks | — |
| `base-url` | No | `https://api.nowpayments.io` |
| `callback-path` | No | `/nowpayments/ipn` |
| `success-url` | No | — |
| `cancel-url` | No | — |
| `ipn-callback-url` | No | — |
| `connect-timeout` | No | 10s |
| `read-timeout` | No | 20s |
