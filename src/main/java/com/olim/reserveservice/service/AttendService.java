package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.AttendCheckRequest;
import com.olim.reserveservice.dto.response.AttendGetListResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AttendService {
    @Transactional
    String attend(UUID userId, AttendCheckRequest attendCheckRequest);
    AttendGetListResponse getAttendList(UUID userId, String centerId, int page, int count, String sortBy, String date, String keyword, Boolean orderByDesc);
}
