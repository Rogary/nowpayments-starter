package com.gary.nowpayments.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CurrencyResponse {

    @JSONField(name = "currencies")
    private List<String> currencies;
}
