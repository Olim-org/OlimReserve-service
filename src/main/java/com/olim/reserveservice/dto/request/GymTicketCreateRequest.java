package com.olim.reserveservice.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record GymTicketCreateRequest(
        UUID centerId,
        String title,
        String description,
        String price,
        String sale,
        Integer applyDays,
        String startTime,
        String endTime
) {
}
