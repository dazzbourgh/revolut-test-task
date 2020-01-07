package com.revolut.service;

import com.revolut.domain.Account;

public interface AccountQueryService {
    Account getById(long id);
}
