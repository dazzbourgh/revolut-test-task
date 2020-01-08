package com.revolut.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.domain.Account;
import com.revolut.dto.request.Deposit;
import com.revolut.dto.request.Transfer;
import com.revolut.dto.request.Withdrawal;
import com.revolut.exception.IllegalAccountException;
import com.revolut.service.AccountCommandService;
import com.revolut.service.AccountQueryService;
import io.javalin.Handler;
import org.eclipse.jetty.http.HttpStatus;

import java.math.BigDecimal;

@Singleton
public class AccountController {
    private AccountCommandService accountCommandService;
    private AccountQueryService accountQueryService;

    @Inject
    public AccountController(AccountCommandService accountCommandService,
                             AccountQueryService accountQueryService) {
        this.accountCommandService = accountCommandService;
        this.accountQueryService = accountQueryService;
    }

    public Handler accountInfo() {
        return ctx -> {
            long id = ctx.pathParam("id", Long.class).get();
            Account account = accountQueryService.getById(id);
            if (account != null) {
                ctx.status(200);
                ctx.json(account);
            } else {
                throw new IllegalAccountException(id);
            }
        };
    }

    public Handler deposit() {
        return ctx -> {
            var deposit = ctx.bodyValidator(Deposit.class)
                    .check(dep -> dep.getAmount() != null && dep.getAmount().compareTo(BigDecimal.ZERO) > 0,
                            "deposit amount must be greater than zero")
                    .get();
            accountCommandService.deposit(deposit.getAccountId(), deposit.getAmount());
            ctx.status(HttpStatus.OK_200);
        };
    }

    public Handler withdraw() {
        return ctx -> {
            var withdrawal = ctx.bodyValidator(Withdrawal.class)
                    .check(w -> w.getAmount() != null && w.getAmount().compareTo(BigDecimal.ZERO) > 0,
                            "withdrawal amount must be greater than zero")
                    .get();
            accountCommandService.withdraw(withdrawal.getAccountId(), withdrawal.getAmount());
            ctx.status(HttpStatus.OK_200);
        };
    }

    public Handler transfer() {
        return ctx -> {
            var transfer = ctx.bodyValidator(Transfer.class)
                    .check(t -> t.getAmount() != null && t.getAmount().compareTo(BigDecimal.ZERO) > 0,
                            "transfer amount must be greater than zero")
                    .get();
            accountCommandService.transfer(transfer.getFromId(), transfer.getToId(), transfer.getAmount());
            ctx.status(HttpStatus.OK_200);
        };
    }
}
