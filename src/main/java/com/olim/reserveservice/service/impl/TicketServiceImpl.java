package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.client.UserClient;
import com.olim.reserveservice.dto.request.TicketCreateRequest;
import com.olim.reserveservice.dto.request.TicketModifyRequest;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.dto.response.TicketGetListResonse;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.enumeration.TicketType;
import com.olim.reserveservice.exception.customexception.CustomException;
import com.olim.reserveservice.exception.customexception.DataNotFoundException;
import com.olim.reserveservice.exception.customexception.PermissionFailException;
import com.olim.reserveservice.repository.TicketRepository;
import com.olim.reserveservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final UserClient userClient;
    private final CustomerClient customerClient;
    @Transactional
    @Override
    public String createGymTicket(UUID userId, TicketCreateRequest ticketCreateRequest) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), ticketCreateRequest.centerId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 생성할 권한이 없습니다.");
        }
        switch (ticketCreateRequest.type()) {
            case GYM -> {
                Ticket ticket = Ticket.builder()
                        .centerId(centerFeignResponse.centerId())
                        .title(ticketCreateRequest.title())
                        .description(ticketCreateRequest.description())
                        .price(ticketCreateRequest.price())
                        .sale(ticketCreateRequest.sale())
                        .type(TicketType.GYM)
                        .applyDays(ticketCreateRequest.applyDays())
                        .validCount(null)
                        .startTime(LocalDateTime.parse(ticketCreateRequest.startTime(), DateTimeFormatter.ISO_TIME))
                        .endTime(LocalDateTime.parse(ticketCreateRequest.endTime(), DateTimeFormatter.ISO_TIME))
                        .build();
                this.ticketRepository.save(ticket);
                break;
            }
            case PT -> {
                Ticket ticket = Ticket.builder()
                        .centerId(centerFeignResponse.centerId())
                        .title(ticketCreateRequest.title())
                        .description(ticketCreateRequest.description())
                        .price(ticketCreateRequest.price())
                        .sale(ticketCreateRequest.sale())
                        .type(TicketType.PT)
                        .applyDays(ticketCreateRequest.applyDays())
                        .validCount(ticketCreateRequest.validCount())
                        .startTime(LocalDateTime.parse(ticketCreateRequest.startTime(), DateTimeFormatter.ISO_TIME))
                        .endTime(LocalDateTime.parse(ticketCreateRequest.endTime(), DateTimeFormatter.ISO_TIME))
                        .build();
                this.ticketRepository.save(ticket);
            }
        }

        return "성공적으로 이용권이 등록 되었습니다.";
    }

    @Override
    public TicketGetListResonse getTicketList(UUID userId, UUID centerId) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId.toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 조회할 권한이 없습니다.");
        }
        List<Ticket> tickets = ticketRepository.findAllByCenterIdAndStatusIsNot(centerId, TicketStatus.DELETE);
        TicketGetListResonse ticketGetListResonse = TicketGetListResonse
                .makeDto(tickets);
        return ticketGetListResonse;
    }
    @Transactional
    @Override
    public String updateTicket(UUID userId, UUID ticketId, TicketModifyRequest ticketModifyRequest) {
        if (ticketModifyRequest.ticketStatus().getKey().equals("DELETE")) {
            throw new CustomException("삭제는 수정에서 진행할 수 없습니다.");
        }
        Optional<Ticket> gymTicket = ticketRepository.findById(ticketId);
        if (!gymTicket.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 이용권이 존재하지 않습니다.");
        }
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), gymTicket.get().getCenterId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 수정할 권한이 없습니다.");
        }

        Ticket gotTicket = gymTicket.get();
        switch (gotTicket.getType()) {
            case GYM -> {
                gotTicket.updateTicket(
                        ticketModifyRequest.title(),
                        ticketModifyRequest.description(),
                        ticketModifyRequest.price(),
                        ticketModifyRequest.sale(),
                        ticketModifyRequest.applyDays(),
                        null,
                        LocalDateTime.parse(ticketModifyRequest.startTime(), DateTimeFormatter.ISO_TIME),
                        LocalDateTime.parse(ticketModifyRequest.endTime(), DateTimeFormatter.ISO_TIME),
                        ticketModifyRequest.ticketStatus()
                );
                ticketRepository.save(gotTicket);
                break;
            }
            case PT -> {
                gotTicket.updateTicket(
                        ticketModifyRequest.title(),
                        ticketModifyRequest.description(),
                        ticketModifyRequest.price(),
                        ticketModifyRequest.sale(),
                        ticketModifyRequest.applyDays(),
                        ticketModifyRequest.validCount(),
                        LocalDateTime.parse(ticketModifyRequest.startTime(), DateTimeFormatter.ISO_TIME),
                        LocalDateTime.parse(ticketModifyRequest.endTime(), DateTimeFormatter.ISO_TIME),
                        ticketModifyRequest.ticketStatus()
                );
                ticketRepository.save(gotTicket);
                break;
            }
        }
        return "성공적으로 이용권이 수정 되었습니다.";
    }
    @Transactional
    @Override
    public String deleteTicket(UUID userId, UUID ticketId) {
        Optional<Ticket> gymTicket = ticketRepository.findById(ticketId);
        if (!gymTicket.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 이용권이 존재하지 않습니다.");
        }
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), gymTicket.get().getCenterId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 삭제할 권한이 없습니다.");
        }
        Ticket gotTicket = gymTicket.get();
        gotTicket.updateStatus(TicketStatus.DELETE);
        this.ticketRepository.save(gotTicket);
        return "성공적으로 이용권이 삭제 되었습니다.";
    }
}
