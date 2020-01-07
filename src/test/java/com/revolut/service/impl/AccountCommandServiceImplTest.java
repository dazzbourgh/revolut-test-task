package com.revolut.service.impl;

import com.revolut.dao.AccountDao;
import com.revolut.domain.Account;
import com.revolut.exception.IllegalAccountException;
import com.revolut.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCommandServiceImplTest {
    private static final long FROM_ID = 1L;
    private static final long TO_ID = 2L;
    @InjectMocks
    private AccountCommandServiceImpl sut;
    @Mock
    private AccountDao accountDao;
    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    public void init() {
        fromAccount = new Account();
        fromAccount.setId(FROM_ID);
        fromAccount.setBalance(new BigDecimal("1000000"));

        toAccount = new Account();
        toAccount.setId(TO_ID);
        toAccount.setBalance(new BigDecimal("50"));
    }

    @Test
    public void shouldThrowForIllegalAccount() {
        when(accountDao.getById(-1)).thenReturn(null);

        assertThrows(IllegalAccountException.class, () -> sut.deposit(-1, BigDecimal.ONE));
        assertThrows(IllegalAccountException.class, () -> sut.withdraw(-1, BigDecimal.ONE));
        assertThrows(IllegalAccountException.class, () -> sut.transfer(-1, 1, BigDecimal.ONE));
        assertThrows(IllegalAccountException.class, () -> sut.transfer(1, -1, BigDecimal.ONE));
    }

    @Test
    public void shouldDeposit() {
        // given
        when(accountDao.getById(FROM_ID)).thenReturn(fromAccount);
        // when
        sut.deposit(fromAccount.getId(), BigDecimal.ONE);
        // then
        assertEquals(new BigDecimal("1000001"), fromAccount.getBalance());
        verify(accountDao).save(fromAccount);
    }

    @Test
    public void shouldWithdraw() {
        //given
        when(accountDao.getById(FROM_ID)).thenReturn(fromAccount);
        // when
        sut.withdraw(fromAccount.getId(), new BigDecimal("1000"));
        // then
        assertEquals(new BigDecimal("999000"), fromAccount.getBalance());
        verify(accountDao).save(fromAccount);
    }

    @Test
    public void shouldThrowIfNotEnoughFundsDuringWithdrawal() {
        // given
        when(accountDao.getById(FROM_ID)).thenReturn(fromAccount);
        // then
        assertThrows(InsufficientFundsException.class, () -> sut.withdraw(fromAccount.getId(), new BigDecimal("2000000")));
    }

    @Test
    public void shouldTransfer() {
        // given
        when(accountDao.getById(FROM_ID)).thenReturn(fromAccount);
        when(accountDao.getById(TO_ID)).thenReturn(toAccount);
        // when
        sut.transfer(FROM_ID, TO_ID, new BigDecimal("50"));
        // then
        assertEquals(new BigDecimal("999950"), fromAccount.getBalance());
        assertEquals(new BigDecimal("100"), toAccount.getBalance());
        verify(accountDao).save(fromAccount, toAccount);
    }

    @Test
    public void shouldThrowIfInsufficientFundsDuringTransfer() {
        // given
        when(accountDao.getById(FROM_ID)).thenReturn(fromAccount);
        when(accountDao.getById(TO_ID)).thenReturn(toAccount);
        // then
        assertThrows(InsufficientFundsException.class, () -> sut.transfer(2L, 1L, new BigDecimal("100")));
    }
}
