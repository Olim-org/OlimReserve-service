package com.olim.reserveservice.dto.response;

import java.util.UUID;

public record LockerGetResponse(
        UUID id,
        UUID centerId,
        String name,
        String description,
        String hexColor,
        String customJson
) {
}
