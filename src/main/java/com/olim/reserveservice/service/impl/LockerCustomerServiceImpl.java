package com.olim.reserveservice.service.impl;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.dto.request.LockerCustomerCreateRequest;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.dto.response.CustomerFeignResponse;
import com.olim.reserveservice.entity.Locker;
import com.olim.reserveservice.entity.LockerCustomer;
import com.olim.reserveservice.enumeration.LockerCustomerStatus;
import com.olim.reserveservice.exception.customexception.CustomException;
import com.olim.reserveservice.exception.customexception.DataNotFoundException;
import com.olim.reserveservice.exception.customexception.PermissionFailException;
import com.olim.reserveservice.repository.LockerCustomerRepository;
import com.olim.reserveservice.repository.LockerRepository;
import com.olim.reserveservice.service.LockerCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockerCustomerServiceImpl implements LockerCustomerService {
    private final LockerCustomerRepository lockerCustomerRepository;
    private final LockerRepository lockerRepository;
    private final CustomerClient customerClient;
    @Transactional
    @Override
    public String createLockerCustomer(String userId, LockerCustomerCreateRequest lockerCustomerCreateRequest) {
        CenterFeignResponse centerFeignResponse = customerClient.getCenterInfo(userId.toString(), lockerCustomerCreateRequest.centerId().toString());
        Optional<Locker> locker = lockerRepository.findById(lockerCustomerCreateRequest.lockerId());
        if (!locker.isPresent()) {
            throw new DataNotFoundException("해당 락커를 찾을 수 없습니다.");
        }
        if (centerFeignResponse == null) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!centerFeignResponse.owner().equals(userId)) {
            throw new PermissionFailException("락커에 고객을 등록할 권한이 없습니다.");
        }
        CustomerFeignResponse customerFeignResponse = customerClient.getCustomerInfo(userId.toString(), lockerCustomerCreateRequest.customerId());
        if (customerFeignResponse == null) {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        Boolean isExist = lockerCustomerRepository.existsByLockerAndStatusAndStartDateAndEndDate(lockerCustomerCreateRequest.lockerId(), LockerCustomerStatus.VALID, LocalDate.parse(lockerCustomerCreateRequest.startDate()), LocalDate.parse(lockerCustomerCreateRequest.endDate()));
        if (isExist) {
            throw new CustomException("해당 기간에 이미 사용 중인 락커입니다.");
        }
        LockerCustomer lockerCustomer = LockerCustomer.builder()
                .locker(locker.get())
                .centerId(lockerCustomerCreateRequest.centerId())
                .customerId(lockerCustomerCreateRequest.customerId())
                .status(LockerCustomerStatus.VALID)
                .startDate(LocalDate.parse(lockerCustomerCreateRequest.startDate()))
                .endDate(LocalDate.parse(lockerCustomerCreateRequest.endDate()))
                .customerName(customerFeignResponse.name())
                .hexColor(lockerCustomerCreateRequest.hexColor())
                .customJson(lockerCustomerCreateRequest.customJson())
                .build();
        lockerCustomerRepository.save(lockerCustomer);
        return "성공적으로 " + lockerCustomer.getCustomerName() + "님이 기간"+ lockerCustomer.getStartDate() + " ~ " + lockerCustomer.getEndDate() + " 동안" + locker.get().getName() + "락커를 사용하게 되었습니다.";
    }

}
