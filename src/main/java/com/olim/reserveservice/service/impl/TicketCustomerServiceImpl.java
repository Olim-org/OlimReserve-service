package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.dto.request.TicketCustomerGiveRequest;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.dto.response.CustomerFeignResponse;
import com.olim.reserveservice.dto.response.TicketCustomerGetListResponse;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.entity.TicketCustomer;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketType;
import com.olim.reserveservice.exception.customexception.DataNotFoundException;
import com.olim.reserveservice.exception.customexception.PermissionFailException;
import com.olim.reserveservice.repository.TicketCustomerRepository;
import com.olim.reserveservice.repository.TicketRepository;
import com.olim.reserveservice.service.TicketCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketCustomerServiceImpl implements TicketCustomerService {
    private final TicketCustomerRepository ticketCustomerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerClient customerClient;
    @Transactional
    @Override
    public String giveTicket(TicketCustomerGiveRequest ticketCustomerGiveRequest, UUID userId, String token) {
        Optional<Ticket> ticket = this.ticketRepository.findById(ticketCustomerGiveRequest.ticketId());
        if (ticket.isEmpty()) {
            throw new DataNotFoundException("해당 이용권을 찾을 수 없습니다.");
        }
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), ticket.get().getCenterId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("해당 이용권을 지급할 권한이 없습니다.");
        }
        CustomerFeignResponse customerFeignResponse = customerClient.getCustomerInfo(userId.toString(), ticketCustomerGiveRequest.customerId());
        if (customerFeignResponse == null) {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        Ticket gotTicket = ticket.get();
        switch (gotTicket.getType()) {
            case GYM -> {
                TicketCustomer ticketCustomer = TicketCustomer.builder()
                        .customerId(ticketCustomerGiveRequest.customerId())
                        .customerName(customerFeignResponse.name())
                        .ticketType(TicketType.GYM)
                        .ticket(gotTicket)
                        .startDate(LocalDate.parse(ticketCustomerGiveRequest.startDate(), DateTimeFormatter.ISO_DATE))
                        .endDate(LocalDate.parse(ticketCustomerGiveRequest.endDate(), DateTimeFormatter.ISO_DATE))
                        .type(TicketCustomerType.VALID)
                        .description(ticketCustomerGiveRequest.description())
                        .customJson(ticketCustomerGiveRequest.customJson())
                        .startTime(LocalTime.parse(ticketCustomerGiveRequest.startDate(), DateTimeFormatter.ISO_TIME))
                        .endTime(LocalTime.parse(ticketCustomerGiveRequest.endDate(), DateTimeFormatter.ISO_TIME))
                        .validCounts(ticketCustomerGiveRequest.validCounts())
                        .build();
                this.ticketCustomerRepository.save(ticketCustomer);
                break;

            }
            case PT -> {
                TicketCustomer ticketCustomer = TicketCustomer.builder()
                        .customerId(ticketCustomerGiveRequest.customerId())
                        .customerName(customerFeignResponse.name())
                        .ticketType(TicketType.PT)
                        .ticket(gotTicket)
                        .startDate(LocalDate.parse(ticketCustomerGiveRequest.startDate(), DateTimeFormatter.ISO_DATE))
                        .endDate(LocalDate.parse(ticketCustomerGiveRequest.endDate(), DateTimeFormatter.ISO_DATE))
                        .type(TicketCustomerType.VALID)
                        .description(ticketCustomerGiveRequest.description())
                        .customJson(ticketCustomerGiveRequest.customJson())
                        .startTime(LocalTime.parse(ticketCustomerGiveRequest.startDate(), DateTimeFormatter.ISO_TIME))
                        .endTime(LocalTime.parse(ticketCustomerGiveRequest.endDate(), DateTimeFormatter.ISO_TIME))
                        .validCounts(ticketCustomerGiveRequest.validCounts())
                        .build();
                this.ticketCustomerRepository.save(ticketCustomer);
                break;
            }
        }
        return "성공적으로 이용권이 지급 되었습니다.";
    }
    @Override
    public TicketCustomerGetListResponse getListTicketCustomer(UUID userId, String centerId, int page, int count, String name, String sortBy, Boolean orderByDesc, String type) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId);
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("해당 이용권을 조회할 권한이 없습니다.");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        if (!orderByDesc) {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        }
        Pageable pageable = PageRequest.of(page, count, sort);
        Page<TicketCustomer> ticketCustomerPage = this.ticketCustomerRepository.findAllByCenterIdAndCustomerName(UUID.fromString(centerId), name, pageable);
        TicketCustomerGetListResponse ticketCustomerGetListResponse =
                TicketCustomerGetListResponse.makeDto(ticketCustomerPage);
        return ticketCustomerGetListResponse;
    }
}
