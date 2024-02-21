package com.olim.reserveservice.dto.request;

import java.util.UUID;

public record AttendModifyRequest(
        UUID attendId,
        String attendTime
) {
}
