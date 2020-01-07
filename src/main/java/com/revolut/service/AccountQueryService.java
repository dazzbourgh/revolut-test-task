package com.revolut.service;

import com.revolut.domain.Account;

/**
 * A service class to provide querying for accounts.
 */
public interface AccountQueryService {
    /**
     * Get a single {@link Account} from the database.
     *
     * @param id of an account to retrieve.
     * @return an {@link Account} instance, or null if not present in the
     * database.
     */
    Account getById(long id);
}
