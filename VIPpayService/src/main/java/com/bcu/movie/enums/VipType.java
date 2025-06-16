package com.bcu.movie.enums;

import lombok.Getter;

@Getter
public enum VipType {
    MONTHLY(1, "月度会员", 30),
    QUARTERLY(2, "季度会员", 90),
    YEARLY(3, "年度会员", 365);

    private final Integer code;
    private final String name;
    private final Integer days;

    VipType(Integer code, String name, Integer days) {
        this.code = code;
        this.name = name;
        this.days = days;
    }

    public static VipType getByCode(Integer code) {
        for (VipType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
} 