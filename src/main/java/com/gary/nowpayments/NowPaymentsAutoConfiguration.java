package com.gary.nowpayments;

import com.gary.nowpayments.callback.NowPaymentsCallbackController;
import com.gary.nowpayments.callback.NowPaymentsCallbackHandler;
import com.gary.nowpayments.callback.SignatureVerifier;
import com.gary.nowpayments.client.NowPaymentsClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NowPaymentsProperties.class)
@ConditionalOnProperty(prefix = "nowpayments", name = "api-key")
public class NowPaymentsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NowPaymentsClient nowPaymentsClient(NowPaymentsProperties properties) {
        return new NowPaymentsClient(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "nowpayments", name = "ipn-secret")
    public SignatureVerifier signatureVerifier(NowPaymentsProperties properties) {
        return new SignatureVerifier(properties.getIpnSecret());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({SignatureVerifier.class, NowPaymentsCallbackHandler.class})
    @ConditionalOnProperty(prefix = "nowpayments", name = "ipn-secret")
    public NowPaymentsCallbackController nowPaymentsCallbackController(
            SignatureVerifier signatureVerifier,
            NowPaymentsCallbackHandler callbackHandler) {
        return new NowPaymentsCallbackController(signatureVerifier, callbackHandler);
    }
}
