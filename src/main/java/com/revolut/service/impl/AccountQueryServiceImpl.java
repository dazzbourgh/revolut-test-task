package com.revolut.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import com.revolut.service.AccountQueryService;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation for {@link AccountQueryService}.
 */
@Singleton
public class AccountQueryServiceImpl implements AccountQueryService {
    private AccountDao accountDao;

    @Inject
    public AccountQueryServiceImpl(@NotNull AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account getById(long id) {
        return accountDao.getById(id);
    }
}
