package com.revolut.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Withdrawal implements Amount {
    private long accountId;
    private BigDecimal amount;
}
