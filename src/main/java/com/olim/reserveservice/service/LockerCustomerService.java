package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.LockerCustomerCreateRequest;
import org.springframework.transaction.annotation.Transactional;

public interface LockerCustomerService {
    @Transactional
    String createLockerCustomer(String userId, LockerCustomerCreateRequest lockerCustomerCreateRequest);
}
