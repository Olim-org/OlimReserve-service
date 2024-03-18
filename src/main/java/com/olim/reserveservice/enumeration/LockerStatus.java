package com.olim.reserveservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LockerStatus {
    AVAILABLE("AVAILABLE"),
    BROKEN("BROKEN");
    private final String key;
}
