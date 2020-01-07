package http;

import com.google.gson.Gson;
import com.revolut.domain.Account;
import com.revolut.dto.request.Deposit;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandIntegrationTests extends IntegrationTest {
    @Test
    public void shouldDeposit() throws IOException {
        var infoRequest = new HttpGet(BASE_ADDR + "/accounts/1");
        var accountBeforeUpdate = extractResult(client.execute(infoRequest), Account.class);
        var balanceBeforeUpdate = accountBeforeUpdate.getBalance();

        var request = new HttpPost(BASE_ADDR + "/deposit");
        request.setEntity(new StringEntity(new Gson().toJson(new Deposit(1L, BigDecimal.TEN))));
        var response = client.execute(request);
        assertEquals(HttpStatus.OK_200, response.getStatusLine().getStatusCode());

        var accountAfterUpdate = extractResult(client.execute(infoRequest), Account.class);
        assertEquals(balanceBeforeUpdate.add(BigDecimal.TEN), accountAfterUpdate.getBalance());
    }

    @Test
    public void shouldThrowForInvalidBody() {
        Stream.of("{}", "", "abc")
                .map(body -> doPost("/deposit", body))
                .forEach(response -> assertEquals(HttpStatus.BAD_REQUEST_400, response.getStatusLine().getStatusCode()));
    }

    @SneakyThrows
    private HttpResponse doPost(String endpoint, String entity) {
        var request = new HttpPost(BASE_ADDR + endpoint);
        try {
            request.setEntity(new StringEntity(entity));
            return client.execute(request);
        } finally {
            request.releaseConnection();
        }
    }
}
