package com.revolut.handler;

import com.revolut.dto.request.Deposit;
import com.revolut.dto.response.ErrorResponse;
import com.revolut.service.AccountCommandService;
import io.javalin.Context;
import io.javalin.Handler;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@AllArgsConstructor
public class DepositHandler implements Handler {
    private AccountCommandService accountCommandService;

    @Override
    public void handle(@NotNull Context ctx) {
        var deposit = ctx.bodyAsClass(Deposit.class);
        if (deposit.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
            accountCommandService.deposit(deposit.getId(), deposit.getAmount());
            ctx.status(200);
        }
        ctx.status(400);
        ctx.json(new ErrorResponse(String.format("Invalid amount: %s", deposit.getAmount())));
    }
}
