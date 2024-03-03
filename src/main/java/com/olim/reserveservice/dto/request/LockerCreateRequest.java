package com.olim.reserveservice.dto.request;

import jakarta.persistence.Column;

import java.util.UUID;

public record LockerCreateRequest(
        UUID centerId,
        String name,
        String description,
        String hexColor,
        String customJson
) {
}
