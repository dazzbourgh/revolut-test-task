package http;

import com.google.gson.Gson;
import com.revolut.RevolutApp;
import com.revolut.domain.Account;
import com.revolut.dto.response.ErrorResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueryIntegrationTests {
    private static final int PORT = 8088;
    private static final String BASE_ADDR = String.format("http://localhost:%s/v1", PORT);
    private RevolutApp sut;
    private HttpClient client;

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

    @Test
    public void shouldReturnAccountInfo() throws IOException {
        var request = new HttpGet(BASE_ADDR + "/accounts/1");
        var response = client.execute(request);

        var result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        var account = new Gson().fromJson(result, Account.class);
        assertEquals(1L, account.getId());
        assertEquals(new BigDecimal("100"), account.getBalance());
    }

    @Test
    public void shouldReturnErrorMessageForInvalidAccount() throws IOException {
        testAccount("22");
        testAccount("abc");
    }

    private void testAccount(String id) throws IOException {
        var request = new HttpGet(BASE_ADDR + "/accounts/" + id);
        var httpResponse = client.execute(request);

        var code = httpResponse.getStatusLine().getStatusCode();
        assertEquals(HttpStatus.BAD_REQUEST_400, code);
        var result = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        var response = new Gson().fromJson(result, ErrorResponse.class);
        assertTrue(response.isError());
    }
}
