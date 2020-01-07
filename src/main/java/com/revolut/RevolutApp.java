package com.revolut;

import com.revolut.dto.response.ErrorResponse;
import io.javalin.Handler;
import io.javalin.Javalin;

import java.util.Map;

public class RevolutApp {
    private static Map<String, Handler> handlerMap;

    // TODO: collect known exceptions and assign 400 handler
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8083);
        app
                .get("/v1/accounts/:id", ctx -> ctx.result("Hello World"))
                .post("/v1/deposits", handlerMap.get("deposit"))
                .post("/v1/withdrawals", ctx -> {

                })
                .post("/v1/transfers", ctx -> {

                })
                .exception(Exception.class,
                        (e, ctx) -> {
                            ctx.status(500);
                            ctx.json(new ErrorResponse(e.getMessage()));
                        });
    }
}
