package com.olim.reserveservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olim.reserveservice.dto.request.RouteAndIdRequest;
import com.olim.reserveservice.dto.request.TicketCustomerGiveRequest;
import com.olim.reserveservice.dto.request.TicketCustomerPutRequest;
import com.olim.reserveservice.dto.response.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
public interface TicketCustomerService {
    @Transactional
    String giveTicket(TicketCustomerGiveRequest ticketCustomerGiveRequest, UUID userId, String token);
    @Transactional
    TicketCustomerGetListResponse getListTicketCustomer(UUID userId, String centerId, int page, int count, String name, String sortBy, Boolean orderByDesc, String type);
    @Transactional
    TicketCustomerGetListResponse getTicketCustomer(UUID userId, Long customerId, int page, int count, String sortBy, Boolean orderByDesc, String type);
    @Transactional
    String modifyTicketCustomer(UUID userId, UUID ticketCustomerId, TicketCustomerPutRequest ticketCustomerPutRequest);
    @Transactional
    String deleteTicketCustomer(UUID userId, UUID ticketCustomerId);
    CenterNewCustomerResponse getTicketCustomersIsValid(UUID userId, UUID centerId, List<Long> customerIds);
    List<TicketSalesResponse> getTicketSales(UUID userId, UUID centerId, String startDate, String endDate);
    List<RouteSalseResponse> getRouteTicketSales(UUID userId, UUID centerId, String routeAndId) throws JsonProcessingException;
}
