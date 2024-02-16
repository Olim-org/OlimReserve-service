package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.TicketCustomerGiveRequest;
import com.olim.reserveservice.dto.response.TicketCustomerGetListResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
public interface TicketCustomerService {
    @Transactional
    String giveTicket(TicketCustomerGiveRequest ticketCustomerGiveRequest, UUID userId, String token);
    @Transactional
    TicketCustomerGetListResponse getListTicketCustomer(UUID userId, String centerId, int page, int count, String name, String sortBy, Boolean orderByDesc, String type);
}
