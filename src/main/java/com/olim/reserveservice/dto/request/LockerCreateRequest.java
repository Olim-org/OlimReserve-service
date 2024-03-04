package com.olim.reserveservice.dto.request;

import com.olim.reserveservice.enumeration.LockerStatus;
import jakarta.persistence.Column;

import java.util.UUID;

public record LockerCreateRequest(
        UUID centerId,
        String name,
        String section,
        String description,
        String hexColor,
        String customJson
) {
}
