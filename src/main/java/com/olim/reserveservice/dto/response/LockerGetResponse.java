package com.olim.reserveservice.dto.response;

import java.util.UUID;

public record LockerGetResponse(
        UUID id,
        UUID centerId,
        String name,
        String section,
        String description,
        String hexColor,
        String customJson
) {
    public static LockerGetResponse makeDto(UUID id, UUID centerId, String name, String section, String description, String hexColor, String customJson) {
        return new LockerGetResponse(id, centerId, name, section, description, hexColor, customJson);
    }
}
