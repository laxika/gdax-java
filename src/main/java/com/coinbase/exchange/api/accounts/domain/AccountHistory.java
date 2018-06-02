package com.coinbase.exchange.api.accounts.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AccountHistory {

    private int id;
    private String createdAt;
    private BigDecimal amount;
    private BigDecimal balance;
    private String type;
    private AccountHistoryDetails details;
}
