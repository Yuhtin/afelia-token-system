package com.yuhtin.commission.afelia.tokensystem.api.account;

import com.yuhtin.commission.afelia.tokensystem.util.NumberUtils;
import lombok.Data;

@Data
public class Account {

    private final String username;
    private double balance;

    public String getBalanceFormated() {
        return NumberUtils.format(balance);
    }

    public void setBalance(double quantity) {
        if (NumberUtils.isInvalid(quantity)) return;
        if (quantity < 0) balance = 0;
        else balance = quantity;
    }

}
