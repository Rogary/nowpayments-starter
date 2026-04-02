package com.gary.nowpayments.client;

import com.alibaba.fastjson2.JSON;
import com.gary.nowpayments.NowPaymentsProperties;
import com.gary.nowpayments.dto.*;
import com.gary.nowpayments.exception.NowPaymentsException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NowPaymentsClient {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final NowPaymentsProperties properties;
    private final OkHttpClient httpClient;

    public NowPaymentsClient(NowPaymentsProperties properties) {
        this.properties = properties;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(properties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(properties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    // ==================== Public API Methods ====================

    public ApiStatusResponse getApiStatus() {
        return executeGet("/v1/status", ApiStatusResponse.class, false);
    }

    public CurrencyResponse getAvailableCurrencies() {
        return executeGet("/v1/currencies", CurrencyResponse.class, false);
    }

    public CurrencyResponse getSelectedCurrencies() {
        return executeGet("/v1/merchant/coins", CurrencyResponse.class, true);
    }

    public MinAmountResponse getMinAmount(String currencyFrom, String currencyTo) {
        String path = "/v1/min-amount?currency_from=" + currencyFrom;
        if (currencyTo != null) {
            path += "&currency_to=" + currencyTo;
        }
        return executeGet(path, MinAmountResponse.class, true);
    }

    public EstimatePriceResponse getEstimatePrice(Double amount, String currencyFrom, String currencyTo) {
        String path = "/v1/estimate?amount=" + amount + "&currency_from=" + currencyFrom + "&currency_to=" + currencyTo;
        return executeGet(path, EstimatePriceResponse.class, true);
    }

    public InvoiceResponse createInvoice(InvoiceRequest request) {
        if (request.getIpnCallbackUrl() == null && properties.getIpnCallbackUrl() != null) {
            request.setIpnCallbackUrl(properties.getIpnCallbackUrl());
        }
        if (request.getSuccessUrl() == null && properties.getSuccessUrl() != null) {
            request.setSuccessUrl(properties.getSuccessUrl());
        }
        if (request.getCancelUrl() == null && properties.getCancelUrl() != null) {
            request.setCancelUrl(properties.getCancelUrl());
        }
        return executePost("/v1/invoice", request, InvoiceResponse.class);
    }

    public PaymentStatusResponse createPayment(PaymentRequest request) {
        if (request.getIpnCallbackUrl() == null && properties.getIpnCallbackUrl() != null) {
            request.setIpnCallbackUrl(properties.getIpnCallbackUrl());
        }
        return executePost("/v1/payment", request, PaymentStatusResponse.class);
    }

    public PaymentStatusResponse getPaymentStatus(String paymentId) {
        return executeGet("/v1/payment/" + paymentId, PaymentStatusResponse.class, true);
    }

    // ==================== Internal Helpers ====================

    private <T> T executeGet(String path, Class<T> clazz, boolean withAuth) {
        String url = properties.getBaseUrl() + path;
        Request request = buildGetRequest(url, withAuth);
        return executeRequest(request, clazz);
    }

    private <T> T executePost(String path, Object body, Class<T> clazz) {
        String url = properties.getBaseUrl() + path;
        String jsonBody = JSON.toJSONString(body);
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("x-api-key", properties.getApiKey())
                .build();
        return executeRequest(request, clazz);
    }

    private Request buildGetRequest(String url, boolean withAuth) {
        Request.Builder builder = new Request.Builder().url(url).get();
        if (withAuth) {
            builder.addHeader("x-api-key", properties.getApiKey());
        }
        return builder.build();
    }

    private <T> T executeRequest(Request request, Class<T> clazz) {
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                log.error("NOWPayments API error: {} {}", response.code(), responseBody);
                throw new NowPaymentsException(response.code(),
                        "NOWPayments API error: " + response.code() + " " + responseBody);
            }
            return JSON.parseObject(responseBody, clazz);
        } catch (IOException e) {
            log.error("NOWPayments API network error", e);
            throw new NowPaymentsException("NOWPayments API network error", e);
        }
    }
}
