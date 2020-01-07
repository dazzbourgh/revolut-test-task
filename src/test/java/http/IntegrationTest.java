package http;

import com.google.gson.Gson;
import com.revolut.RevolutApp;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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

    protected <T> T extractResult(HttpResponse response, Class<T> clazz) throws IOException {
        var result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        return new Gson().fromJson(result, clazz);
    }
}
