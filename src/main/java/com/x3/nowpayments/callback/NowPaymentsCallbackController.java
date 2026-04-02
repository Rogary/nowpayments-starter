package com.x3.nowpayments.callback;

import com.alibaba.fastjson2.JSON;
import com.x3.nowpayments.dto.PaymentStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class NowPaymentsCallbackController {
    private final SignatureVerifier signatureVerifier;
    private final NowPaymentsCallbackHandler callbackHandler;

    public NowPaymentsCallbackController(SignatureVerifier signatureVerifier,
                                          NowPaymentsCallbackHandler callbackHandler) {
        this.signatureVerifier = signatureVerifier;
        this.callbackHandler = callbackHandler;
    }

    @PostMapping("${nowpayments.callback-path:/nowpayments/ipn}")
    public ResponseEntity<Void> handleCallback(
            @RequestHeader(name = "x-nowpayments-sig", required = false) String signature,
            @RequestBody String rawBody) {

        log.info("NOWPayments IPN callback received");
        log.debug("IPN body: {}", rawBody);

        if (!signatureVerifier.verify(signature, rawBody)) {
            log.warn("NOWPayments IPN signature verification failed");
            callbackHandler.onSignatureFailure(rawBody);
            return ResponseEntity.badRequest().build();
        }

        PaymentStatusResponse payment = JSON.parseObject(rawBody, PaymentStatusResponse.class);
        log.info("NOWPayments IPN verified, paymentId={}, status={}",
                payment.getPaymentId(), payment.getPaymentStatus());
        callbackHandler.onPaymentUpdate(payment);
        return ResponseEntity.ok().build();
    }
}
