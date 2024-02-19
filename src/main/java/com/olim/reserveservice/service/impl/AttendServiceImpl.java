package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.client.UserClient;
import com.olim.reserveservice.dto.request.AttendByPhoneRequest;
import com.olim.reserveservice.dto.request.AttendCheckRequest;
import com.olim.reserveservice.dto.response.AttendGetListResponse;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.dto.response.CustomerFeignListResponse;
import com.olim.reserveservice.dto.response.CustomerFeignResponse;
import com.olim.reserveservice.entity.Attend;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.entity.TicketCustomer;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketType;
import com.olim.reserveservice.exception.customexception.DataNotFoundException;
import com.olim.reserveservice.exception.customexception.DuplicateException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        Optional<Attend> attendCheck = attendRepository.findTop1ByCenterIdAndCustomerIdAndAttendTimeBeforeAndAttendTimeAfterOrderByAttendTimeDesc(
                attendCheckRequest.centerId(),
                customerFeignResponse.id(),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        );
        if (attendCheck.isPresent()) {
            throw new DuplicateException("오늘은 이미 출석체크 되었습니다.");
        }
        Optional<TicketCustomer> ticketCustomer = this.ticketCustomerRepository.findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartDateBeforeAndEndDateAfterAndStartTimeBeforeAndEndTimeAfterAndValidCountsGreaterThanOrderByStartDateDesc(
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

        if (gotTicektCustomer.getValidCounts() != null) {
            gotTicektCustomer.updateValidCounts(gotTicektCustomer.getValidCounts() - 1);
            this.ticketCustomerRepository.save(gotTicektCustomer);
        }
        Attend attend = Attend.builder()
                .centerId(attendCheckRequest.centerId())
                .customerId(attendCheckRequest.customerId())
                .customerName(customerFeignResponse.name())
                .ticketCustomer(gotTicektCustomer)
                .attendTime(LocalDateTime.now())
                .build();
        this.attendRepository.save(attend);
        return "출석체크 완료";
    }
    @Transactional
    @Override
    public ResponseEntity<?> attend(UUID userId, AttendByPhoneRequest attendByPhoneRequest) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), attendByPhoneRequest.centerId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("출석 체크할 권한이 없습니다.");
        }
        CustomerFeignListResponse customerFeignResponse = customerClient.getCustomerInfo(userId.toString(), attendByPhoneRequest.phoneNumber(), attendByPhoneRequest.centerId().toString());
        if (customerFeignResponse == null) {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        if (customerFeignResponse.total() == 1) {
            Optional<Attend> attendCheck = attendRepository.findTop1ByCenterIdAndCustomerIdAndAttendTimeBeforeAndAttendTimeAfterOrderByAttendTimeDesc(
                    attendByPhoneRequest.centerId(),
                    customerFeignResponse.hits().get(0).id(),
                    LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
                    LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
            );
            if (attendCheck.isPresent()) {
                throw new DuplicateException("오늘은 이미 출석체크 되었습니다.");
            }
            Optional<TicketCustomer> infiniteTicketCustomer = this.ticketCustomerRepository.findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartDateBeforeAndEndDateAfterAndStartTimeBeforeAndEndTimeAfterAndValidCountsIsOrderByStartDateDesc(
                    attendByPhoneRequest.centerId(),
                    customerFeignResponse.hits().get(0).id(),
                    TicketCustomerType.VALID,
                    TicketType.GYM,
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalTime.now(),
                    LocalTime.now(),
                    null
            );
            if (infiniteTicketCustomer.isPresent()) {
                TicketCustomer gotTicektCustomer = infiniteTicketCustomer.get();

                Attend attend = Attend.builder()
                        .centerId(attendByPhoneRequest.centerId())
                        .customerId(customerFeignResponse.hits().get(0).id())
                        .customerName(customerFeignResponse.hits().get(0).name())
                        .ticketCustomer(gotTicektCustomer)
                        .attendTime(LocalDateTime.now())
                        .build();
                this.attendRepository.save(attend);
            } else {
                Optional<TicketCustomer> ticketCustomer = this.ticketCustomerRepository.findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartDateBeforeAndEndDateAfterAndStartTimeBeforeAndEndTimeAfterAndValidCountsGreaterThanOrderByStartDateDesc(
                        attendByPhoneRequest.centerId(),
                        customerFeignResponse.hits().get(0).id(),
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

                if (gotTicektCustomer.getValidCounts() != null) {
                    gotTicektCustomer.updateValidCounts(gotTicektCustomer.getValidCounts() - 1);
                    this.ticketCustomerRepository.save(gotTicektCustomer);
                }
                Attend attend = Attend.builder()
                        .centerId(attendByPhoneRequest.centerId())
                        .customerId(customerFeignResponse.hits().get(0).id())
                        .customerName(customerFeignResponse.hits().get(0).name())
                        .ticketCustomer(gotTicektCustomer)
                        .attendTime(LocalDateTime.now())
                        .build();
                this.attendRepository.save(attend);
            }
        }
            return new ResponseEntity<>(customerFeignResponse, HttpStatus.OK);
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
        Page<Attend> ticketCustomers = attendRepository.findAllByCenterIdAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(
                UUID.fromString(centerId),
                LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN),
                LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX),
                keyword,
                pageable
        );
        AttendGetListResponse attendGetListResponse = AttendGetListResponse.makeDto(ticketCustomers);
        return attendGetListResponse;
    }
}
