package com.gary.nowpayments.callback;

import com.gary.nowpayments.dto.PaymentStatusResponse;

public interface NowPaymentsCallbackHandler {
    void onPaymentUpdate(PaymentStatusResponse payment);
    default void onSignatureFailure(String rawBody) {}
}
