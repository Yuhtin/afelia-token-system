package com.yuhtin.commission.afelia.tokensystem.dao.repository;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.commission.afelia.tokensystem.api.account.Account;
import com.yuhtin.commission.afelia.tokensystem.dao.repository.adapter.AccountAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class AccountRepository {

    private static final String TABLE = "afeliatoken_data";

    @Getter
    private final SQLExecutor sqlExecutor;

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "owner CHAR(36) NOT NULL PRIMARY KEY," +
                "balance DOUBLE NOT NULL DEFAULT 0" +
                ");"
        );
    }

    public void recreateTable() {
        sqlExecutor.updateQuery("DELETE FROM " + TABLE);
        createTable();
    }

    private Account selectOneQuery(String query) {
        return sqlExecutor.resultOneQuery(
                "SELECT * FROM " + TABLE + " " + query,
                statement -> {
                },
                AccountAdapter.class
        );
    }

    public Account selectOne(String owner) {
        return selectOneQuery("WHERE owner = '" + owner + "'");
    }

    public Set<Account> selectAll(String query) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE + " " + query,
                k -> {
                },
                AccountAdapter.class
        );
    }

    public void saveOne(Account account) {
        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?,?,?,?,?,?)", TABLE),
                statement -> {
                    statement.set(1, account.getOwner());
                    statement.set(2, account.getAfelia());
                }
        );
    }

}
