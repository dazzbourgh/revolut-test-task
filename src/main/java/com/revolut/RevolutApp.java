package com.revolut;

import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.revolut.controller.AccountController;
import com.revolut.dto.response.ErrorResponse;
import com.revolut.exception.IllegalAccountException;
import com.revolut.exception.InsufficientFundsException;
import com.revolut.module.AccountModule;
import io.javalin.ExceptionHandler;
import io.javalin.Javalin;
import io.javalin.json.JavalinJson;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RevolutApp {
    private static final Logger LOGGER = Logger.getLogger(RevolutApp.class.getName());

    // TODO: collect known exceptions and assign 400 handler
    public static void main(String[] args) {
        var injector = Guice.createInjector(new AccountModule());
        var controller = injector.getInstance(AccountController.class);
        var app = Javalin.create().start(8083);
        var gson = new GsonBuilder().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        JavalinJson.setToJsonMapper(gson::toJson);
        app
                .get("/v1/accounts/:id", controller.accountInfo())
                .post("/v1/deposits", controller.deposit())
//                .post("/v1/withdrawals", controller.withdraw())
//                .post("/v1/transfers", controller.transfer())
                .exception(IllegalAccountException.class, exceptionHandler(400))
                .exception(InsufficientFundsException.class, exceptionHandler(400))
                .exception(Exception.class, exceptionHandler(500));
    }

    @NotNull
    private static ExceptionHandler<Exception> exceptionHandler(int code) {
        return (e, ctx) -> {
            ctx.status(code);
            var message = code >= 500 ? "Unknown error" : e.getLocalizedMessage();
            ctx.json(new ErrorResponse(message));
            LOGGER.log(Level.SEVERE, e, e::getMessage);
        };
    }
}
