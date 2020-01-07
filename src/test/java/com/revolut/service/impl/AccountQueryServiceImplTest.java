package com.revolut.service.impl;

import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountQueryServiceImplTest {
    @InjectMocks
    private AccountQueryServiceImpl sut;
    @Mock
    private AccountDao accountDao;

    @Test
    void getById() {
        // given
        Account account = new Account(1L, BigDecimal.ONE);
        when(accountDao.getById(anyLong())).thenReturn(account);
        // when
        var result = sut.getById(1L);
        // then
        assertEquals(account, result);
    }
}
