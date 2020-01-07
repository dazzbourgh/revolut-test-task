package com.revolut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Account {
    private long id;
    @NotNull
    private BigDecimal balance;
}
