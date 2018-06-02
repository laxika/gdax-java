package com.coinbase.exchange.api.useraccount;

import com.coinbase.exchange.api.exchange.GdaxHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by robevansuk on 17/02/2017.
 */
@Component
public class UserAccountService {

    private static final String USER_ACCOUNT_ENDPOINT = "/users/self/trailing-volume";

    @Autowired
    GdaxHttpClient gdaxHttpClient;

    /**
     * Returns the 30 day trailing volume information from all accounts for the key provided
     * @return UserAccountData
     */
    public List<UserAccountData> getTrailingVolume(){
        return gdaxHttpClient.getAsList(USER_ACCOUNT_ENDPOINT, new ParameterizedTypeReference<UserAccountData[]>() {});
    }
}
