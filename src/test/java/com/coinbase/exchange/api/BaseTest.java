package com.coinbase.exchange.api;

import com.coinbase.exchange.api.configuration.ApiConfiguration;
import com.coinbase.exchange.api.exchange.GdaxHttpClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiConfiguration.class)
public abstract class BaseTest {

    @Autowired
    public GdaxHttpClient exchange;
}
