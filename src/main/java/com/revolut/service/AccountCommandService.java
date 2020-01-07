package com.revolut.service;

import java.math.BigDecimal;

/**
 * A service class to provide basic operations for a bank account.
 */
public interface AccountCommandService {
    /**
     * Adds funds to an existing account.
     *
     * @param toId   an id of an account to which to deposit.
     * @param amount how much money to deposit.
     * @throws com.revolut.exception.IllegalAccountException if an account with
     *                                                       provided id cannot be found.
     */
    void deposit(long toId, BigDecimal amount);

    /**
     * @param fromId an id of an account from which to withdraw.
     * @param amount how much money to withdraw.
     * @throws com.revolut.exception.IllegalAccountException    if an account with
     *                                                          provided id cannot be found.
     * @throws com.revolut.exception.InsufficientFundsException if there is not
     *                                                          enough funds to withdraw.
     */
    void withdraw(long fromId, BigDecimal amount);

    /**
     * @param fromId an id of an account where the funds are transferred from.
     * @param toId   an id of an account which is the recipient.
     * @param amount how much funds to transfer.
     * @throws com.revolut.exception.IllegalAccountException    if an account with
     *                                                          provided id cannot be found.
     * @throws com.revolut.exception.InsufficientFundsException if there is not
     *                                                          enough funds on the account with {@code fromId}.
     */
    void transfer(long fromId, long toId, BigDecimal amount);
}
