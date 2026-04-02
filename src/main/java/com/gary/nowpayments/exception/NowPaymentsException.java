package com.gary.nowpayments.exception;

import lombok.Getter;

@Getter
public class NowPaymentsException extends RuntimeException {

    private int httpStatusCode;

    public NowPaymentsException(String message) {
        super(message);
    }

    public NowPaymentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NowPaymentsException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
