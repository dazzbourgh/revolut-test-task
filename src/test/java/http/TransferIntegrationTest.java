package http;

import com.google.gson.Gson;
import com.revolut.domain.Account;
import com.revolut.dto.request.Transfer;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferIntegrationTest extends IntegrationTest {
    @Test
    public void shouldTransferBetweenAccounts() throws IOException {
        var fundsBeforeTransfer1 = extractResult(doGet("/accounts/1"), Account.class).getBalance();
        var fundsBeforeTransfer2 = extractResult(doGet("/accounts/2"), Account.class).getBalance();
        var result = doPost("/transfer", new Gson().toJson(new Transfer(1L, 2L, BigDecimal.TEN)));

        assertEquals(HttpStatus.OK_200, result.getStatusLine().getStatusCode());

        var fundsAfterTransfer1 = extractResult(doGet("/accounts/1"), Account.class).getBalance();
        var fundsAfterTransfer2 = extractResult(doGet("/accounts/2"), Account.class).getBalance();

        assertEquals(fundsBeforeTransfer1.subtract(BigDecimal.TEN), fundsAfterTransfer1);
        assertEquals(fundsBeforeTransfer2.add(BigDecimal.TEN), fundsAfterTransfer2);
    }

    @Test
    public void shouldThrowForInvalidRequest() {
        Stream.of("{}", "", "abc",
                new Gson().toJson(new Transfer(1111, 2, BigDecimal.ZERO)),
                new Gson().toJson(new Transfer(1, 2222, BigDecimal.ZERO)),
                new Gson().toJson(new Transfer(1, 2, BigDecimal.ZERO)),
                new Gson().toJson(new Transfer(1, 2, new BigDecimal("1000"))))
                .map(req -> doPost("/withdraw", req))
                .forEach(resp -> assertEquals(HttpStatus.BAD_REQUEST_400, resp.getStatusLine().getStatusCode()));
    }
}
