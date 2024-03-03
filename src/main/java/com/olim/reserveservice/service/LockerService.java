package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.LockerCreateRequest;
import org.springframework.transaction.annotation.Transactional;

public interface LockerService {
    @Transactional
    String createLocker(String userId, LockerCreateRequest lockerCreateRequest);

}
