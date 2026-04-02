package com.x3.nowpayments.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class ApiStatusResponse {

    @JSONField(name = "message")
    private String message;
}
