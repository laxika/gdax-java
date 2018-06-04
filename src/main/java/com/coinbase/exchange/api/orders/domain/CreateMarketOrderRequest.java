package com.coinbase.exchange.api.orders.domain;

import java.math.BigDecimal;

public class CreateMarketOrderRequest extends CreateOrderRequest {

    private BigDecimal size; //optional: Desired amount in BTC

    public CreateMarketOrderRequest(){
        setType("market");
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

}
