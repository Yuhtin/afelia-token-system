package com.yuhtin.commission.afelia.tokensystem.configuration.registry;

import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.configuration.FeatureValue;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import lombok.Data;

@Data(staticConstructor = "of")
public final class ConfigurationRegistry {

    private final AfeliaTokenSystem plugin;

    public void register() {
        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);

        configurationInjector.saveDefaultConfiguration(plugin, "messages.yml");
        configurationInjector.injectConfiguration(FeatureValue.instance(), MessageValue.instance());

        getPlugin().getLogger().info("Configuration injected successfully.");
    }

}
