package integration.account;

import com.google.gson.Gson;
import com.revolut.domain.Account;
import com.revolut.dto.request.Withdrawal;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WithdrawalIntegrationTest extends IntegrationTest {
    @Test
    public void shouldWithdraw() throws IOException {
        var fundsBeforeWithdrawal = extractResult(doGet("/accounts/1"), Account.class).getBalance();
        var result = doPost("/withdraw", new Gson().toJson(new Withdrawal(1L, BigDecimal.TEN)));

        assertEquals(HttpStatus.OK_200, result.getStatusLine().getStatusCode());

        var fundsAfterWithdrawal = extractResult(doGet("/accounts/1"), Account.class).getBalance();

        assertEquals(fundsBeforeWithdrawal.subtract(BigDecimal.TEN), fundsAfterWithdrawal);
    }

    @Test
    public void shouldThrowForInvalidRequest() {
        Stream.of("{}", "", "abc",
                new Gson().toJson(new Withdrawal(1, BigDecimal.ZERO)),
                new Gson().toJson(new Withdrawal(1, new BigDecimal("1000"))))
                .map(req -> doPost("/withdraw", req))
                .forEach(resp -> assertEquals(HttpStatus.BAD_REQUEST_400, resp.getStatusLine().getStatusCode()));
    }
}
