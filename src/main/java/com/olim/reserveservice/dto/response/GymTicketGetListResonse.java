package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.GymTicket;

import java.util.ArrayList;
import java.util.List;

public record GymTicketGetListResonse(
        int total,
        List<GymTicketGetResponse> hits
) {
    public static GymTicketGetListResonse makeDto(List<GymTicket> gymTickets) {
        List<GymTicketGetResponse> gymTicketGetResponses = new ArrayList<>();
        for (GymTicket g : gymTickets) {
            GymTicketGetResponse gymTicketGetResponse = GymTicketGetResponse.makeDto(g);
            gymTicketGetResponses.add(gymTicketGetResponse);
        }
        return new GymTicketGetListResonse(gymTicketGetResponses.size(), gymTicketGetResponses);
    }
}
