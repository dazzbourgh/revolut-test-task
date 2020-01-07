package com.revolut.dao.impl;

import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class H2AccountDao implements AccountDao {
    @Nullable
    @Override
    public Account getById(long id) {
        return null;
    }

    @Override
    public void save(@NotNull Account... accounts) {

    }
}
