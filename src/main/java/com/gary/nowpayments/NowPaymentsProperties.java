package com.gary.nowpayments;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "nowpayments")
public class NowPaymentsProperties {

    private String apiKey;

    private String ipnSecret;

    private String baseUrl = "https://api.nowpayments.io";

    private String callbackPath = "/nowpayments/ipn";

    private String successUrl;

    private String cancelUrl;

    private String ipnCallbackUrl;

    private Duration connectTimeout = Duration.ofSeconds(10);

    private Duration readTimeout = Duration.ofSeconds(20);
}
