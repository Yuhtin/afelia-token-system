package com.yuhtin.commission.afelia.tokensystem.placeholder;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class EconomyPlaceholderHook extends PlaceholderExpansion {

    private final AfeliaTokenSystem plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "afelia";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Yuhtin";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        val account = plugin.getAccountStorage().findAccount(player);
        if (params.equalsIgnoreCase("amount")) {
            return account.getBalanceFormated();
        }

        if (params.equalsIgnoreCase("tycoon")) {
            val rankingStorage = plugin.getRankingStorage();
            if (!rankingStorage.getTopPlayer().equals(player.getName())) return "";

            return MessageValue.get(MessageValue::tycoonTagValue);
        }

        return "Invalid param";
    }

}
