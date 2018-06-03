package com.coinbase.exchange.api.orders;

import com.coinbase.exchange.api.entity.Fill;
import com.coinbase.exchange.api.entity.Hold;
import com.coinbase.exchange.api.entity.NewOrderSingle;
import com.coinbase.exchange.api.exchange.GdaxHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String ORDERS_ENDPOINT = "/orders";

    private final GdaxHttpClient httpClient;

    public List<Hold> getHolds(final String accountId) {
        return httpClient.getAsList(ORDERS_ENDPOINT + "/" + accountId + "/holds",
                new ParameterizedTypeReference<Hold[]>() {
                });
    }

    public List<Order> getOpenOrders(final String accountId) {
        return httpClient.getAsList(ORDERS_ENDPOINT + "/" + accountId + "/orders",
                new ParameterizedTypeReference<Order[]>() {
                });
    }

    public Order getOrder(final String orderId) {
        return httpClient.get(ORDERS_ENDPOINT + "/" + orderId, new ParameterizedTypeReference<Order>() {
        });
    }

    public Order createOrder(final NewOrderSingle order) {
        return httpClient.post(ORDERS_ENDPOINT, new ParameterizedTypeReference<Order>() {
        }, order);
    }

    public String cancelOrder(final String orderId) {
        final String deleteEndpoint = ORDERS_ENDPOINT + "/" + orderId;

        return httpClient.delete(deleteEndpoint, new ParameterizedTypeReference<String>() {
        });
    }

    public List<Order> getOpenOrders() {
        return httpClient.getAsList(ORDERS_ENDPOINT, new ParameterizedTypeReference<Order[]>() {
        });
    }

    public List<Order> cancelAllOpenOrders() {
        return Arrays.asList(httpClient.delete(ORDERS_ENDPOINT, new ParameterizedTypeReference<Order[]>() {
        }));
    }

    public List<Fill> getAllFills() {
        final String fillsEndpoint = "/fills";

        return httpClient.getAsList(fillsEndpoint, new ParameterizedTypeReference<Fill[]>() {
        });
    }
}


