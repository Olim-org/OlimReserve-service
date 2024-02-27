package com.olim.reserveservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.dto.request.RouteAndIdRequest;
import com.olim.reserveservice.dto.request.TicketCustomerGiveRequest;
import com.olim.reserveservice.dto.request.TicketCustomerPutRequest;
import com.olim.reserveservice.dto.response.*;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.entity.TicketCustomer;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.enumeration.TicketType;
import com.olim.reserveservice.exception.customexception.CustomException;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketCustomerServiceImpl implements TicketCustomerService {
    private final TicketCustomerRepository ticketCustomerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerClient customerClient;
    private final ObjectMapper objectMapper;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        switch (gotTicket.getType()) {
            case GYM -> {
                TicketCustomer ticketCustomer = TicketCustomer.builder()
                        .centerId(centerFeignResponse.centerId())
                        .customerId(ticketCustomerGiveRequest.customerId())
                        .customerName(customerFeignResponse.name())
                        .ticketType(TicketType.GYM)
                        .ticket(gotTicket)
                        .startDate(LocalDate.parse(ticketCustomerGiveRequest.startDate(), formatter))
                        .endDate(LocalDate.parse(ticketCustomerGiveRequest.endDate(), formatter))
                        .type(TicketCustomerType.VALID)
                        .paymentMethod(ticketCustomerGiveRequest.paymentMethod())
                        .price(ticketCustomerGiveRequest.price())
                        .paidPrice(ticketCustomerGiveRequest.paidPrice())
                        .description(ticketCustomerGiveRequest.description())
                        .customJson(ticketCustomerGiveRequest.customJson())
                        .startTime(gotTicket.getStartTime())
                        .endTime(gotTicket.getEndTime())
                        .validCounts(-10)
                        .build();
                this.ticketCustomerRepository.save(ticketCustomer);
                break;

            }
            case PT -> {
                TicketCustomer ticketCustomer = TicketCustomer.builder()
                        .customerId(ticketCustomerGiveRequest.customerId())
                        .centerId(centerFeignResponse.centerId())
                        .customerName(customerFeignResponse.name())
                        .ticketType(TicketType.PT)
                        .ticket(gotTicket)
                        .startDate(LocalDate.parse(ticketCustomerGiveRequest.startDate(), formatter))
                        .endDate(LocalDate.parse(ticketCustomerGiveRequest.endDate(), formatter))
                        .type(TicketCustomerType.VALID)
                        .paymentMethod(ticketCustomerGiveRequest.paymentMethod())
                        .price(ticketCustomerGiveRequest.price())
                        .paidPrice(ticketCustomerGiveRequest.paidPrice())
                        .description(ticketCustomerGiveRequest.description())
                        .customJson(ticketCustomerGiveRequest.customJson())
                        .startTime(gotTicket.getStartTime())
                        .endTime(gotTicket.getEndTime())
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
        Page<TicketCustomer> ticketCustomerPage = this.ticketCustomerRepository.findAllByCenterIdAndTicketTypeAndCustomerNameContainingAndTypeNotIn(UUID.fromString(centerId), TicketType.valueOf(type), name, List.of(TicketCustomerType.DELETED), pageable);
        TicketCustomerGetListResponse ticketCustomerGetListResponse =
                TicketCustomerGetListResponse.makeDto(ticketCustomerPage);
        return ticketCustomerGetListResponse;
    }

    @Override
    public TicketCustomerGetListResponse getTicketCustomer(UUID userId, Long customerId, int page, int count, String sortBy, Boolean orderByDesc, String type) {
        CustomerFeignResponse customerFeignResponse = customerClient.getCustomerInfo(userId.toString(), customerId);
        if (customerFeignResponse == null) {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        if (!customerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("해당 이용권을 조회할 권한이 없습니다.");
        }
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), customerFeignResponse.centerId().toString());
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
        Page<TicketCustomer> ticketCustomerPage = this.ticketCustomerRepository.findAllByCenterIdAndCustomerIdAndTicketTypeAndTypeNotIn(centerFeignResponse.centerId(), customerId, TicketType.valueOf(type), List.of(TicketCustomerType.DELETED), pageable);
        TicketCustomerGetListResponse  ticketCustomerGetListResponse = TicketCustomerGetListResponse.makeDto(ticketCustomerPage);

        return ticketCustomerGetListResponse;
    }

    @Override
    public String modifyTicketCustomer(UUID userId, UUID ticketCustomerId, TicketCustomerPutRequest ticketCustomerPutRequest) {
        Optional<TicketCustomer> ticketCustomer = this.ticketCustomerRepository.findById(ticketCustomerId);
        if (!ticketCustomer.isPresent()) {
            throw new DataNotFoundException("해당 이용권을 찾을 수 없습니다.");
        }
        CustomerFeignResponse customerFeignResponse = customerClient.getCustomerInfo(userId.toString(), ticketCustomer.get().getCustomerId());
        if (customerFeignResponse == null) {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        if (!customerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("해당 이용권을 수정할 권한이 없습니다.");
        }
        TicketCustomer gotTicketCustomer = ticketCustomer.get();
        Optional<Ticket> ticket = this.ticketRepository.findById(ticketCustomerPutRequest.ticketId());
        if (!ticket.get().getCenterId().equals(gotTicketCustomer.getCenterId())) {
            throw new PermissionFailException("해당 이용권은 해당 센터의 이용권이 아닙니다.");
        }
        if (ticketCustomerPutRequest.type().getKey().equals("DELETE")) {
            throw new CustomException("이용권을 삭제할 수 없습니다. 삭제는 삭제 API를 이용해주세요.");
        }
        if (gotTicketCustomer.getType().getKey().equals("DELETED")) {
            throw new CustomException("이미 삭제된 고객 이용권입니다.");
        }
        switch (ticketCustomer.get().getTicketType()) {
            case GYM -> {
                gotTicketCustomer.updateTicketCustomer(
                        ticket.get(),
                        TicketType.GYM,
                        gotTicketCustomer.getCenterId(),
                        gotTicketCustomer.getCustomerId(),
                        gotTicketCustomer.getCustomerName(),
                        LocalDate.parse(ticketCustomerPutRequest.startDate(), DateTimeFormatter.ISO_DATE),
                        LocalDate.parse(ticketCustomerPutRequest.endDate(), DateTimeFormatter.ISO_DATE),
                        ticketCustomerPutRequest.validCounts(),
                        ticketCustomerPutRequest.paymentMethod(),
                        ticketCustomerPutRequest.price(),
                        ticketCustomerPutRequest.paidPrice(),
                        ticketCustomerPutRequest.type(),
                        ticketCustomerPutRequest.description(),
                        ticketCustomerPutRequest.customJson()
                );
                break;
            }
            case PT -> {
                gotTicketCustomer.updateTicketCustomer(
                        ticket.get(),
                        TicketType.PT,
                        gotTicketCustomer.getCenterId(),
                        gotTicketCustomer.getCustomerId(),
                        gotTicketCustomer.getCustomerName(),
                        LocalDate.parse(ticketCustomerPutRequest.startDate(), DateTimeFormatter.ISO_DATE),
                        LocalDate.parse(ticketCustomerPutRequest.endDate(), DateTimeFormatter.ISO_DATE),
                        ticketCustomerPutRequest.validCounts(),
                        ticketCustomerPutRequest.paymentMethod(),
                        ticketCustomerPutRequest.price(),
                        ticketCustomerPutRequest.paidPrice(),
                        ticketCustomerPutRequest.type(),
                        ticketCustomerPutRequest.description(),
                        ticketCustomerPutRequest.customJson()
                );
                break;
            }
        }

        this.ticketCustomerRepository.save(gotTicketCustomer);
        return "성공적으로 " + customerFeignResponse.name() +  " 고객의 이용권이 수정 되었습니다.";
    }
    @Transactional
    @Override
    public String deleteTicketCustomer(UUID userId, UUID ticketCustomerId) {
        Optional<TicketCustomer> ticketCustomer = this.ticketCustomerRepository.findById(ticketCustomerId);
        if (!ticketCustomer.isPresent()) {
            throw new DataNotFoundException("해당 이용권을 찾을 수 없습니다.");
        }
        CustomerFeignResponse customerFeignResponse = customerClient.getCustomerInfo(userId.toString(), ticketCustomer.get().getCustomerId());
        if (customerFeignResponse == null) {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        if (!customerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("해당 이용권을 삭제할 권한이 없습니다.");
        }
        TicketCustomer gotTicketCustomer = ticketCustomer.get();
        if (gotTicketCustomer.getType().getKey().equals("DELETED")) {
            throw new CustomException("이미 삭제된 고객 이용권입니다.");
        }
        Optional<Ticket> ticket = this.ticketRepository.findById(gotTicketCustomer.getTicket().getId());
        if (!ticket.get().getCenterId().equals(gotTicketCustomer.getCenterId())) {
            throw new PermissionFailException("해당 이용권은 해당 센터의 이용권이 아닙니다.");
        }
        gotTicketCustomer.deleteTicketCustomer();
        this.ticketCustomerRepository.save(gotTicketCustomer);
        return "성공적으로 " + customerFeignResponse.name() +  " 고객으로부터 이용권이 제거 되었습니다.";
    }
    @Override
    public CenterNewCustomerResponse getTicketCustomersIsValid(UUID userId, UUID centerId, List<Long> customerIds) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId.toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 조회할 권한이 없습니다.");
        }
        List<TicketCustomer> validTicketCustomers = this.ticketCustomerRepository.findAllByCenterIdAndCustomerIdInAndType(centerId, customerIds, TicketCustomerType.VALID);
        Long validCounts = validTicketCustomers.stream().map(TicketCustomer::getCustomerId).distinct().count();
        List<TicketCustomer> invalidTicketCustomers = this.ticketCustomerRepository.findAllByCenterIdAndCustomerIdInAndType(centerId, customerIds, TicketCustomerType.INVALID);
        Long invalidCounts = invalidTicketCustomers.stream().map(TicketCustomer::getCustomerId).distinct().count();
        CenterNewCustomerResponse centerNewCustomerResponse = CenterNewCustomerResponse.makeDto(Long.valueOf(customerIds.size()), validCounts, invalidCounts);
        return centerNewCustomerResponse;
    }
    @Override
    public List<TicketSalesResponse> getTicketSales(UUID userId, UUID centerId, String startDate, String endDate) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId.toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 조회할 권한이 없습니다.");
        }
        List<Ticket> tickets = ticketRepository.findAllByCenterIdAndStatusNotIn(centerId, List.of(TicketStatus.DELETE));
        List<TicketCustomer> ticketCustomers = this.ticketCustomerRepository.findAllByCenterIdAndTicketInAndCreatedAtAfterAndCreatedAtBeforeAndTypeNotIn(centerId, tickets, LocalDateTime.of(LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE), LocalTime.MIN), LocalDateTime.of(LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE), LocalTime.MAX), List.of(TicketCustomerType.REFUND));
        Map<Ticket, String> totalSales = ticketCustomers.stream().collect(
                Collectors.groupingBy(
                        TicketCustomer::getTicket,
                        Collectors.mapping(TicketCustomer::getPaidPrice, Collectors.reducing("0", (a, b) -> String.valueOf(Integer.parseInt(a) + Integer.parseInt(b))))
                )
        );

        List<TicketSalesResponse> ticketSalesResponse = TicketSalesResponse.makeDto(totalSales);
        return ticketSalesResponse;
    }

    @Override
    public List<RouteSalseResponse> getRouteTicketSales(UUID userId, UUID centerId, String routeAndId) throws JsonProcessingException {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId.toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 조회할 권한이 없습니다.");
        }
        Map<String, List<Long>> responseMap = objectMapper.readValue(routeAndId, Map.class);

        List<Ticket> tickets = ticketRepository.findAllByCenterIdAndStatusNotIn(centerId, List.of(TicketStatus.DELETE));
        List<RouteSalseResponse> routeSalseResponses = new ArrayList<>();
        for (String route : responseMap.keySet()) {
            List<TicketCustomer> ticketCustomers = this.ticketCustomerRepository.findAllByCenterIdAndCustomerIdInAndTicketInAndTypeNotIn(centerId, responseMap.get(route), tickets, List.of(TicketCustomerType.REFUND));
            Map<Ticket, String> routeSales = ticketCustomers.stream().collect(
                    Collectors.groupingBy(
                            TicketCustomer::getTicket,
                            Collectors.mapping(TicketCustomer::getPaidPrice, Collectors.reducing("0", (a, b) -> String.valueOf(Integer.parseInt(a) + Integer.parseInt(b)))
                    )
            ));
            String routePaid = ticketCustomers.stream().map(TicketCustomer::getPaidPrice).reduce("0", (a, b) -> String.valueOf(Integer.parseInt(a) + Integer.parseInt(b)));
//            List<RouteTicketSalesResponse> routeTicketSalesResponse = RouteTicketSalesResponse.makeDto(routePaid);
            RouteSalseResponse routeSalseResponse = new RouteSalseResponse(route, routePaid);
            routeSalseResponses.add(routeSalseResponse);
        }

        return routeSalseResponses;
    }
}
