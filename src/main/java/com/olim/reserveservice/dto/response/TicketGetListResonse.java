package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record TicketGetListResonse(
        @Schema(description = "총 티켓 수", example = "10")
        Long total,
        @Schema(description = "티켓 목록")
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
