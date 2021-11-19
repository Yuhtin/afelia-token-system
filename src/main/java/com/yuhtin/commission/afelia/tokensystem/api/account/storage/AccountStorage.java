package com.yuhtin.commission.afelia.tokensystem.api.account.storage;

import com.yuhtin.commission.afelia.tokensystem.api.account.Account;
import com.yuhtin.commission.afelia.tokensystem.dao.repository.AccountRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class AccountStorage {

    private final AccountRepository accountRepository;
    private final Map<String, Account> cache = new HashMap<>();

    public void init(Plugin plugin) {
        accountRepository.createTable();
        plugin.getLogger().info("Storage started successfully");
    }

    @Nullable
    public Account findAccountByName(@NotNull String name) {
        if (cache.containsKey(name)) return cache.get(name);

        val account = accountRepository.selectOne(name);
        if (account != null) cache.put(name, account);

        return account;
    }

    @Nullable
    public Account findAccount(@NotNull OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            val player = offlinePlayer.getPlayer();
            if (player != null) return findAccount(player);
        }

        return findAccountByName(offlinePlayer.getName());
    }

    @NotNull
    public Account findAccount(@NotNull Player player) {
        Account account = findAccountByName(player.getName());
        if (account == null) {
            account = new Account(player.getName());
            cache.put(player.getName(), account);
        }

        return account;
    }

    public void flushData() {
        cache.values().forEach(accountRepository::saveOne);
        cache.clear();
    }
}
