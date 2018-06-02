package com.coinbase.exchange.api.deposits;

import com.coinbase.exchange.api.deposits.domain.DepositResponse;
import com.coinbase.exchange.api.entity.CoinbasePaymentRequest;
import com.coinbase.exchange.api.exchange.GdaxExchange;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositService {

    private static final String DEPOSIT_ENDPOINT = "/deposits";
    private static final String PAYMENTS = "/payment-method";
    private static final String COINBASE_PAYMENT = "/coinbase-account";

    private final GdaxExchange exchange;

    public DepositResponse depositViaPaymentMethod(BigDecimal amount, String currency, String paymentMethodId) {
        CoinbasePaymentRequest coinbasePaymentRequest = new CoinbasePaymentRequest(amount, currency, paymentMethodId);
        return exchange.post(DEPOSIT_ENDPOINT + PAYMENTS, new ParameterizedTypeReference<DepositResponse>() {
        }, coinbasePaymentRequest);
    }

    public DepositResponse depositViaCoinbase(BigDecimal amount, String currency, String coinbaseAccountId) {
        CoinbasePaymentRequest coinbasePaymentRequest = new CoinbasePaymentRequest(amount, currency, coinbaseAccountId);
        return exchange.post(DEPOSIT_ENDPOINT + COINBASE_PAYMENT, new ParameterizedTypeReference<DepositResponse>() {
        }, coinbasePaymentRequest);
    }
}
