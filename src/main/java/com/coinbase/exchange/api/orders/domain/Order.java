package com.coinbase.exchange.api.orders.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Order {

    private String id;
    private BigDecimal size;
    private BigDecimal price;
    private String productId;
    private String side;
    private String stp;
    private String type;
    private String timeInForce;
    private String postOnly;
    private String createdAt;
    private String fillFees;
    private String filledSize;
    private String executedValue;
    private String status;
    private Boolean settled;

    public BigDecimal getPrice() {
        return price.setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getSize() {
        return size.setScale(8, BigDecimal.ROUND_HALF_UP);
    }
}
