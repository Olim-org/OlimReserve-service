package com.olim.reserveservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@RequiredArgsConstructor
public enum TicketStatus {
    SELL("SELL"),
    WAIT("WAIT"),
    CLOSE("CLOSE"),
    DELETE("DELETE");
    private final String key;
}
