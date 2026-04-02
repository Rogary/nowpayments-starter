package com.x3.nowpayments.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class MinAmountResponse {

    @JSONField(name = "currency_from")
    private String currencyFrom;

    @JSONField(name = "currency_to")
    private String currencyTo;

    @JSONField(name = "min_amount")
    private Double minAmount;
}
