package com.x3.nowpayments.callback;

import com.x3.nowpayments.dto.PaymentStatusResponse;

public interface NowPaymentsCallbackHandler {
    void onPaymentUpdate(PaymentStatusResponse payment);
    default void onSignatureFailure(String rawBody) {}
}
