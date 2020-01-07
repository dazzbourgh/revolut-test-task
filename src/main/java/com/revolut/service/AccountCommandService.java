package com.revolut.service;

import java.math.BigDecimal;

public interface AccountCommandService {
    void deposit(long toId, BigDecimal amount);
    void withdraw(long fromId, BigDecimal amount);
    void transfer(long fromId, long toId, BigDecimal amount);
}
