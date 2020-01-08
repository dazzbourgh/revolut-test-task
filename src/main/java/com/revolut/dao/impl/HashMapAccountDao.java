package com.revolut.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

/**
 * A DAO for {@link Account}, which uses a HashMap as it's data store.
 * Thread safety is ensured by injecting {@link java.util.concurrent.ConcurrentHashMap} during startup.
 *
 * @see com.revolut.module.AccountModule for injection details.
 */
// I mean, it's shorter and simpler than Hibernate and H2, but we can talk
// about this later if we want.
@Singleton
public class HashMapAccountDao implements AccountDao {
    @NotNull
    private Map<Long, Account> dataStore;

    @Inject
    public HashMapAccountDao(@NotNull Map<Long, Account> dataStore) {
        this.dataStore = dataStore;
    }

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
