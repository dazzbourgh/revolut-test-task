package com.revolut.service.impl;

import com.revolut.dao.AccountDao;
import com.revolut.service.AccountCommandService;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class AccountCommandServiceImpl implements AccountCommandService {
    private AccountDao accountDao;

    // TODO: make thread safe
    @Override
    public void deposit(long toId, BigDecimal amount) {
        var account = accountDao.getById(toId);
        account.setBalance(account.getBalance().add(amount));
        accountDao.save(account);
    }

    // TODO: make thread safe
    @Override
    public void withdraw(long fromId, BigDecimal amount) {

    }

    // TODO: make thread safe
    @Override
    public void transfer(long fromId, long toId, BigDecimal amount) {

    }
}
