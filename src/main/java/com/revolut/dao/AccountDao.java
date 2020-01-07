package com.revolut.dao;

import com.revolut.domain.Account;

public interface AccountDao {
    Account getById(long id);
    void save(Account account);
}
