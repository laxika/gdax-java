package com.coinbase.exchange.api.orders;

import com.coinbase.exchange.api.BaseTest;
import com.coinbase.exchange.api.accounts.domain.Account;
import com.coinbase.exchange.api.accounts.AccountService;
import com.coinbase.exchange.api.entity.Fill;
import com.coinbase.exchange.api.orders.domain.CreateLimitOrderRequest;
import com.coinbase.exchange.api.orders.domain.CreateMarketOrderRequest;
import com.coinbase.exchange.api.marketdata.domain.MarketData;
import com.coinbase.exchange.api.marketdata.MarketDataService;
import com.coinbase.exchange.api.orders.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
@Ignore
public class OrderTests extends BaseTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MarketDataService marketDataService;

    @Autowired
    private OrderService orderService;

    // accounts: BTC, USD, GBP, EUR, CAD
    // products: BTC-USD, BTC-GBP, BTC-EUR, ETH-BTC, ETH-USD, LTC-BTC, LTC-USD

    /*
     * Not Strictly the best test but tests placing the order and then cancelling it without leaving a mess.
     * Note: You'll need credit available
     */
    @Test
    public void canMakeLimitOrderAndGetTheOrderAndCancelIt() {
        final List<Account> accounts = accountService.getAccounts();
        final Optional<Account> accountsWithMoreThanZeroCoinsAvailable = accounts.stream()
                .filter(account -> account.getBalance().compareTo(BigDecimal.ONE) > 0)
                .findFirst();

        assertTrue(accountsWithMoreThanZeroCoinsAvailable.isPresent());

        final String productId = accountsWithMoreThanZeroCoinsAvailable.get().getCurrency() + "-BTC";
        final MarketData marketData = getMarketDataOrderBook(productId);

        assertNotNull(marketData);

        final BigDecimal price = getAskPrice(marketData).setScale(8, BigDecimal.ROUND_HALF_UP);
        final BigDecimal size = new BigDecimal("0.01").setScale(8, BigDecimal.ROUND_HALF_UP);

        final CreateLimitOrderRequest limitOrder = getNewLimitOrderSingle(productId, price, size);

        final Order order = orderService.createOrder(limitOrder);

        assertNotNull(order);
        assertEquals(productId, order.getProductId());
        assertEquals(size, order.getSize());
        assertEquals(price, order.getPrice());
        assertEquals("limit", order.getType());

        orderService.cancelOrder(order.getId());

        final List<Order> orders = orderService.getOpenOrders();
        orders.forEach(o -> assertNotSame(o.getId(), order.getId()));
    }

    @Test
    public void cancelAllOrders() {
        List<Order> cancelledOrders = orderService.cancelAllOpenOrders();
        assertTrue(cancelledOrders.size() >= 0);
    }

    @Test
    public void getAllOpenOrders() {
        List<Order> openOrders = orderService.getOpenOrders();
        assertTrue(openOrders.size() >= 0);
    }

    @Test
    public void getFills() {
        List<Fill> fills = orderService.getAllFills();
        assertTrue(fills.size() >= 0);
    }

    @Test
    public void createMarketOrderBuy() {
        CreateMarketOrderRequest marketOrder = createNewMarketOrder("BTC-USD", "buy", new BigDecimal(0.01));
        Order order = orderService.createOrder(marketOrder);

        assertNotNull(order); //make sure we created an order
        String orderId = order.getId();
        assertTrue(orderId.length() > 0); //ensure we have an actual orderId
        Order filledOrder = orderService.getOrder(orderId);
        assertNotNull(filledOrder); //ensure our order hit the system
        assertTrue(filledOrder.getSize().compareTo(BigDecimal.ZERO) > 0); //ensure we got a fill
        log.info("Order opened and filled: " + filledOrder.getSize() + " @ " + filledOrder.getExecutedValue()
                + " at the cost of " + filledOrder.getFillFees());
    }

    @Test
    public void createMarketOrderSell() {
        CreateMarketOrderRequest marketOrder = createNewMarketOrder("BTC-USD", "sell", new BigDecimal(0.01));
        Order order = orderService.createOrder(marketOrder);
        assertNotNull(order); //make sure we created an order
        String orderId = order.getId();
        assertTrue(orderId.length() > 0); //ensure we have an actual orderId
        Order filledOrder = orderService.getOrder(orderId);
        assertNotNull(filledOrder); //ensure our order hit the system
        assertTrue(filledOrder.getSize().compareTo(BigDecimal.ZERO) > 0); //ensure we got a fill
        log.info("Order opened and filled: " + filledOrder.getSize() + " @ " + filledOrder.getExecutedValue()
                + " at the cost of " + filledOrder.getFillFees());
    }

    private CreateMarketOrderRequest createNewMarketOrder(String product, String action, BigDecimal size) {
        CreateMarketOrderRequest marketOrder = new CreateMarketOrderRequest();
        marketOrder.setProductId(product);
        marketOrder.setSide(action);
        marketOrder.setSize(size);
        return marketOrder;
    }

    private MarketData getMarketDataOrderBook(String product) {
        return marketDataService.getMarketDataOrderBook(product, "1");
    }

    private CreateLimitOrderRequest getNewLimitOrderSingle(String productId, BigDecimal price, BigDecimal size) {
        CreateLimitOrderRequest limitOrder = new CreateLimitOrderRequest();
        limitOrder.setProductId(productId);
        if (productId.contains("-BTC")) {
            limitOrder.setSide("sell");
        } else {
            limitOrder.setSide("buy");
        }
        limitOrder.setType("limit");
        limitOrder.setPrice(price);
        limitOrder.setSize(size);
        return limitOrder;
    }

    private BigDecimal getAskPrice(MarketData marketData) {
        return marketData.getAsks().get(0).getPrice().setScale(4, BigDecimal.ROUND_HALF_UP);
    }
}
