package com.olim.reserveservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketType {
    GYM("GYM"),
    PT("PT"),
    GYM_SUIT("GYM_SUIT");
    private final String key;
}
