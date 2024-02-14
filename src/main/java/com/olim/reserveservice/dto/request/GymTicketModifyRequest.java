package com.olim.reserveservice.dto.request;

import com.olim.reserveservice.enumeration.TicketStatus;

import java.util.UUID;

public record GymTicketModifyRequest(
        String title,
        String description,
        String price,
        String sale,
        Integer applyDays,
        String startTime,
        String endTime,
        TicketStatus ticketStatus
) {
}
