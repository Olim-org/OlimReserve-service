package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.dto.request.LockerCreateRequest;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.entity.Locker;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.exception.customexception.CustomException;
import com.olim.reserveservice.exception.customexception.DataNotFoundException;
import com.olim.reserveservice.exception.customexception.PermissionFailException;
import com.olim.reserveservice.repository.LockerRepository;
import com.olim.reserveservice.service.LockerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockerServiceImpl implements LockerService {
    private final LockerRepository lockerRepository;
    private final CustomerClient customerClient;
    @Transactional
    @Override
    public String createLocker(String userId, LockerCreateRequest lockerCreateRequest) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), lockerCreateRequest.centerId().toString());
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("락커를 생성할 권한이 없습니다.");
        }
        Locker locker = Locker.builder()
                .centerId(lockerCreateRequest.centerId())
                .name(lockerCreateRequest.name())
                .description(lockerCreateRequest.description())
                .hexColor(lockerCreateRequest.hexColor())
                .customJson(lockerCreateRequest.customJson())
                .build();
        lockerRepository.save(locker);
        return "락커가 생성되었습니다.";
    }

}
