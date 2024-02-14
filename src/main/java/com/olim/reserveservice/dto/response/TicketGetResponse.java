package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.enumeration.TicketType;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record TicketGetResponse(
        UUID id,
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
        TicketStatus status
) {
    public static TicketGetResponse makeDto(Ticket ticket) {
        TicketGetResponse response = new TicketGetResponse(
                ticket.getId(),
                ticket.getCenterId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPrice(),
                ticket.getSale(),
                ticket.getType(),
                ticket.getApplyDays(),
                ticket.getValidCounts(),
                ticket.getStartTime().format(DateTimeFormatter.ISO_TIME),
                ticket.getEndTime().format(DateTimeFormatter.ISO_TIME),
                ticket.getStatus()
        );
        return response;
    }
}
