package com.coinbase.exchange.api.marketdata.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonDeserialize(builder = MarketData.MarketDataBuilder.class)
public class MarketData {

    private final long sequence;
    private final List<OrderItem> bids;
    private final List<OrderItem> asks;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class MarketDataBuilder {
    }
}
