package com.coinbase.exchange.api.orders.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateLimitOrderRequest extends CreateOrderRequest {

    private BigDecimal size;
    private BigDecimal price;
    private Boolean postOnly;

    public BigDecimal getPrice() {
        return price.setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getSize() {
        return size.setScale(8, BigDecimal.ROUND_HALF_UP);
    }
}
