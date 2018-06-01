package com.coinbase.exchange.api.marketdata.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@JsonDeserialize(builder = Trade.TradeBuilder.class)
public class Trade {

    private final DateTime time;
    private final long trade_id;
    private final BigDecimal price;
    private final BigDecimal size;
    private final String side;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TradeBuilder {
    }
}
