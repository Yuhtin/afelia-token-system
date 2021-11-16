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

import java.util.List;
import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigFile("messages.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageValue implements ConfigurationInjectable {

    @Getter private static final MessageValue instance = new MessageValue();

    @ConfigField("invalidPlayer") private String invalidPlayer;
    @ConfigField("invalidQuantity") private String invalidQuantity;

    @ConfigField("set") private String set;
    @ConfigField("add") private String add;
    @ConfigField("take") private String take;

    @ConfigField("see") private String seeBalance;
    @ConfigField("see-other") private String seeOtherBalance;

    @ConfigField("help-command") private List<String> helpCommand;
    @ConfigField("help-command-staff") private List<String> helpCommandStaff;

    @ConfigField("format-type") private String formatType;
    @ConfigField("currency-format") private List<String> currencyFormat;

    public static <T> T get(Function<MessageValue, T> function) {
        return function.apply(instance);
    }

}
