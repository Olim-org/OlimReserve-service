package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.client.UserClient;
import com.olim.reserveservice.dto.request.AttendByPhoneRequest;
import com.olim.reserveservice.dto.request.AttendCheckRequest;
import com.olim.reserveservice.dto.request.AttendModifyRequest;
import com.olim.reserveservice.dto.request.CustomerAttendFeignRequest;
import com.olim.reserveservice.dto.response.*;
import com.olim.reserveservice.entity.Attend;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.entity.TicketCustomer;
import com.olim.reserveservice.enumeration.BlackConsumer;
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
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        Optional<TicketCustomer> infiniteTicketCustomer = this.ticketCustomerRepository.findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartDateBeforeAndEndDateAfterAndStartTimeBeforeAndEndTimeAfterAndValidCountsLessThanOrderByStartDateDesc(
                attendCheckRequest.centerId(),
                customerFeignResponse.id(),
                TicketCustomerType.VALID,
                TicketType.GYM,
                LocalDate.now().plusDays(1),
                LocalDate.now().minusDays(1),
                LocalTime.now().plusMinutes(1),
                LocalTime.now().minusMinutes(1),
                -5
        );
        if (infiniteTicketCustomer.isPresent()) {
            TicketCustomer gotTicektCustomer = infiniteTicketCustomer.get();

            Attend attend = Attend.builder()
                    .centerId(attendCheckRequest.centerId())
                    .customerId(customerFeignResponse.id())
                    .customerName(customerFeignResponse.name())
                    .ticketCustomer(gotTicektCustomer)
                    .ticket(gotTicektCustomer.getTicket())
                    .attendTime(LocalDateTime.now())
                    .blackConsumer(gotTicektCustomer.getEndTime().isBefore(LocalTime.now().plusMinutes(30)) ? BlackConsumer.YES : BlackConsumer.NO)
                    .timeGraph(LocalTime.now())
                    .build();
            this.attendRepository.save(attend);
            CustomerAttendFeignRequest customerAttendFeignRequest = CustomerAttendFeignRequest.makeDto(
                    gotTicektCustomer.getCustomerId(),
                    attend.getId(),
                    attend.getBlackConsumer().getKey().equals("YES") ? true : false);
            customerClient.attend(userId.toString(), customerAttendFeignRequest);
        } else {
            Optional<TicketCustomer> ticketCustomer = this.ticketCustomerRepository.findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartTimeGreaterThanEndTime(
                    attendCheckRequest.centerId(),
                    customerFeignResponse.id(),
                    TicketCustomerType.VALID,
                    TicketType.GYM,
                    LocalDate.now().plusDays(1),
                    LocalDate.now().minusDays(1),
                    LocalTime.now().plusMinutes(1),
                    LocalTime.now().minusMinutes(1),
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
                    .customerId(customerFeignResponse.id())
                    .customerName(customerFeignResponse.name())
                    .ticketCustomer(gotTicektCustomer)
                    .ticket(gotTicektCustomer.getTicket())
                    .attendTime(LocalDateTime.now())
                    .blackConsumer(gotTicektCustomer.getEndTime().isBefore(LocalTime.now().plusMinutes(30)) ? BlackConsumer.YES : BlackConsumer.NO)
                    .timeGraph(LocalTime.now())
                    .build();
            this.attendRepository.save(attend);
            CustomerAttendFeignRequest customerAttendFeignRequest = CustomerAttendFeignRequest.makeDto(
                    gotTicektCustomer.getCustomerId(),
                    attend.getId(),
                    attend.getBlackConsumer().getKey().equals("YES") ? true : false);
            customerClient.attend(userId.toString(), customerAttendFeignRequest);
        }
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
            Optional<TicketCustomer> infiniteTicketCustomer = this.ticketCustomerRepository.findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartDateBeforeAndEndDateAfterAndStartTimeBeforeAndEndTimeAfterAndValidCountsLessThanOrderByStartDateDesc(
                    attendByPhoneRequest.centerId(),
                    customerFeignResponse.hits().get(0).id(),
                    TicketCustomerType.VALID,
                    TicketType.GYM,
                    LocalDate.now().plusDays(1),
                    LocalDate.now().minusDays(1),
                    LocalTime.now().plusMinutes(1),
                    LocalTime.now().minusMinutes(1),
                    -5
            );
            if (infiniteTicketCustomer.isPresent()) {
                TicketCustomer gotTicektCustomer = infiniteTicketCustomer.get();

                Attend attend = Attend.builder()
                        .centerId(attendByPhoneRequest.centerId())
                        .customerId(customerFeignResponse.hits().get(0).id())
                        .customerName(customerFeignResponse.hits().get(0).name())
                        .ticketCustomer(gotTicektCustomer)
                        .ticket(gotTicektCustomer.getTicket())
                        .attendTime(LocalDateTime.now())
                        .blackConsumer(gotTicektCustomer.getEndTime().isBefore(LocalTime.now().plusMinutes(30)) ? BlackConsumer.YES : BlackConsumer.NO)
                        .timeGraph(LocalTime.now())
                        .build();
                this.attendRepository.save(attend);
                CustomerAttendFeignRequest customerAttendFeignRequest = CustomerAttendFeignRequest.makeDto(
                        gotTicektCustomer.getCustomerId(),
                        attend.getId(),
                        attend.getBlackConsumer().getKey().equals("YES") ? true : false);
                customerClient.attend(userId.toString(), customerAttendFeignRequest);
            } else {
                Optional<TicketCustomer> ticketCustomer = this.ticketCustomerRepository.findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartTimeGreaterThanEndTime(
                        attendByPhoneRequest.centerId(),
                        customerFeignResponse.hits().get(0).id(),
                        TicketCustomerType.VALID,
                        TicketType.GYM,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().minusDays(1),
                        LocalTime.now().plusMinutes(1),
                        LocalTime.now().minusMinutes(1),
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
                        .ticket(gotTicektCustomer.getTicket())
                        .attendTime(LocalDateTime.now())
                        .blackConsumer(gotTicektCustomer.getEndTime().isBefore(LocalTime.now().plusMinutes(30)) ? BlackConsumer.YES : BlackConsumer.NO)
                        .timeGraph(LocalTime.now())
                        .build();
                this.attendRepository.save(attend);
                CustomerAttendFeignRequest customerAttendFeignRequest = CustomerAttendFeignRequest.makeDto(
                        gotTicektCustomer.getCustomerId(),
                        attend.getId(),
                        attend.getBlackConsumer().getKey().equals("YES") ? true : false);
                customerClient.attend(userId.toString(), customerAttendFeignRequest);
            }

        }
            return new ResponseEntity<>(customerFeignResponse, HttpStatus.OK);
    }
    @Transactional
    @Override
    public AttendGetListResponse getAttendList(UUID userId, String centerId, int page, int count, String sortBy, String startDate, String endDate, String ticket, String isBlack, String keyword, Boolean orderByDesc) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId);
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("출석체크를 조회할 권한이 없습니다.");
        }
        if (startDate.equals("")){
            startDate = "2000-01-01";
            endDate = LocalDate.now().toString();
        } else if (endDate.equals("")) {
            endDate = LocalDate.now().toString();
        }
        if (startDate.equals("") && endDate.equals("")) {
            startDate = "2000-01-01";
            endDate = LocalDate.now().toString();
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        if (orderByDesc) {
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        }
        Pageable pageable = PageRequest.of(page, count, sort);
        if (ticket.equals("")) {
            if (!isBlack.equals("")) {
                Page<Attend> ticketCustomers = attendRepository.findAllByCenterIdAndBlackConsumerAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(
                        UUID.fromString(centerId),
                        BlackConsumer.valueOf(isBlack),
                        LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIN),
                        LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX),
                        keyword,
                        pageable
                );
                AttendGetListResponse attendGetListResponse = AttendGetListResponse.makeDto(ticketCustomers);
                return attendGetListResponse;
            }
            Page<Attend> ticketCustomers = attendRepository.findAllByCenterIdAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(
                    UUID.fromString(centerId),
                    LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIN),
                    LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX),
                    keyword,
                    pageable
            );
            AttendGetListResponse attendGetListResponse = AttendGetListResponse.makeDto(ticketCustomers);
            return attendGetListResponse;
        }
        Optional<Ticket> ticketOptional = ticketRepository.findById(UUID.fromString(ticket));
        if (!ticketOptional.isPresent()) {
            throw new DataNotFoundException("해당 티켓을 찾을 수 없습니다.");
        }
        Ticket gotTicket = ticketOptional.get();
        if (!isBlack.equals("")) {
            Page<Attend> ticketCustomers = attendRepository.findAllByCenterIdAndTicketAndBlackConsumerAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(
                    UUID.fromString(centerId),
                    gotTicket,
                    BlackConsumer.valueOf(isBlack),
                    LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIN),
                    LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX),
                    keyword,
                    pageable
            );
            AttendGetListResponse attendGetListResponse = AttendGetListResponse.makeDto(ticketCustomers);
            return attendGetListResponse;
        }
        Page<Attend> ticketCustomers = attendRepository.findAllByCenterIdAndTicketAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(
                UUID.fromString(centerId),
                gotTicket,
                LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIN),
                LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX),
                keyword,
                pageable
        );
        AttendGetListResponse attendGetListResponse = AttendGetListResponse.makeDto(ticketCustomers);
        return attendGetListResponse;
    }

    @Override
    public AttendTimeGraphResponse getTimeGraph(UUID userId, String centerId, String startDate, String endDate) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId);
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("출석체크 시간 그래프를 조회할 권한이 없습니다.");
        }
        if (startDate.equals("") && endDate.equals("")) {
            startDate = LocalDate.now().toString();
            endDate = LocalDate.now().toString();
        }
        List<AttendTimeResponse> attendTimeResponses = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String startTime = String.format("%02d", i);
            String endtime = String.format("%02d", i+1);
            Integer count = attendRepository.countAllByCenterIdAndAttendTimeAfterAndAttendTimeBeforeAndTimeGraphAfterAndTimeGraphBefore(
                    UUID.fromString(centerId),
                    LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIN),
                    LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX),
                    LocalTime.of(i, 0),
                    LocalTime.of(i, 59)
            );
            AttendTimeResponse attendTimeResponse = AttendTimeResponse.makeDto(startTime, count);
            attendTimeResponses.add(attendTimeResponse);
        }
        AttendTimeGraphResponse attendTimeGraphResponse = new AttendTimeGraphResponse(attendTimeResponses.size(), attendTimeResponses);
        return attendTimeGraphResponse;
    }

    @Override
    public String updateAttend(UUID userId, AttendModifyRequest attendModifyRequest) {
        Optional<Attend> attendance = attendRepository.findById(attendModifyRequest.attendId());
        if (!attendance.isPresent()) {
            throw new DataNotFoundException("해당 출석체크를 찾을 수 없습니다.");
        }

        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), attendance.get().getCenterId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("출석체크를 수정할 권한이 없습니다.");
        }
        attendance.get().updateAttendTime(LocalDateTime.parse(attendModifyRequest.attendTime(), DateTimeFormatter.ISO_DATE_TIME) );
        attendRepository.save(attendance.get());
        return "출석체크 수정 완료";
    }
}
