package com.olim.reserveservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LockerCustomerStatus {
    VALID("VALID"),
    INVALID("INVALID");
    private final String key;
}
