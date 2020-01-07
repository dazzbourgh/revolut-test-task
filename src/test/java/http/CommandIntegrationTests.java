package http;

import com.google.gson.Gson;
import com.revolut.domain.Account;
import com.revolut.dto.request.Deposit;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

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
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

//        extractResult(response, Response.class);

        var accountAfterUpdate = extractResult(client.execute(infoRequest), Account.class);
        assertEquals(balanceBeforeUpdate.add(BigDecimal.TEN), accountAfterUpdate.getBalance());
    }
}
