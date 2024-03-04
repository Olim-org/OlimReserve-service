package com.olim.reserveservice.controller;

import com.olim.reserveservice.dto.request.LockerCreateRequest;
import com.olim.reserveservice.dto.response.LockerGetListResponse;
import com.olim.reserveservice.dto.response.LockerGetResponse;
import com.olim.reserveservice.service.LockerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/reserve-service/locker")
@Slf4j
@Tag(name = "Locker", description = "Locker API 구성")
@Validated
public class LockerController {
    private final LockerService lockerService;
    @PostMapping("/create")
    @Operation(summary = "Locker 생성", description = "Locker를 생성합니다.")
    @Parameters({
            @Parameter(name = "id", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER)
    })
    public ResponseEntity<String> createLocker(
            @RequestHeader(value = "id") String userId,
            @RequestBody LockerCreateRequest lockerCreateRequest
            ) {
        return new ResponseEntity<>(lockerService.createLocker(userId, lockerCreateRequest), HttpStatus.OK);
    }
    @GetMapping("/list")
    @Operation(summary = "Locker 목록 조회", description = "Locker 목록을 조회합니다.")
    @Parameters({
            @Parameter(name = "id", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", required = true, in = ParameterIn.QUERY, example = "asdf-qr-xcv-daf"),
            @Parameter(name = "section", description = "섹션", required = false, in = ParameterIn.QUERY, example = "A"),
            @Parameter(name = "keyword", description = "검색어", required = false, in = ParameterIn.QUERY, example = "김"),
            @Parameter(name = "status", description = "상태", required = false, in = ParameterIn.QUERY, example = "ALL"),
            @Parameter(name = "page", description = "페이지", required = false, in = ParameterIn.QUERY, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", required = false, in = ParameterIn.QUERY, example = "20")
    })
    public ResponseEntity<LockerGetListResponse> getLockerList(
            @RequestHeader(value = "id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "section", defaultValue = "") String section,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "status", defaultValue = "ALL") String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "30") int count
    ) {
        return new ResponseEntity<>(lockerService.getLockerList(userId, centerId, section, keyword, status, page, count), HttpStatus.OK);
    }
}
