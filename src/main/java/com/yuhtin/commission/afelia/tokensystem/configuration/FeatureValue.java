package com.yuhtin.commission.afelia.tokensystem.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigFile("config.yml")
@ConfigSection("afelium")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeatureValue implements ConfigurationInjectable {

    @Getter private static final FeatureValue instance = new FeatureValue();

    @ConfigField("buy") private double buyPrice;
    @ConfigField("sell") private double sellPrice;

    public static <T> T get(Function<FeatureValue, T> function) {
        return function.apply(instance);
    }

}
