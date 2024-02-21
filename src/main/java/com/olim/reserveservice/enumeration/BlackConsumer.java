package com.olim.reserveservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BlackConsumer {
    YES("YES"),
    NO("NO");
    private final String key;
}
