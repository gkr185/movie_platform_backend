package com.edu.bcu.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VipUpdateDTO {
    private Integer vipType;
    private LocalDateTime vipExpireTime;
} 