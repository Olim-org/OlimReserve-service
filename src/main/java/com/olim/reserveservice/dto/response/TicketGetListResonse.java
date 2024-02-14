package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record TicketGetListResonse(
        Long total,
        List<TicketGetResponse> hits
) {
    public static TicketGetListResonse makeDto(Page<Ticket> tickets) {
        List<TicketGetResponse> ticketGetRespons = new ArrayList<>();
        for (Ticket g : tickets.getContent()) {
            TicketGetResponse ticketGetResponse = TicketGetResponse.makeDto(g);
            ticketGetRespons.add(ticketGetResponse);
        }
        return new TicketGetListResonse(tickets.getTotalElements(), ticketGetRespons);
    }
}
