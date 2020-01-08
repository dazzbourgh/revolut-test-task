package com.revolut.controller.account;

import com.google.gson.Gson;
import com.revolut.RevolutApp;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class IntegrationTest {
    public static final int PORT = 8088;
    public static final String BASE_ADDR = String.format("http://localhost:%s/v1", PORT);
    protected HttpClient client;
    private RevolutApp sut;

    // reinstantiating for each test since it's cheap in this case
    @BeforeEach
    public void init() {
        sut = new RevolutApp(PORT);
        sut.start();
        client = HttpClients.createDefault();
    }

    @AfterEach
    public void cleanup() {
        sut.stop();
    }

    @SneakyThrows
    protected HttpResponse doPost(String endpoint, String entity) {
        var request = new HttpPost(BASE_ADDR + endpoint);
        try {
            request.setEntity(new StringEntity(entity));
            return client.execute(request);
        } finally {
            request.releaseConnection();
        }
    }

    @SneakyThrows
    protected HttpResponse doGet(String endpoint) {
        var request = new HttpGet(BASE_ADDR + endpoint);
        return client.execute(request);
    }

    protected <T> T extractResult(HttpResponse response, Class<T> clazz) throws IOException {
        var result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        return new Gson().fromJson(result, clazz);
    }
}
