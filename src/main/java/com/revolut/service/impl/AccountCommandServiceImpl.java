package com.revolut.service.impl;

import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import com.revolut.exception.IllegalAccountException;
import com.revolut.exception.InsufficientFundsException;
import com.revolut.service.AccountCommandService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * An implementation of {@link AccountCommandService}.
 */
@AllArgsConstructor
public class AccountCommandServiceImpl implements AccountCommandService {
    @NotNull
    private AccountDao accountDao;

    /**
     * {@inheritDoc}
     */
    // TODO: make thread safe
    @Override
    @Transactional
    public void deposit(long id, BigDecimal amount) {
        var account = getAccount(id);
        account.setBalance(account.getBalance().add(amount));
        accountDao.save(account);
    }

    /**
     * {@inheritDoc}
     */
    // TODO: make thread safe
    @Override
    @Transactional
    public void withdraw(long id, BigDecimal amount) {
        var account = getAccount(id);
        var currentBalance = account.getBalance();
        var newBalance = currentBalance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) >= 1) {
            account.setBalance(currentBalance.subtract(amount));
            accountDao.save(account);
        } else {
            throw new InsufficientFundsException(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    // TODO: make thread safe
    @Override
    @Transactional
    public void transfer(long fromId, long toId, BigDecimal amount) {
        var from = getAccount(fromId);
        var to = getAccount(toId);
        if (from.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0) {
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));
            accountDao.save(from, to);
        } else {
            throw new InsufficientFundsException(fromId);
        }
    }

    private Account getAccount(long id) {
        var account = accountDao.getById(id);
        if (account == null) {
            throw new IllegalAccountException(id);
        }
        return account;
    }
}
