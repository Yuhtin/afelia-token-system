package com.yuhtin.commission.afelia.tokensystem.listener;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.listener.transaction.TransactionRequestListener;
import com.yuhtin.commission.afelia.tokensystem.listener.update.RankingListener;
import lombok.Data;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.Arrays;

@Data(staticConstructor = "of")
public final class ListenerRegistry {

    private final AfeliaTokenSystem plugin;

    public void register() {
        val logger = plugin.getLogger();
        try {
            val pluginManager = Bukkit.getPluginManager();

            val rankingStorage = plugin.getRankingStorage();
            val accountRepository = plugin.getAccountRepository();
            val accountStorage = plugin.getAccountStorage();
            val rankingChatBody = getPlugin().getRankingChatBody();

            val listeners = Arrays.asList(
                    new RankingListener(accountRepository, rankingStorage, rankingChatBody),
                    new TransactionRequestListener(accountStorage)
            );

            listeners.forEach(listener -> pluginManager.registerEvents(listener, plugin));

            logger.info("Registered " + listeners.size() + " listeners successfully!");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
