package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

public record TicketGetListResonse(
        int total,
        List<TicketGetResponse> hits
) {
    public static TicketGetListResonse makeDto(List<Ticket> tickets) {
        List<TicketGetResponse> ticketGetRespons = new ArrayList<>();
        for (Ticket g : tickets) {
            TicketGetResponse ticketGetResponse = TicketGetResponse.makeDto(g);
            ticketGetRespons.add(ticketGetResponse);
        }
        return new TicketGetListResonse(ticketGetRespons.size(), ticketGetRespons);
    }
}
