package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.client.UserClient;
import com.olim.reserveservice.dto.request.AttendCheckRequest;
import com.olim.reserveservice.dto.response.AttendGetListResponse;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.dto.response.CustomerFeignResponse;
import com.olim.reserveservice.entity.Attend;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.entity.TicketCustomer;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketType;
import com.olim.reserveservice.exception.customexception.DataNotFoundException;
import com.olim.reserveservice.exception.customexception.PermissionFailException;
import com.olim.reserveservice.repository.AttendRepository;
import com.olim.reserveservice.repository.TicketCustomerRepository;
import com.olim.reserveservice.repository.TicketRepository;
import com.olim.reserveservice.service.AttendService;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendServiceImpl implements AttendService {
    private final CustomerClient customerClient;
    private final UserClient userClient;
    private final TicketRepository ticketRepository;
    private final TicketCustomerRepository ticketCustomerRepository;
    private final AttendRepository attendRepository;
    @Override
    @Transactional
    public String attend(UUID userId, AttendCheckRequest attendCheckRequest) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), attendCheckRequest.centerId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("출석체크할 권한이 없습니다.");
        }
        CustomerFeignResponse customerFeignResponse = customerClient.getCustomerInfo(userId.toString(), attendCheckRequest.customerId());
        if (customerFeignResponse == null) {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        Optional<TicketCustomer> ticketCustomer = this.ticketCustomerRepository.findByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartDateAfterAndEndDateBeforeAndStartTimeAfterAndEndTimeBeforeAndValidCountsGreaterThan(
                attendCheckRequest.centerId(),
                attendCheckRequest.customerId(),
                TicketCustomerType.VALID,
                TicketType.GYM,
                LocalDate.now(),
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                0
        );
        if (!ticketCustomer.isPresent()) {
            throw new DataNotFoundException("해당 고객의 유효한 이용권을 찾을 수 없습니다.");
        }
        TicketCustomer gotTicektCustomer = ticketCustomer.get();
        Attend attend = Attend.builder()
                        .centerId(attendCheckRequest.centerId())
                        .customerId(attendCheckRequest.customerId())
                        .customerName(customerFeignResponse.name())
                        .build();
        this.attendRepository.save(attend);
        if (gotTicektCustomer.getValidCounts() != null) {
            gotTicektCustomer.updateValidCounts(gotTicektCustomer.getValidCounts() - 1);
            this.ticketCustomerRepository.save(gotTicektCustomer);
        }

        return "출석체크 완료";
    }

    @Override
    public AttendGetListResponse getAttendList(UUID userId, String centerId, int page, int count, String sortBy, String date, String keyword, Boolean orderByDesc) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId);
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("출석체크를 조회할 권한이 없습니다.");
        }
        if (date.equals("")) {
            date = LocalDate.now().toString();
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        if (orderByDesc) {
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        }
        Pageable pageable = PageRequest.of(page, count, sort);
        Page<Attend> ticketCustomers = attendRepository.findAllByCenterIdAndCAtAfterAndCAtBeforeAndCustomerNameContaining(
                UUID.fromString(centerId),
                LocalDate.parse(date),
                LocalDate.parse(date),
                keyword,
                pageable
        );
        AttendGetListResponse attendGetListResponse = AttendGetListResponse.makeDto(ticketCustomers);
        return attendGetListResponse;
    }
}
