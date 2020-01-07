package com.revolut.service.impl;

import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import com.revolut.service.AccountQueryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountQueryServiceImpl implements AccountQueryService {
    private AccountDao accountDao;

    @Override
    public Account getById(long id) {
        return accountDao.getById(id);
    }
}
