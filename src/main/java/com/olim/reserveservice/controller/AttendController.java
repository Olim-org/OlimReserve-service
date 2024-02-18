package com.olim.reserveservice.controller;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.client.UserClient;
import com.olim.reserveservice.dto.request.AttendCheckRequest;
import com.olim.reserveservice.dto.response.AttendGetListResponse;
import com.olim.reserveservice.service.AttendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reserve-service/attend")
public class AttendController {
private final AttendService attendService;
    private final CustomerClient customerClient;
    private final UserClient userClient;
    @PostMapping("/check")
    @Operation(description = "출석 체크")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER)
    })
    public ResponseEntity<String> checkAttend(
            @RequestHeader("id") String userId,
            @RequestBody AttendCheckRequest attendCheckRequest
    ) {
        return new ResponseEntity<>(this.attendService.attend(UUID.fromString(userId), attendCheckRequest), HttpStatus.OK);
    }
    @GetMapping("/list")
    @Operation(description = "출석 목록 조회")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "page", description = "페이지", in = ParameterIn.QUERY, required = false, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", in = ParameterIn.QUERY, required = false, example = "20"),
            @Parameter(name = "sortBy", description = "정렬 기준", in = ParameterIn.QUERY, example = "cAt"),
            @Parameter(name = "date", description = "날짜", in = ParameterIn.QUERY, example = "2021-01-01"),
            @Parameter(name = "keyword", description = "검색어", in = ParameterIn.QUERY, example = "헬스장"),
            @Parameter(name = "orderByDesc", description = "내림차순", in = ParameterIn.QUERY, example = "true")
    })
    public ResponseEntity<AttendGetListResponse> getAttendList(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "50") int count,
            @RequestParam(value = "sortBy", defaultValue = "cAt") String sortBy,
            @RequestParam(value = "date", defaultValue = "") String date,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "orderByDesc", defaultValue = "true") Boolean orderByDesc
    ) {
        return new ResponseEntity<>(this.attendService.getAttendList(UUID.fromString(userId), centerId, page, count, sortBy, date, keyword, orderByDesc), HttpStatus.OK);
    }
}