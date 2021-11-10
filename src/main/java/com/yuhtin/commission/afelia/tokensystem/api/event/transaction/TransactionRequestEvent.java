package com.yuhtin.commission.afelia.tokensystem.api.event.transaction;

import com.yuhtin.commission.afelia.tokensystem.api.account.Account;
import com.yuhtin.commission.afelia.tokensystem.api.event.AfeliaEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public final class TransactionRequestEvent extends AfeliaEvent implements Cancellable {

    private final Player player;
    private final OfflinePlayer target;
    private final Account account;
    private final double amount;
    private boolean cancelled;

    public TransactionRequestEvent(Player player, OfflinePlayer target, Account account, double amount) {
        super(false);
        this.player = player;
        this.target = target;
        this.account = account;
        this.amount = amount;
    }
}
