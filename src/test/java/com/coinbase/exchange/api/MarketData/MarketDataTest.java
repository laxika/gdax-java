package com.coinbase.exchange.api.MarketData;

import com.coinbase.exchange.api.BaseTest;
import com.coinbase.exchange.api.marketdata.domain.MarketData;
import com.coinbase.exchange.api.marketdata.MarketDataService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

@Ignore
public class MarketDataTest extends BaseTest {

    @Autowired
    private MarketDataService marketDataService;

    @Test
    public void canGetMarketDataForLevelOneBidAndAsk() {
        final MarketData marketData = marketDataService.getMarketDataOrderBook("BTC-GBP", "1");

        assertTrue(marketData.getSequence() > 0);
    }

    @Test
    public void canGetMarketDataForLevelTwoBidAndAsk() {
        final MarketData marketData = marketDataService.getMarketDataOrderBook("BTC-GBP", "2");

        assertTrue(marketData.getSequence() > 0);
    }

    /*
     * Note that the returned results are slightly different for level 3. For level 3 you will see an
     * order Id rather than the count of orders at a certain price.
     */
    @Test
    public void canGetMarketDataForLevelThreeBidAndAsk() {
        final MarketData marketData = marketDataService.getMarketDataOrderBook("BTC-GBP", "3");

        assertTrue(marketData.getSequence() > 0);
    }
}
