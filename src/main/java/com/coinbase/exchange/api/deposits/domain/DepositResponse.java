package com.coinbase.exchange.api.deposits.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DepositResponse {

    private String id;
    private BigDecimal amount;
    private String currency;
    private String payoutAt;
}
