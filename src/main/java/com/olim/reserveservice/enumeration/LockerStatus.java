package com.olim.reserveservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LockerStatus {
    EMPTY("EMPTY"),
    RESERVED("RESERVED"),
    USED("USED"),
    BROKEN("BROKEN");
    private final String key;
}
