package com.yuhtin.commission.afelia.tokensystem.command;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.api.account.storage.AccountStorage;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import com.yuhtin.commission.afelia.tokensystem.util.ColorUtil;
import com.yuhtin.commission.afelia.tokensystem.util.NumberUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AfeliaCommand {

    private final AfeliaTokenSystem plugin;
    private final AccountStorage accountStorage;

    @Command(
            name = "afelia",
            aliases = {"afeliaeco"},
            description = "Afelia command",
            async = true
    )
    public void moneyCommand(Context<CommandSender> context) {
        val sender = context.getSender();
        if (sender.hasPermission("afelia.command.help.staff")) {
            for (String s : MessageValue.get(MessageValue::helpCommandStaff)) {
                sender.sendMessage(s);
            }
        } else {
            for (String s : MessageValue.get(MessageValue::helpCommand)) {
                sender.sendMessage(s);
            }
        }
    }

    @Command(
            name = "afelia.see",
            description = "See afelia",
            async = true
    )
    public void moneySeeCommand(Context<CommandSender> context, @Optional OfflinePlayer offlinePlayer) {
        OfflinePlayer target = context.getSender() instanceof Player ? (OfflinePlayer) context.getSender() : null;
        if (offlinePlayer != null) target = offlinePlayer;

        if (target == null || target.getName() == null) {
            context.sendMessage(ColorUtil.colored("&cTarget required."));
            return;
        }

        val account = accountStorage.findAccount(target);
        if (account == null) {
            context.sendMessage(MessageValue.get(MessageValue::invalidPlayer));
            return;
        }

        String message = offlinePlayer != null
                ? MessageValue.get(MessageValue::seeOtherBalance)
                : MessageValue.get(MessageValue::seeBalance);

        context.sendMessage(message
                .replace("$player", target.getName())
                .replace("$amount", account.getBalanceFormated())
        );
    }

    @Command(
            name = "coins.send",
            aliases = {"pay"},
            usage = "/afelia send {player} {quantity}",
            description = "Send afelia to player",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void moneyPayCommand(Context<Player> context, OfflinePlayer target, String amount) {
        val player = context.getSender();

        val parse = NumberUtils.parse(amount);
        if (parse < 1) {
            player.sendMessage(MessageValue.get(MessageValue::invalidQuantity));
            return;
        }

        val offlineAccount = accountStorage.findAccount(target);
        if (offlineAccount == null) {
            player.sendMessage(MessageValue.get(MessageValue::invalidPlayer));
            return;
        }

        val transactionRequestEvent = new TransactionRequestEvent(player, target, offlineAccount, parse);
        Bukkit.getPluginManager().callEvent(transactionRequestEvent);
    }

    @Command(
            name = "afelia.help",
            description = "Afelia system help message.",
            async = true
    )
    public void moneyHelpCommand(Context<CommandSender> context) {
        val sender = context.getSender();
        if (sender.hasPermission("afelia.command.help.staff")) {
            for (String s : MessageValue.get(MessageValue::helpCommandStaff)) {
                sender.sendMessage(s);
            }
        } else {
            for (String s : MessageValue.get(MessageValue::helpCommand)) {
                sender.sendMessage(s);
            }
        }
    }

}
