package com.x3.nowpayments.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class PaymentStatusResponse {

    @JSONField(name = "payment_id")
    private Long paymentId;

    @JSONField(name = "invoice_id")
    private Long invoiceId;

    @JSONField(name = "payment_status")
    private String paymentStatus;

    @JSONField(name = "pay_address")
    private String payAddress;

    @JSONField(name = "payin_extra_id")
    private String payinExtraId;

    @JSONField(name = "price_amount")
    private Double priceAmount;

    @JSONField(name = "price_currency")
    private String priceCurrency;

    @JSONField(name = "pay_amount")
    private Double payAmount;

    @JSONField(name = "actually_paid")
    private Double actuallyPaid;

    @JSONField(name = "pay_currency")
    private String payCurrency;

    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "order_description")
    private String orderDescription;

    @JSONField(name = "purchase_id")
    private String purchaseId;

    @JSONField(name = "outcome_amount")
    private Double outcomeAmount;

    @JSONField(name = "outcome_currency")
    private String outcomeCurrency;

    @JSONField(name = "payout_hash")
    private String payoutHash;

    @JSONField(name = "payin_hash")
    private String payinHash;

    @JSONField(name = "created_at")
    private String createdAt;

    @JSONField(name = "updated_at")
    private String updatedAt;

    @JSONField(name = "burning_percent")
    private String burningPercent;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "payment_extra_ids")
    private List<Long> paymentExtraIds;
}
