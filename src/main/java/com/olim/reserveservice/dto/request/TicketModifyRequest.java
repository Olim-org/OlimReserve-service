package com.olim.reserveservice.dto.request;

import com.olim.reserveservice.enumeration.TicketStatus;

public record TicketModifyRequest(
        String title,
        String description,
        String price,
        String sale,
        Integer applyDays,
        Integer validCount,
        String startTime,
        String endTime,
        TicketStatus ticketStatus
) {
}
