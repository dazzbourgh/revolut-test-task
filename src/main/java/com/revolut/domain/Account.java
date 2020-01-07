package com.revolut.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @GeneratedValue
    private long id;
    private User user;
    private BigDecimal balance;
}
