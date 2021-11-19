package com.yuhtin.commission.afelia.tokensystem.listener.update;

import com.yuhtin.commission.afelia.tokensystem.api.account.Account;
import com.yuhtin.commission.afelia.tokensystem.api.event.operations.AsyncAfeliaRankingUpdateEvent;
import com.yuhtin.commission.afelia.tokensystem.api.ranking.RankingChatBody;
import com.yuhtin.commission.afelia.tokensystem.api.ranking.RankingStorage;
import com.yuhtin.commission.afelia.tokensystem.configuration.MessageValue;
import com.yuhtin.commission.afelia.tokensystem.dao.repository.AccountRepository;
import com.yuhtin.commission.afelia.tokensystem.util.ColorUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.LinkedList;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@RequiredArgsConstructor
public class RankingListener implements Listener {

    private final AccountRepository accountRepository;
    private final RankingStorage rankingStorage;
    private final RankingChatBody rankingChatBody;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRankingUpdate(AsyncAfeliaRankingUpdateEvent event) {
        rankingStorage.getTop().clear();
        val accounts = accountRepository.selectAll("ORDER BY balance DESC LIMIT 10");

        if (!accounts.isEmpty()) {
            val tycoonTag = MessageValue.get(MessageValue::tycoonTagValue);

            val bodyLines = new LinkedList<String>();
            int position = 1;
            for (Account account : accounts) {
                if (position == 1) rankingStorage.setTopPlayer(account.getUsername());
                rankingStorage.getTop().add(account);

                val body = MessageValue.get(MessageValue::rankingBody);
                bodyLines.add(body
                        .replace("$position", String.valueOf(position))
                        .replace("$player", account.getUsername())
                        .replace("$tycoon", position == 1 ? tycoonTag : "")
                        .replace("$amount", account.getBalanceFormated()));
                position++;
            }

            rankingChatBody.setLines(bodyLines.toArray(new String[]{}));
        } else {
            rankingChatBody.setLines(new String[]{ColorUtil.colored(
                    "  &cNothing!"
            )});
        }
    }

}
