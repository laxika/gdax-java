package com.coinbase.exchange.api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("coinbase.pro.api")
public class ApiProperties {

    private String webEndpoint;
    private String websocketEndpoint;
    private String passphrase;
    private String accessKey;
    private String secretKey;
}
