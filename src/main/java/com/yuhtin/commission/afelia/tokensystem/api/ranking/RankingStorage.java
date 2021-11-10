package com.yuhtin.commission.afelia.tokensystem.api.ranking;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.api.account.Account;
import com.yuhtin.commission.afelia.tokensystem.api.event.operations.AsyncAfeliaRankingUpdateEvent;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@Getter
public class RankingStorage {

    private final LinkedList<Account> top = new LinkedList<>();
    private String topPlayer;
    private long nextUpdateMillis;

    public boolean updateRanking(boolean force) {
        if (!force && nextUpdateMillis > System.currentTimeMillis()) return false;

        val plugin = AfeliaTokenSystem.getInstance();
        val pluginManager = Bukkit.getPluginManager();
        val updateDelayMillis = TimeUnit.SECONDS.toMillis(1800);

        nextUpdateMillis = System.currentTimeMillis() + updateDelayMillis;

        val accountStorage = plugin.getAccountStorage();
        accountStorage.flushData();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            AsyncAfeliaRankingUpdateEvent event = new AsyncAfeliaRankingUpdateEvent();
            pluginManager.callEvent(event);
        });

        return true;
    }

}
