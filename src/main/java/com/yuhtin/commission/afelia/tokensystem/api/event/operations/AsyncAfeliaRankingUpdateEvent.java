package com.yuhtin.commission.afelia.tokensystem.api.event.operations;

import com.yuhtin.commission.afelia.tokensystem.api.event.AfeliaEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AsyncAfeliaRankingUpdateEvent extends AfeliaEvent {

    private final Instant instant = Instant.now();

    public AsyncAfeliaRankingUpdateEvent() {
        super(true);
    }

}
