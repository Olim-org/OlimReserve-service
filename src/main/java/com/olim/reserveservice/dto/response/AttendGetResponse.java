package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Attend;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record AttendGetResponse(
        Long customerId,
        UUID ticketCustomerId,
        UUID centerId,
        UUID ticketId,
        String customerName,
        String ticketTitle,
        String ticketStartDate,
        String ticketEndDate,
        Integer ticketValidCounts,
        String cAt // 출석일
) {
    public static AttendGetResponse makeDto(Attend attend) {
        return new AttendGetResponse(
                attend.getCustomerId(),
                attend.getCenterId(),
                attend.getTicketCustomer().getId(),
                attend.getTicketCustomer().getTicket().getId(),
                attend.getTicketCustomer().getCustomerName(),
                attend.getTicketCustomer().getTicket().getTitle(),
                attend.getTicketCustomer().getStartDate().format(DateTimeFormatter.ISO_DATE),
                attend.getTicketCustomer().getEndDate().format(DateTimeFormatter.ISO_DATE),
                attend.getTicketCustomer().getValidCounts(),
                attend.getCAt().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"))
        );
    }
}
