package com.olim.reserveservice.service;

import com.olim.reserveservice.dto.request.AttendByPhoneRequest;
import com.olim.reserveservice.dto.request.AttendCheckRequest;
import com.olim.reserveservice.dto.request.AttendModifyRequest;
import com.olim.reserveservice.dto.response.AttendGetListResponse;
import com.olim.reserveservice.dto.response.AttendTimeGraphResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AttendService {
    @Transactional
    String attend(UUID userId, AttendCheckRequest attendCheckRequest);
    @Transactional
    ResponseEntity<?> attend(UUID userId, AttendByPhoneRequest attendCheckRequest);
    AttendGetListResponse getAttendList(UUID userId, String centerId, int page, int count, String sortBy, String startDate, String endDate, String ticket, String isBlack, String keyword, Boolean orderByDesc);
    AttendTimeGraphResponse getTimeGraph(UUID userId, String centerId, String startDate, String endDate);
    String updateAttend(UUID userId, AttendModifyRequest attendModifyRequest);

}
