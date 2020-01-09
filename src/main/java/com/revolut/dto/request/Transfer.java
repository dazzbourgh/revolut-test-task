package com.revolut.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transfer implements Amount {
    private long fromId;
    private long toId;
    private BigDecimal amount;
}
