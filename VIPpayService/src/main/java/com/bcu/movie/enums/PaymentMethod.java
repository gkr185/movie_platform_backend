package com.bcu.movie.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    WECHAT(0, "微信支付"),
    ALIPAY(1, "支付宝"),
    BANK_CARD(2, "银行卡");

    private final Integer code;
    private final String name;

    PaymentMethod(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static PaymentMethod getByCode(Integer code) {
        for (PaymentMethod method : values()) {
            if (method.getCode().equals(code)) {
                return method;
            }
        }
        return null;
    }
} 