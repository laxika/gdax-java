package com.coinbase.exchange.api;

import com.coinbase.exchange.api.exchange.GdaxExchange;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public abstract class BaseTest {

    @Autowired
    public GdaxExchange exchange;
}
