package com.olim.reserveservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketCustomerType {
    VALID("VALID"),
    INVALID("INVALID"),
    REFUND("REFUND"),
    DELETED("DELETED");
    private final String key;
}
