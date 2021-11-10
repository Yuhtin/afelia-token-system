package com.yuhtin.commission.afelia.tokensystem.placeholder.registry;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.placeholder.EconomyPlaceholderHook;
import lombok.Data;
import org.bukkit.Bukkit;

@Data(staticConstructor = "of")
public final class PlaceholderRegistry {

    private final AfeliaTokenSystem plugin;

    public void register() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            plugin.getLogger().warning(
                    "PlaceholderAPI not found, disabling placeholder support."
            );
            return;
        }

        new EconomyPlaceholderHook(plugin).register();
        plugin.getLogger().info("Placeholder support added!");
    }

}
