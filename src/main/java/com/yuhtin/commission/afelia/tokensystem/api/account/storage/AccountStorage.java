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
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AccountStorage {

    private final AccountRepository accountRepository;
    private final Map<UUID, Account> cache = new HashMap<>();

    public void init(Plugin plugin) {
        accountRepository.createTable();
        plugin.getLogger().info("Storage started successfully");
    }

    @Nullable
    public Account findAccountByUUID(@NotNull UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);

        val account = accountRepository.selectOne(uuid);
        if (account != null) cache.put(uuid, account);

        return account;
    }

    @Nullable
    public Account findAccount(@NotNull OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            val player = offlinePlayer.getPlayer();
            if (player != null) return findAccount(player);
        }

        return findAccountByUUID(offlinePlayer.getUniqueId());
    }

    @NotNull
    public Account findAccount(@NotNull Player player) {
        Account account = findAccountByUUID(player.getUniqueId());
        if (account == null) {
            account = new Account(player.getUniqueId());
            cache.put(player.getUniqueId(), account);
        }

        if (account.getLastName() == null) account.setLastName(player.getName());
        return account;
    }

    public void flushData() {
        cache.values().forEach(accountRepository::saveOne);
    }
}
