package com.revolut.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
}
