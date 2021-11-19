package com.yuhtin.commission.afelia.tokensystem;

import com.google.common.base.Stopwatch;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.commission.afelia.tokensystem.api.account.storage.AccountStorage;
import com.yuhtin.commission.afelia.tokensystem.api.ranking.RankingChatBody;
import com.yuhtin.commission.afelia.tokensystem.api.ranking.RankingStorage;
import com.yuhtin.commission.afelia.tokensystem.command.registry.CommandRegistry;
import com.yuhtin.commission.afelia.tokensystem.configuration.registry.ConfigurationRegistry;
import com.yuhtin.commission.afelia.tokensystem.dao.SQLProvider;
import com.yuhtin.commission.afelia.tokensystem.dao.repository.AccountRepository;
import com.yuhtin.commission.afelia.tokensystem.hook.EconomyHook;
import com.yuhtin.commission.afelia.tokensystem.listener.ListenerRegistry;
import com.yuhtin.commission.afelia.tokensystem.placeholder.registry.PlaceholderRegistry;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public class AfeliaTokenSystem extends JavaPlugin {

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;

    private AccountRepository accountRepository;
    private AccountStorage accountStorage;

    private RankingStorage rankingStorage;
    private RankingChatBody rankingChatBody;

    private EconomyHook economyHook;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        getLogger().info("Init plugin.");

        val loadTime = Stopwatch.createStarted();

        sqlConnector = SQLProvider.of(this).setup();
        if (sqlConnector == null) {
            disablePlugin();
            return;
        }

        sqlExecutor = new SQLExecutor(sqlConnector);
        accountRepository = new AccountRepository(sqlExecutor);
        accountStorage = new AccountStorage(accountRepository);

        rankingStorage = new RankingStorage();
        rankingChatBody = new RankingChatBody();
        economyHook = new EconomyHook();

        accountStorage.init(this);
        economyHook.init();

        InventoryManager.enable(this);
        ConfigurationRegistry.of(this).register();
        ListenerRegistry.of(this).register();
        CommandRegistry.of(this).register();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            PlaceholderRegistry.of(this).register();
            rankingStorage.updateRanking(true);
        }, 150L);

        loadTime.stop();
        getLogger().log(Level.INFO, "Plugin started. ({0})", loadTime);
    }

    @Override
    public void onDisable() {
        accountStorage.flushData();
    }

    private void disablePlugin() {
        getLogger().info("Disabling plugin by error");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public static AfeliaTokenSystem getInstance() {
        return JavaPlugin.getPlugin(AfeliaTokenSystem.class);
    }
}
