package com.revolut.dao.impl;

import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

/**
 * A DAO for {@link Account}, which uses a HashMap as it's data store.
 */
// I mean, it's shorter and simpler than Hibernate and H2, but we can talk
// about this later if we want.
@AllArgsConstructor
public class HashMapAccountDao implements AccountDao {
    @NotNull
    private Map<Long, Account> dataStore;

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Account getById(long id) {
        return dataStore.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(@NotNull Account... accounts) {
        Arrays.asList(accounts)
                .forEach(acc -> dataStore.put(acc.getId(), acc));
    }
}
