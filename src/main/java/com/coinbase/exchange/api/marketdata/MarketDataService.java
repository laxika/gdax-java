package com.coinbase.exchange.api.marketdata;

import com.coinbase.exchange.api.exchange.GdaxHttpClient;
import com.coinbase.exchange.api.marketdata.domain.MarketData;
import com.coinbase.exchange.api.marketdata.domain.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private static final String PRODUCT_ENDPOINT = "/products";

    private final GdaxHttpClient exchange;

    public MarketData getMarketDataOrderBook(final String productId, final String level) {
        String marketDataEndpoint = PRODUCT_ENDPOINT + "/" + productId + "/book";

        if (!StringUtils.isEmpty(level)) {
            marketDataEndpoint += "?level=" + level;
        }

        return exchange.get(marketDataEndpoint, new ParameterizedTypeReference<MarketData>() {
        });
    }

    public List<Trade> getTrades(final String productId) {
        final String tradesEndpoint = PRODUCT_ENDPOINT + "/" + productId + "/trades";

        return exchange.getAsList(tradesEndpoint, new ParameterizedTypeReference<Trade[]>() {
        });
    }
}
