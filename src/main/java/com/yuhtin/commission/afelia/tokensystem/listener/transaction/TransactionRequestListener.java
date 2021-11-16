package com.yuhtin.commission.afelia.tokensystem.listener.transaction;

import com.yuhtin.commission.afelia.tokensystem.api.account.storage.AccountStorage;
import com.yuhtin.commission.afelia.tokensystem.api.event.transaction.TransactionRequestEvent;
import com.yuhtin.commission.afelia.tokensystem.configuration.FeatureValue;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import com.yuhtin.commission.afelia.tokensystem.util.NumberUtils;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@AllArgsConstructor
public final class TransactionRequestListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRequest(TransactionRequestEvent event) {
        if (event.isCancelled()) return;

        val player = event.getPlayer();
        val target = event.getTarget();
        val targetAccount = event.getAccount();
        val amount = event.getAmount();

        if (target.getName().equals(player.getName())) {
            player.sendMessage(MessageValue.get(MessageValue::yourself));
            return;
        }

        val account = accountStorage.findAccount(player);
        if (NumberUtils.isInvalid(amount)) {
            player.sendMessage(MessageValue.get(MessageValue::invalidQuantity));
            return;
        }

        if (account.getBalance() < amount) {
            player.sendMessage(MessageValue.get(MessageValue::noAfelium));
            return;
        }

        targetAccount.setBalance(targetAccount.getBalance() + amount);
        account.setBalance(account.getBalance() - amount);

        player.sendMessage(
                MessageValue.get(MessageValue::sent).replace("$player", target.getName())
                        .replace("$amount", NumberUtils.format(amount))
        );

        if (target.isOnline()) {
            target.getPlayer().sendMessage(
                    MessageValue.get(MessageValue::received).replace("$player", player.getName())
                            .replace("$amount", NumberUtils.format(amount))
            );
        }
    }

}
