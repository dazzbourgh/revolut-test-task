package com.revolut.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.domain.Account;
import com.revolut.dto.request.Deposit;
import com.revolut.dto.response.ErrorResponse;
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
            long id = Long.parseLong(ctx.pathParam("id"));
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
            var deposit = ctx.bodyAsClass(Deposit.class);
            if (deposit.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                accountCommandService.deposit(deposit.getAccountId(), deposit.getAmount());
                ctx.status(HttpStatus.OK_200);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST_400);
                ctx.json(new ErrorResponse(String.format("Invalid amount: %s", deposit.getAmount())));
            }
        };
    }

    public Handler withdraw() {
        throw new UnsupportedOperationException();
    }

    public Handler transfer() {
        throw new UnsupportedOperationException();
    }
}
