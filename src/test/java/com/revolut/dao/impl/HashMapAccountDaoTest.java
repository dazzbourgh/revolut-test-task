package com.revolut.dao.impl;

import com.revolut.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HashMapAccountDaoTest {
    private static final Account TEST_ACCOUNT = new Account(1L, BigDecimal.ONE);
    private HashMapAccountDao sut;
    private Map<Long, Account> dataStore;

    @BeforeEach
    public void init() {
        dataStore = new HashMap<>();
        dataStore.put(1L, TEST_ACCOUNT);
        sut = new HashMapAccountDao(dataStore);
    }

    @Test
    public void shouldGetById() {
        assertEquals(TEST_ACCOUNT, sut.getById(1L));
        assertNull(sut.getById(2L));
    }

    @Test
    public void shouldSave() {
        // when
        var account2 = new Account(2L, BigDecimal.TEN);
        var account3 = new Account(3L, BigDecimal.TEN);
        sut.save(account2, account3);
        //then
        assertEquals(account2, dataStore.get(2L));
        assertEquals(account3, dataStore.get(3L));
    }
}
