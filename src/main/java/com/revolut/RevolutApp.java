package com.revolut;

import io.javalin.Javalin;

public class RevolutApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8083);
        app
                .get("/", ctx -> ctx.result("Hello World"))
                .get("/account/:id", ctx -> {

                });
    }
}
