package com.coinbase.exchange.api.accounts;

import com.coinbase.exchange.api.BaseTest;
import com.coinbase.exchange.api.accounts.domain.Account;
import com.coinbase.exchange.api.accounts.domain.AccountHistory;
import com.coinbase.exchange.api.entity.Hold;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//@Ignore
public class AccountsTest extends BaseTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void canGetAccounts() {
        final List<Account> accounts = accountService.getAccounts();

        assertNotNull(accounts);
    }

    @Test
    public void getAccount() {
        final List<Account> accounts = accountService.getAccounts();
        final Account account = accountService.getAccount(accounts.get(0).getId());

        assertNotNull(account);
    }

    @Test
    public void canGetAccountHistory() {
        final List<Account> accounts = accountService.getAccounts();
        final List<AccountHistory> history = accountService.getAccountHistory(accounts.get(0).getId());

        assertNotNull(history);
    }

    @Test
    public void canGetAccountHolds() {
        List<Account> accounts = accountService.getAccounts();
        List<Hold> holds = accountService.getHolds(accounts.get(0).getId());
        assertNotNull(holds);

        // only check the holds if they exist
        if (holds.size() > 0) {
            assertTrue(holds.get(0).getAmount().floatValue() >= 0.0f);
        }
    }

    /**
     * note that for paged requests the before param takes precedence
     * only if before is null and after is not-null will the after param be inserted.
     */
    @Test
    public void canGetPagedAccountHistory() {
        List<Account> accounts = accountService.getAccounts();
        assertTrue(accounts.size() > 0);
        String beforeOrAfter = "before";
        int pageNumber = 1;
        int limit = 5;
        List<AccountHistory> firstPageAccountHistory = accountService.getPagedAccountHistory(accounts.get(0).getId(),
                beforeOrAfter, pageNumber, limit);
        assertNotNull(firstPageAccountHistory);
        assertTrue(firstPageAccountHistory.size() <= limit);
    }

    @Test
    public void canGetPagedHolds() {
        List<Account> accounts = accountService.getAccounts();

        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);

        String beforeOrAfter = "after";
        int pageNumber = 1;
        int limit = 5;

        List<Hold> firstPageOfHolds = accountService.getPagedHolds(accounts.get(0).getId(),
                beforeOrAfter,
                pageNumber,
                limit);

        assertNotNull(firstPageOfHolds);
        assertTrue(firstPageOfHolds.size() <= limit);
    }
}
