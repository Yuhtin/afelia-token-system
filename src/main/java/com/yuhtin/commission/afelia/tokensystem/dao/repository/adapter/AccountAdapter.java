package com.yuhtin.commission.afelia.tokensystem.dao.repository.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.commission.afelia.tokensystem.api.account.Account;

public final class AccountAdapter implements SQLResultAdapter<Account> {

    @Override
    public Account adaptResult(SimpleResultSet resultSet) {
        String username = resultSet.get("username");
        double accountBalance = resultSet.get("balance");

        Account account = new Account(username);
        account.setBalance(accountBalance);

        return account;
    }

}
