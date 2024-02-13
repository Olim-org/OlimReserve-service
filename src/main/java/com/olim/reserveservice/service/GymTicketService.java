package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.GymTicketCreateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface GymTicketService {
    @Transactional
    String createGymTicket(UUID userId, GymTicketCreateRequest gymTicketCreateRequest);
}
