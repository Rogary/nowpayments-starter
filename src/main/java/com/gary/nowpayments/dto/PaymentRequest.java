package com.gary.nowpayments.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentRequest {

    @JSONField(name = "price_amount")
    private Double priceAmount;

    @JSONField(name = "price_currency")
    private String priceCurrency;

    @JSONField(name = "pay_amount")
    private Double payAmount;

    @JSONField(name = "pay_currency")
    private String payCurrency;

    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "order_description")
    private String orderDescription;

    @JSONField(name = "ipn_callback_url")
    private String ipnCallbackUrl;

    @JSONField(name = "payout_currency")
    private String payoutCurrency;

    @JSONField(name = "is_fixed_rate")
    private Boolean isFixedRate;

    @JSONField(name = "is_fee_paid_by_user")
    private Boolean isFeePaidByUser;
}
