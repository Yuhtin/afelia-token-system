package com.yuhtin.commission.afelia.tokensystem.dao.repository.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.commission.afelia.tokensystem.api.account.Account;

import java.util.UUID;

public final class AccountAdapter implements SQLResultAdapter<Account> {

    @Override
    public Account adaptResult(SimpleResultSet resultSet) {
        UUID uuid = UUID.fromString(resultSet.get("uuid"));
        double accountBalance = resultSet.get("balance");

        Account account = new Account(uuid);
        account.setAfelia(accountBalance);

        return account;
    }

}
