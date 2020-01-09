package com.revolut.controller;

import com.revolut.domain.Account;
import com.revolut.dto.request.Deposit;
import com.revolut.dto.request.Transfer;
import com.revolut.dto.request.Withdrawal;
import com.revolut.exception.IllegalAccountException;
import com.revolut.service.AccountCommandService;
import com.revolut.service.AccountQueryService;
import io.javalin.BadRequestResponse;
import io.javalin.Context;
import io.javalin.validation.TypedValidator;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    public static final long ID_2 = 2L;
    private static final long ID = 1L;
    @InjectMocks
    private AccountController sut;
    @Mock
    private AccountCommandService accountCommandService;
    @Mock
    private AccountQueryService accountQueryService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Context context;
    private Account account = new Account(ID, BigDecimal.TEN);

    @Test
    void shouldReturnAccountInfo() throws Exception {
        // given
        when(context.pathParam(eq("id"), eq(Long.class)).get()).thenReturn(ID);
        when(accountQueryService.getById(eq(ID))).thenReturn(account);
        // when
        sut.accountInfo().handle(context);
        // then
        verify(accountQueryService).getById(eq(ID));
        verify(context).status(eq(HttpStatus.OK_200));
        verify(context).json(eq(account));
    }

    @Test
    void shouldThrowIfAccountInfoNotFound() {
        // given
        when(context.pathParam(eq("id"), eq(Long.class)).get()).thenReturn(ID);
        when(accountQueryService.getById(eq(ID))).thenReturn(null);
        var handler = sut.accountInfo();
        // then
        assertThrows(IllegalAccountException.class, () -> handler.handle(context));
        verify(accountQueryService).getById(eq(ID));
    }

    @Test
    void shouldDeposit() throws Exception {
        // given
        var amount = BigDecimal.ONE;
        var deposit = new Deposit(ID, amount);
        when(context.bodyValidator(eq(Deposit.class)).check(any(), anyString()).get()).thenReturn(deposit);
        doNothing().when(accountCommandService).deposit(eq(ID), eq(amount));
        // when
        sut.deposit(d -> true).handle(context);
        // then
        verify(context).status(eq(HttpStatus.OK_200));
        verify(accountCommandService).deposit(eq(ID), eq(amount));
    }

    @Test
    void shouldThrowForNonPositiveAmountDuringDeposit() {
        // given
        when(context.bodyValidator(eq(Deposit.class)))
                .thenReturn(new TypedValidator<>(new Deposit(ID, BigDecimal.ONE), ""));
        // then
        assertThrows(BadRequestResponse.class, () -> sut.deposit(a -> false).handle(context));
    }

    @Test
    void shouldWithdraw() throws Exception {
        // given
        var amount = BigDecimal.ONE;
        var withdrawal = new Withdrawal(ID, amount);
        when(context.bodyValidator(eq(Withdrawal.class)).check(any(), anyString()).get()).thenReturn(withdrawal);
        doNothing().when(accountCommandService).withdraw(eq(ID), eq(amount));
        // when
        sut.withdraw(w -> true).handle(context);
        // then
        verify(context).status(eq(HttpStatus.OK_200));
        verify(accountCommandService).withdraw(eq(ID), eq(amount));
    }

    @Test
    void shouldThrowForNonPositiveAmountDuringWithdrawal() {
        // given
        when(context.bodyValidator(eq(Withdrawal.class)))
                .thenReturn(new TypedValidator<>(new Withdrawal(ID, BigDecimal.ONE), ""));
        // then
        assertThrows(BadRequestResponse.class, () -> sut.withdraw(a -> false).handle(context));
    }

    @Test
    void shouldTransfer() throws Exception {
        // given
        var amount = BigDecimal.ONE;
        var transfer = new Transfer(ID, ID_2, amount);
        when(context.bodyValidator(eq(Transfer.class)).check(any(), anyString()).get()).thenReturn(transfer);
        doNothing().when(accountCommandService).transfer(eq(ID), eq(ID_2), eq(amount));
        // when
        sut.transfer(t -> true).handle(context);
        // then
        verify(context).status(eq(HttpStatus.OK_200));
        verify(accountCommandService).transfer(eq(ID), eq(ID_2), eq(amount));
    }

    @Test
    void shouldThrowForNonPositiveAmountDuringTransfer() {
        // given
        when(context.bodyValidator(eq(Transfer.class)))
                .thenReturn(new TypedValidator<>(new Transfer(ID, ID_2, BigDecimal.ONE), ""));
        // then
        assertThrows(BadRequestResponse.class, () -> sut.transfer(a -> false).handle(context));
    }
}