package com.revolut;

import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.revolut.controller.AccountController;
import com.revolut.dto.response.ErrorResponse;
import com.revolut.exception.IllegalAccountException;
import com.revolut.exception.InsufficientFundsException;
import com.revolut.module.AccountModule;
import com.revolut.validation.PositiveAmountPredicate;
import io.javalin.BadRequestResponse;
import io.javalin.ExceptionHandler;
import io.javalin.Javalin;
import io.javalin.NotFoundResponse;
import io.javalin.json.JavalinJson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class RevolutApp {
    private static final Logger LOGGER = Logger.getLogger(RevolutApp.class.getName());
    @NonNull
    private int port;
    private Javalin javalin;

    public void start() {
        var injector = Guice.createInjector(new AccountModule());
        var controller = injector.getInstance(AccountController.class);
        javalin = Javalin.create();
        var app = javalin.start(port);
        var gson = new GsonBuilder().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        JavalinJson.setToJsonMapper(gson::toJson);
        PositiveAmountPredicate validator = new PositiveAmountPredicate();
        app
                .get("/v1/accounts/:id", controller.accountInfo())
                .post("/v1/deposit", controller.deposit(validator))
                .post("/v1/withdraw", controller.withdraw(validator))
                .post("/v1/transfer", controller.transfer(validator))
                .exception(IllegalAccountException.class, exceptionHandler(HttpStatus.BAD_REQUEST_400))
                .exception(InsufficientFundsException.class, exceptionHandler(HttpStatus.BAD_REQUEST_400))
                .exception(NumberFormatException.class, exceptionHandler(HttpStatus.BAD_REQUEST_400))
                .exception(NotFoundResponse.class, exceptionHandler(HttpStatus.NOT_FOUND_404))
                .exception(BadRequestResponse.class, exceptionHandler(HttpStatus.BAD_REQUEST_400))
                .exception(Exception.class, exceptionHandler(HttpStatus.INTERNAL_SERVER_ERROR_500));
    }

    public void stop() {
        javalin.stop();
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
