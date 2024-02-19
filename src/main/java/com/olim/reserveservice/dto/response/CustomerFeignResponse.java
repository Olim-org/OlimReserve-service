package com.olim.reserveservice.dto.response;

import java.util.UUID;

public record CustomerFeignResponse(
    Long id,
    UUID userId,
    String name,
    String phoneNumber,
    UUID owner,
    UUID centerId
) {
}
