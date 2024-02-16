package com.olim.reserveservice.dto.response;

import java.util.UUID;

public record CustomerFeignResponse(
    Long id,
    Long userId,
    String name,
    String phoneNumber,
    UUID owner
) {
}
