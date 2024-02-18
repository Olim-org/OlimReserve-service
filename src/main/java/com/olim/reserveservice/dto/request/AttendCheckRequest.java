package com.olim.reserveservice.dto.request;

import java.util.UUID;

public record AttendCheckRequest(
        Long customerId,
        UUID centerId
) {
}
