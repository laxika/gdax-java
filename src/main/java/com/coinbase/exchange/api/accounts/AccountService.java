package com.coinbase.exchange.api.accounts;

import com.coinbase.exchange.api.accounts.domain.Account;
import com.coinbase.exchange.api.accounts.domain.AccountHistory;
import com.coinbase.exchange.api.entity.Hold;
import com.coinbase.exchange.api.exchange.GdaxExchange;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final String ACCOUNTS_ENDPOINT = "/accounts";

    private final GdaxExchange exchange;

    public List<Account> getAccounts() {
        return exchange.getAsList(ACCOUNTS_ENDPOINT, new ParameterizedTypeReference<Account[]>() {
        });
    }

    public Account getAccount(final String accountId) {
        return exchange.get(ACCOUNTS_ENDPOINT + "/" + accountId, new ParameterizedTypeReference<Account>() {
        });
    }

    public List<AccountHistory> getAccountHistory(final String accountId) {
        final String accountHistoryEndpoint = ACCOUNTS_ENDPOINT + "/" + accountId + "/ledger";

        return exchange.getAsList(accountHistoryEndpoint, new ParameterizedTypeReference<AccountHistory[]>() {
        });
    }

    public List<AccountHistory> getPagedAccountHistory(final String accountId, final String beforeOrAfter,
            final int pageNumber, final int limit) {
        final String accountHistoryEndpoint = ACCOUNTS_ENDPOINT + "/" + accountId + "/ledger";

        return exchange.pagedGetAsList(accountHistoryEndpoint, new ParameterizedTypeReference<AccountHistory[]>() {
        }, beforeOrAfter, pageNumber, limit);
    }

    public List<Hold> getHolds(final String accountId) {
        final String holdsEndpoint = ACCOUNTS_ENDPOINT + "/" + accountId + "/holds";

        return exchange.getAsList(holdsEndpoint, new ParameterizedTypeReference<Hold[]>() {
        });
    }

    public List<Hold> getPagedHolds(final String accountId, final String beforeOrAfter, final Integer pageNumber,
            final Integer limit) {
        final String holdsEndpoint = ACCOUNTS_ENDPOINT + "/" + accountId + "/holds";

        return exchange.pagedGetAsList(holdsEndpoint, new ParameterizedTypeReference<Hold[]>() {
        }, beforeOrAfter, pageNumber, limit);
    }

}
