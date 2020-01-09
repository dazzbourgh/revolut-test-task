package com.revolut.validation;

import com.revolut.dto.request.Amount;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class PositiveAmountPredicate implements Predicate<Amount> {
    @Override
    public boolean test(Amount deposit) {
        return deposit.getAmount() != null && deposit.getAmount().compareTo(BigDecimal.ZERO) > 0;
    }
}
