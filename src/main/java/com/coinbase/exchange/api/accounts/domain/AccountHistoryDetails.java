package com.coinbase.exchange.api.accounts.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AccountHistoryDetails {

    private String orderId;
    private int tradeId;
    private String productId;
}
