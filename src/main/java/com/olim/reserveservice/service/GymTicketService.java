package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.GymTicketCreateRequest;
import com.olim.reserveservice.dto.request.GymTicketModifyRequest;
import com.olim.reserveservice.dto.response.GymTicketGetListResonse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface GymTicketService {
    @Transactional
    String createGymTicket(UUID userId, GymTicketCreateRequest gymTicketCreateRequest);
    @Transactional
    GymTicketGetListResonse getTicketList(UUID userId, UUID centerId);
    @Transactional
    String updateTicket(UUID userId, UUID ticketId, GymTicketModifyRequest gymTicketModifyRequest);
    @Transactional
    String deleteTicket(UUID userId, UUID ticketId);
}
