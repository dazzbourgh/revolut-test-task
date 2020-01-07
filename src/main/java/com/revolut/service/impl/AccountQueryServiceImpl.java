package com.revolut.service.impl;

import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import com.revolut.service.AccountQueryService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation for {@link AccountQueryService}.
 */
@AllArgsConstructor
public class AccountQueryServiceImpl implements AccountQueryService {
    @NotNull
    private AccountDao accountDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Account getById(long id) {
        return accountDao.getById(id);
    }
}
