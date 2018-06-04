package com.coinbase.exchange.api.orders.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public abstract class CreateOrderRequest {

    /**
     * Order ID selected by you to identify your order
     */
    private String clientOid; //optional

    /**
     * limit or market (default is limit)
     */
    private String type;

    /**
     * buy or sell
     */
    private String side;

    /**
     * A valid product id
     */
    private String productId;

    /**
     *  Self-trade prevention flag. Optional. Values are dc, co , cn , cb.
     */
    private String stp;
}
