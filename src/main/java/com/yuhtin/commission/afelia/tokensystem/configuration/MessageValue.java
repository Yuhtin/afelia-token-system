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

    @ConfigField("error") private String error;
    @ConfigField("noPermission") private String noPermission;
    @ConfigField("incorrectUsage") private String incorrectUsage;
    @ConfigField("invalidPlayer") private String invalidPlayer;
    @ConfigField("invalidQuantity") private String invalidQuantity;
    @ConfigField("noMoney") private String noMoney;
    @ConfigField("noAfelium") private String noAfelium;
    @ConfigField("yourself") private String yourself;

    @ConfigField("bought") private String bought;
    @ConfigField("sold") private String sold;
    @ConfigField("see") private String seeBalance;
    @ConfigField("see-other") private String seeOtherBalance;
    @ConfigField("set") private String set;
    @ConfigField("add") private String add;
    @ConfigField("take") private String take;
    @ConfigField("sent") private String sent;
    @ConfigField("received") private String received;

    @ConfigField("inventoryName") private String inventoryName;

    @ConfigField("ranking.tycoonTag") private String tycoonTagValue;
    @ConfigField("ranking.header") private List<String> chatModelHeader;
    @ConfigField("ranking.body") private String rankingBody;
    @ConfigField("ranking.footer") private List<String> chatModelFooter;

    @ConfigField("help-command") private List<String> helpCommand;
    @ConfigField("help-command-staff") private List<String> helpCommandStaff;

    @ConfigField("format-type") private String formatType;
    @ConfigField("currency-format") private List<String> currencyFormat;

    public static <T> T get(Function<MessageValue, T> function) {
        return function.apply(instance);
    }

}
