package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record RouteTicketSalesResponse(
        String ticketName,
        String value
) {
    public static List<RouteTicketSalesResponse> makeDto(Map<Ticket, String> totalSales) {
        List<RouteTicketSalesResponse> ticketSalesResponses = new ArrayList<>();
        for (Ticket ticket : totalSales.keySet()) {
            ticketSalesResponses.add(new RouteTicketSalesResponse(ticket.getTitle(), totalSales.get(ticket)));
        }
        return ticketSalesResponses;
    }
}
