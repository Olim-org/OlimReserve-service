package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.GymTicket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record GymTicketGetResponse(
        UUID id,
        UUID centerId,
        String title,
        String description,
        String price,
        String sale,
        Integer applyDays,
        String startTime,
        String endTime
) {
    public static GymTicketGetResponse makeDto(GymTicket gymTicket) {
        GymTicketGetResponse response = new GymTicketGetResponse(
                gymTicket.getId(),
                gymTicket.getCenterId(),
                gymTicket.getTitle(),
                gymTicket.getDescription(),
                gymTicket.getPrice(),
                gymTicket.getSale(),
                gymTicket.getApplyDays(),
                gymTicket.getStartTime().format(DateTimeFormatter.ISO_TIME),
                gymTicket.getEndTime().format(DateTimeFormatter.ISO_TIME)
        );
        return response;
    }
}
