package com.yuhtin.commission.afelia.tokensystem.command.registry;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.command.AfeliaCommand;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import com.yuhtin.commission.afelia.tokensystem.view.BuyAfeliumView;
import lombok.Data;
import lombok.val;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;

@Data(staticConstructor = "of")
public final class CommandRegistry {

    private final AfeliaTokenSystem plugin;

    public void register() {
        try {
            val afeliumView = new BuyAfeliumView(plugin.getEconomyHook(), plugin.getAccountStorage());;
            afeliumView.init();

            BukkitFrame bukkitFrame = new BukkitFrame(plugin);
            bukkitFrame.registerCommands(new AfeliaCommand(plugin, plugin.getAccountStorage(), afeliumView));

            MessageHolder messageHolder = bukkitFrame.getMessageHolder();
            messageHolder.setMessage(MessageType.ERROR, MessageValue.get(MessageValue::error));
            messageHolder.setMessage(MessageType.INCORRECT_TARGET, MessageValue.get(MessageValue::invalidPlayer));
            messageHolder.setMessage(MessageType.INCORRECT_USAGE, MessageValue.get(MessageValue::incorrectUsage));
            messageHolder.setMessage(MessageType.NO_PERMISSION, MessageValue.get(MessageValue::noPermission));

            plugin.getLogger().info("Command registered successfully.");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
