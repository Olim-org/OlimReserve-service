package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record TicketSalesResponse(
    String name,
    String value
) {
    public static List<TicketSalesResponse> makeDto(Map<Ticket, String> totalSales) {
        List<TicketSalesResponse> ticketSalesResponses = new ArrayList<>();
        for (Ticket ticket : totalSales.keySet()) {
            ticketSalesResponses.add(new TicketSalesResponse(ticket.getTitle(), totalSales.get(ticket)));
        }
        return ticketSalesResponses;
    }
}