package com.gary.nowpayments.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class InvoiceResponse {

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "order_description")
    private String orderDescription;

    @JSONField(name = "price_amount")
    private Double priceAmount;

    @JSONField(name = "price_currency")
    private String priceCurrency;

    @JSONField(name = "pay_currency")
    private String payCurrency;

    @JSONField(name = "ipn_callback_url")
    private String ipnCallbackUrl;

    @JSONField(name = "invoice_url")
    private String invoiceUrl;

    @JSONField(name = "success_url")
    private String successUrl;

    @JSONField(name = "cancel_url")
    private String cancelUrl;

    @JSONField(name = "created_at")
    private String createdAt;

    @JSONField(name = "updated_at")
    private String updatedAt;

    @JSONField(name = "partially_paid_url")
    private String partiallyPaidUrl;

    @JSONField(name = "payout_currency")
    private String payoutCurrency;

    @JSONField(name = "is_fixed_rate")
    private Boolean isFixedRate;

    @JSONField(name = "is_fee_paid_by_user")
    private Boolean isFeePaidByUser;
}
