package com.coinbase.exchange.api.payments;

import com.coinbase.exchange.api.exchange.GdaxHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by robevansuk on 16/02/2017.
 */
@Component
public class PaymentService {

    private static final String PAYMENT_METHODS_ENDPOINT = "/payment-methods";
    private static final String COINBASE_ACCOUNTS_ENDPOINT = "/coinbase-accounts";

    @Autowired
    GdaxHttpClient gdaxHttpClient;

    public List<PaymentType> getPaymentTypes() {
        return gdaxHttpClient.getAsList(PAYMENT_METHODS_ENDPOINT, new ParameterizedTypeReference<PaymentType[]>(){});
    }

    public List<CoinbaseAccount> getCoinbaseAccounts() {
        return gdaxHttpClient.getAsList(COINBASE_ACCOUNTS_ENDPOINT, new ParameterizedTypeReference<CoinbaseAccount[]>() {});
    }
}