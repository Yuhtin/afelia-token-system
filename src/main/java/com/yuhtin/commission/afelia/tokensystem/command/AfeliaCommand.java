package com.yuhtin.commission.afelia.tokensystem.command;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.api.account.storage.AccountStorage;
import com.yuhtin.commission.afelia.tokensystem.api.event.transaction.TransactionRequestEvent;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import com.yuhtin.commission.afelia.tokensystem.util.ColorUtil;
import com.yuhtin.commission.afelia.tokensystem.util.NumberUtils;
import com.yuhtin.commission.afelia.tokensystem.view.BuyAfeliumView;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AfeliaCommand {

    private final AfeliaTokenSystem plugin;
    private final AccountStorage accountStorage;
    private final BuyAfeliumView buyAfeliumView;

    @Command(
            name = "afelium",
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
            name = "afelium.see",
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
            name = "afelium.send",
            aliases = {"pay"},
            usage = "/afelium send {player} {quantity}",
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
            name = "afelium.set",
            usage = "/afelium set {player} {quantity}",
            description = "Set afelia to player.",
            permission = "afeliatoken.command.set",
            async = true
    )
    public void moneySetCommand(Context<CommandSender> context, OfflinePlayer target, String amount) {
        val sender = context.getSender();
        val parse = NumberUtils.parse(amount);

        if (parse < 1) {
            sender.sendMessage(MessageValue.get(MessageValue::invalidQuantity));
            return;
        }

        val offlineAccount = accountStorage.findAccount(target);
        if (offlineAccount == null) {
            sender.sendMessage(MessageValue.get(MessageValue::invalidPlayer));
            return;
        }

        offlineAccount.setBalance(parse);
        sender.sendMessage(MessageValue.get(MessageValue::set)
                .replace("$player", target.getName())
                .replace("$amount", NumberUtils.format(parse))
        );
    }

    @Command(
            name = "afelium.add",
            usage = "/afelium add {player} {quantity} ",
            description = "Add afelia to player.",
            permission = "afeliatoken.command.add",
            async = true
    )
    public void moneyAddCommand(Context<CommandSender> context, OfflinePlayer target, String amount) {
        val sender = context.getSender();
        val parse = NumberUtils.parse(amount);

        if (parse < 1) {
            sender.sendMessage(MessageValue.get(MessageValue::invalidQuantity));
            return;
        }

        val offlineAccount = accountStorage.findAccount(target);
        if (offlineAccount == null) {
            sender.sendMessage(MessageValue.get(MessageValue::invalidPlayer));
            return;
        }

        offlineAccount.setBalance(offlineAccount.getBalance() + parse);
        sender.sendMessage(MessageValue.get(MessageValue::add)
                .replace("$player", target.getName())
                .replace("$amount", NumberUtils.format(parse))
        );
    }

    @Command(
            name = "afelium.take",
            usage = "/afelium take {player} {quantity}",
            description = "Take afelia from player.",
            permission = "afeliatoken.command.take",
            async = true
    )
    public void moneyRemoveCommand(Context<CommandSender> context, OfflinePlayer target, String amount) {
        val sender = context.getSender();
        val parse = NumberUtils.parse(amount);

        if (parse < 1) {
            sender.sendMessage(MessageValue.get(MessageValue::invalidQuantity));
            return;
        }

        val offlineAccount = accountStorage.findAccount(target);
        if (offlineAccount == null) {
            sender.sendMessage(MessageValue.get(MessageValue::invalidPlayer));
            return;
        }

        offlineAccount.setBalance(offlineAccount.getBalance() - parse);
        sender.sendMessage(MessageValue.get(MessageValue::take)
                .replace("$player", target.getName())
                .replace("$amount", NumberUtils.format(parse))
        );
    }

    @Command(
            name = "afelium.top",
            async = true
    )
    public void moneyTopCommand(Context<CommandSender> context) {
        val rankingStorage = plugin.getRankingStorage();
        val sender = context.getSender();

        rankingStorage.updateRanking(false);

        val header = MessageValue.get(MessageValue::chatModelHeader);
        val body = plugin.getRankingChatBody();
        val footer = MessageValue.get(MessageValue::chatModelFooter);

        header.forEach(sender::sendMessage);
        sender.sendMessage(body.getLines());
        footer.forEach(sender::sendMessage);
    }

    @Command(
            name = "afelium.shop",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void moneyBuyCommand(Context<Player> context) {
        buyAfeliumView.openInventory(context.getSender());
    }

    @Command(
            name = "afelium.help",
            description = "Afelium system help message.",
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
