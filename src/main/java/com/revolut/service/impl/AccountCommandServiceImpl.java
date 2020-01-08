package com.revolut.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import com.revolut.exception.IllegalAccountException;
import com.revolut.exception.InsufficientFundsException;
import com.revolut.service.AccountCommandService;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.revolut.util.Operation.performOperationSafely;
import static java.util.Collections.singletonList;

/**
 * An implementation of {@link AccountCommandService}.
 */
@Singleton
public class AccountCommandServiceImpl implements AccountCommandService {
    private AccountDao accountDao;

    @Inject
    public AccountCommandServiceImpl(@NotNull AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deposit(long id, BigDecimal amount) {
        performOperationSafely(singletonList(id), () -> {
            var account = getAccount(id);
            account.setBalance(account.getBalance().add(amount));
            accountDao.save(account);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void withdraw(long id, BigDecimal amount) {
        performOperationSafely(singletonList(id), () -> {
            var account = getAccount(id);
            var currentBalance = account.getBalance();
            var newBalance = currentBalance.subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) >= 1) {
                account.setBalance(currentBalance.subtract(amount));
                accountDao.save(account);
            } else {
                throw new InsufficientFundsException(id);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transfer(long fromId, long toId, BigDecimal amount) {
        performOperationSafely(Arrays.asList(fromId, toId), () -> {
            var from = getAccount(fromId);
            var to = getAccount(toId);
            if (from.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0) {
                from.setBalance(from.getBalance().subtract(amount));
                to.setBalance(to.getBalance().add(amount));
                // TODO: not thread safe
                accountDao.save(from, to);
            } else {
                throw new InsufficientFundsException(fromId);
            }
        });
    }

    @NotNull
    private Account getAccount(long id) {
        var account = accountDao.getById(id);
        if (account == null) {
            throw new IllegalAccountException(id);
        }
        return account;
    }
}
