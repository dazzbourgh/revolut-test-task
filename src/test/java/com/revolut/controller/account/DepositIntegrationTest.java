package com.revolut.controller.account;

import com.google.gson.Gson;
import com.revolut.domain.Account;
import com.revolut.dto.request.Deposit;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositIntegrationTest extends IntegrationTest {
    @Test
    public void shouldDeposit() throws IOException {
        var accountBeforeUpdateResponse = doGet("/accounts/1");
        var accountBeforeUpdate = extractResult(accountBeforeUpdateResponse, Account.class);
        var balanceBeforeUpdate = accountBeforeUpdate.getBalance();

        var request = new HttpPost(BASE_ADDR + "/deposit");
        request.setEntity(new StringEntity(new Gson().toJson(new Deposit(1L, BigDecimal.TEN))));
        var response = client.execute(request);
        assertEquals(HttpStatus.OK_200, response.getStatusLine().getStatusCode());

        var accountAfterUpdateResponse = doGet("/accounts/1");
        var accountAfterUpdate = extractResult(accountAfterUpdateResponse, Account.class);
        assertEquals(balanceBeforeUpdate.add(BigDecimal.TEN), accountAfterUpdate.getBalance());
    }

    @Test
    public void shouldThrowForInvalidBody() {
        Stream.of("{}", "", "abc", new Gson().toJson(new Deposit(1, BigDecimal.ZERO)))
                .map(body -> doPost("/deposit", body))
                .forEach(response -> assertEquals(HttpStatus.BAD_REQUEST_400, response.getStatusLine().getStatusCode()));
    }
}
