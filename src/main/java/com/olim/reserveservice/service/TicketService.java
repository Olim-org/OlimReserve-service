package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.TicketCreateRequest;
import com.olim.reserveservice.dto.request.TicketModifyRequest;
import com.olim.reserveservice.dto.response.TicketGetListResonse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface TicketService {
    @Transactional
    String createGymTicket(UUID userId, TicketCreateRequest ticketCreateRequest);
    @Transactional
    TicketGetListResonse getTicketList(UUID userId, UUID centerId);
    @Transactional
    String updateTicket(UUID userId, UUID ticketId, TicketModifyRequest ticketModifyRequest);
    @Transactional
    String deleteTicket(UUID userId, UUID ticketId);
}
