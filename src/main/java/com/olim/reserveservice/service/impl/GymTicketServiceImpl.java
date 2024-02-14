package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.client.UserClient;
import com.olim.reserveservice.dto.request.GymTicketCreateRequest;
import com.olim.reserveservice.dto.request.GymTicketModifyRequest;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.dto.response.GymTicketGetListResonse;
import com.olim.reserveservice.entity.GymTicket;
import com.olim.reserveservice.exception.customexception.CustomException;
import com.olim.reserveservice.exception.customexception.DataNotFoundException;
import com.olim.reserveservice.exception.customexception.PermissionFailException;
import com.olim.reserveservice.repository.GymTicketRepository;
import com.olim.reserveservice.service.GymTicketService;
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
public class GymTicketServiceImpl implements GymTicketService {
    private final GymTicketRepository gymTicketRepository;
    private final UserClient userClient;
    private final CustomerClient customerClient;
    @Transactional
    @Override
    public String createGymTicket(UUID userId, GymTicketCreateRequest gymTicketCreateRequest) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), gymTicketCreateRequest.centerId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 생성할 권한이 없습니다.");
        }
        GymTicket gymTicket = GymTicket.builder()
                .centerId(centerFeignResponse.centerId())
                .title(gymTicketCreateRequest.title())
                .description(gymTicketCreateRequest.description())
                .price(gymTicketCreateRequest.price())
                .sale(gymTicketCreateRequest.sale())
                .applyDays(gymTicketCreateRequest.applyDays())
                .startTime(LocalDateTime.parse(gymTicketCreateRequest.startTime(), DateTimeFormatter.ISO_TIME))
                .endTime(LocalDateTime.parse(gymTicketCreateRequest.endTime(), DateTimeFormatter.ISO_TIME))
                .build();
        return "성공적으로 이용권이 등록 되었습니다.";
    }

    @Override
    public GymTicketGetListResonse getTicketList(UUID userId, UUID centerId) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), centerId.toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("이용권을 조회할 권한이 없습니다.");
        }
        List<GymTicket> gymTickets = gymTicketRepository.findAllByCenterId(centerId);
        GymTicketGetListResonse gymTicketGetListResonse = GymTicketGetListResonse
                .makeDto(gymTickets);
        return gymTicketGetListResonse;
    }
    @Transactional
    @Override
    public String updateTicket(UUID userId, UUID ticketId, GymTicketModifyRequest gymTicketModifyRequest) {
        if (gymTicketModifyRequest.ticketStatus().getKey().equals("DELETE")) {
            throw new CustomException("삭제는 수정에서 진행할 수 없습니다.");
        }
        Optional<GymTicket> gymTicket = gymTicketRepository.findById(ticketId);
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
        GymTicket gotGymTicket = gymTicket.get();
        gotGymTicket.updateTicket(
                gymTicketModifyRequest.title(),
                gymTicketModifyRequest.description(),
                gymTicketModifyRequest.price(),
                gymTicketModifyRequest.sale(),
                gymTicketModifyRequest.applyDays(),
                LocalDateTime.parse(gymTicketModifyRequest.startTime(), DateTimeFormatter.ISO_TIME),
                LocalDateTime.parse(gymTicketModifyRequest.endTime(), DateTimeFormatter.ISO_TIME),
                gymTicketModifyRequest.ticketStatus()
        );
        return "성공적으로 이용권이 수정 되었습니다.";
    }
}
