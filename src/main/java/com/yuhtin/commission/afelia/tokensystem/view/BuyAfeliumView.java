package com.yuhtin.commission.afelia.tokensystem.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.global.GlobalInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.yuhtin.commission.afelia.tokensystem.api.account.storage.AccountStorage;
import com.yuhtin.commission.afelia.tokensystem.configuration.FeatureValue;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import com.yuhtin.commission.afelia.tokensystem.hook.EconomyHook;
import com.yuhtin.commission.afelia.tokensystem.util.ItemBuilder;
import com.yuhtin.commission.afelia.tokensystem.util.NumberUtils;
import lombok.val;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BuyAfeliumView extends GlobalInventory {

    private final EconomyHook economyHook;
    private final AccountStorage accountStorage;

    public BuyAfeliumView(EconomyHook economyHook, AccountStorage accountStorage) {
        super("afelia.main", MessageValue.get(MessageValue::inventoryName), 3 * 9);
        this.economyHook = economyHook;
        this.accountStorage = accountStorage;
    }

    @Override
    protected void configureInventory(@NotNull InventoryEditor editor) {
        val buyPrice = FeatureValue.get(FeatureValue::buyPrice);
        val buyPriceFormatted = NumberUtils.format(buyPrice);

        val sellPrice = FeatureValue.get(FeatureValue::sellPrice);
        val sellPriceFormatted = NumberUtils.format(sellPrice);

        val buyAfelium = new ItemBuilder(Material.DIAMOND)
                .name("&6Buy afelium")
                .setLore("&ePrice: &a" + buyPriceFormatted + " coins")
                .wrap();

        val sellAfelium = new ItemBuilder(Material.EMERALD)
                .name("&cSell afelium")
                .setLore("&ePrice: &a" + sellPriceFormatted + " coins")
                .wrap();

        editor.setItem(12, InventoryItem.of(buyAfelium).defaultCallback(callback -> {
            val viewer = callback.getViewer();
            val player = viewer.getPlayer();
            if (!economyHook.withdrawCoins(player, buyPrice).transactionSuccess()) {
                player.sendMessage(MessageValue.get(MessageValue::noMoney));
                return;
            }

            val account = accountStorage.findAccount(player);
            account.setBalance(account.getBalance() + 1);

            player.sendMessage(MessageValue.get(MessageValue::bought).replace("$amount", buyPriceFormatted));
        }));

        editor.setItem(14, InventoryItem.of(sellAfelium).defaultCallback(callback -> {
            val viewer = callback.getViewer();
            val player = viewer.getPlayer();
            val account = accountStorage.findAccount(player);
            if (account.getBalance() < 1) {
                player.sendMessage(MessageValue.get(MessageValue::noAfelium));
                return;
            }

            account.setBalance(account.getBalance() - 1);
            economyHook.depositCoins(player, sellPrice);

            player.sendMessage(MessageValue.get(MessageValue::sold).replace("$amount", sellPriceFormatted));
        }));
    }
}
