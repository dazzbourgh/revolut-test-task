package com.revolut.dao;

import com.revolut.domain.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DAO class for {@link Account}.
 */
public interface AccountDao {
    /**
     * Retrieves {@link Account} entity from the database.
     *
     * @param id of the account.
     * @return an instance of {@link Account} or null if there's no such in
     * the database.
     */
    // could have been an Optional<Account> instead, but for this particular app
    // a nullable type looks simpler.
    @Nullable
    Account getById(long id);

    /**
     * Update entities in the database.
     *
     * @param accounts entities to be updated in the database.
     */
    void save(@NotNull Account... accounts);
}
