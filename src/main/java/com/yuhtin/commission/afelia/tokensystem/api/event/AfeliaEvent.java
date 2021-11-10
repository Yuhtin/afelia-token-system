package com.yuhtin.commission.afelia.tokensystem.api.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class AfeliaEvent extends Event {

    @Getter private static final HandlerList handlerList = new HandlerList();

    public AfeliaEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
