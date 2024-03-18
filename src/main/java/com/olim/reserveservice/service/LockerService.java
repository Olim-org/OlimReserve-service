package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.LockerCreateRequest;
import com.olim.reserveservice.dto.response.LockerGetListResponse;
import org.springframework.transaction.annotation.Transactional;

public interface LockerService {
    @Transactional
    String createLocker(String userId, LockerCreateRequest lockerCreateRequest);
    LockerGetListResponse getLockerList(String userId, String centerId, String section, String keyword, int page, int count);
}
