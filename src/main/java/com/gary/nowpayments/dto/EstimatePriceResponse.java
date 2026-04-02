package com.gary.nowpayments.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class EstimatePriceResponse {

    @JSONField(name = "currency_from")
    private String currencyFrom;

    @JSONField(name = "currency_to")
    private String currencyTo;

    @JSONField(name = "amount_from")
    private Double amountFrom;

    @JSONField(name = "estimated_amount")
    private Double estimatedAmount;
}
