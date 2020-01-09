package com.revolut;

public class Main {
    public static void main(String[] args) {
        var port = args.length > 1 ? Integer.parseInt(args[1]): 8088;
        new RevolutApp(port).start();
    }
}
