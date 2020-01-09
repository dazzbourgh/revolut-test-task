package com.revolut.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.domain.Account;
import com.revolut.dto.request.Amount;
import com.revolut.dto.request.Deposit;
import com.revolut.dto.request.Transfer;
import com.revolut.dto.request.Withdrawal;
import com.revolut.exception.IllegalAccountException;
import com.revolut.service.AccountCommandService;
import com.revolut.service.AccountQueryService;
import io.javalin.Handler;
import org.eclipse.jetty.http.HttpStatus;

import java.util.function.Predicate;

/**
 * Methods of this class provide various handler functions for HTTP requests.
 */
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

    /**
     * GET request handler for account information, requires {@code id} path parameter
     * that specifies account id.
     *
     * @return a handler for GET request to check account information (balance).
     */
    public Handler accountInfo() {
        return ctx -> {
            long id = ctx.pathParam("id", Long.class).get();
            Account account = accountQueryService.getById(id);
            if (account != null) {
                ctx.status(HttpStatus.OK_200);
                ctx.json(account);
            } else {
                throw new IllegalAccountException(id);
            }
        };
    }

    /**
     * POST request to deposit endpoint, requires {@link Deposit} as request body.
     *
     * @return a handler function for POST request to deposit endpoint.
     */
    public Handler deposit(Predicate<Amount> validator) {
        return ctx -> {
            var deposit = ctx.bodyValidator(Deposit.class)
                    .check(validator::test, "deposit amount must be greater than zero")
                    .get();
            accountCommandService.deposit(deposit.getAccountId(), deposit.getAmount());
            ctx.status(HttpStatus.OK_200);
        };
    }

    /**
     * POST request to withdrawal endpoint, requires {@link Withdrawal} as request body.
     *
     * @return a handler function for POST request to withdrawal endpoint.
     */
    public Handler withdraw(Predicate<Amount> validator) {
        return ctx -> {
            var withdrawal = ctx.bodyValidator(Withdrawal.class)
                    .check(validator::test, "withdrawal amount must be greater than zero")
                    .get();
            accountCommandService.withdraw(withdrawal.getAccountId(), withdrawal.getAmount());
            ctx.status(HttpStatus.OK_200);
        };
    }

    /**
     * POST request to transfer endpoint, requires {@link Transfer} as request body.
     *
     * @return a handler function for POST request to transfer endpoint.
     */
    public Handler transfer(Predicate<Amount> validator) {
        return ctx -> {
            var transfer = ctx.bodyValidator(Transfer.class)
                    .check(validator::test, "transfer amount must be greater than zero")
                    .get();
            accountCommandService.transfer(transfer.getFromId(), transfer.getToId(), transfer.getAmount());
            ctx.status(HttpStatus.OK_200);
        };
    }
}
