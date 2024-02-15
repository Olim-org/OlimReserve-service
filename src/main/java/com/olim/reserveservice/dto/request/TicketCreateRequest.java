package com.olim.reserveservice.dto.request;

import com.olim.reserveservice.enumeration.TicketType;

import java.util.Map;
import java.util.UUID;

public record TicketCreateRequest(
        UUID centerId,
        String title,
        String description,
        String price,
        String sale,
        TicketType type,
        Integer applyDays,
        Integer validCount,
        String startTime,
        String endTime,
        String customJson
) {
}
