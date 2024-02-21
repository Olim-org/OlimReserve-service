package com.olim.reserveservice.controller;

import com.olim.reserveservice.client.CustomerClient;
import com.olim.reserveservice.client.UserClient;
import com.olim.reserveservice.dto.request.AttendByPhoneRequest;
import com.olim.reserveservice.dto.request.AttendCheckRequest;
import com.olim.reserveservice.dto.request.AttendModifyRequest;
import com.olim.reserveservice.dto.response.AttendGetListResponse;
import com.olim.reserveservice.dto.response.AttendTimeGraphResponse;
import com.olim.reserveservice.service.AttendService;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reserve-service/attend")
@Tag(name = "출석 체크", description = "출석 체크 관련 API 구성")
@Validated
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
    @PostMapping("/pcheck")
    @Operation(description = "휴대폰 출석 체크")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER)
    })
    public ResponseEntity<?> checkAttend(
            @RequestHeader("id") String userId,
            @RequestBody AttendByPhoneRequest attendByPhoneRequest
    ) {
        return this.attendService.attend(UUID.fromString(userId), attendByPhoneRequest);
    }
    @GetMapping("/list")
    @Operation(description = "센터 내 출석 목록 조회")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "page", description = "페이지", in = ParameterIn.QUERY, required = false, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", in = ParameterIn.QUERY, required = false, example = "20"),
            @Parameter(name = "sortBy", description = "정렬 기준", in = ParameterIn.QUERY, example = "cAt"),
            @Parameter(name = "startDate", description = "시작 날짜", in = ParameterIn.QUERY, example = "2021-01-01"),
            @Parameter(name = "endDate", description = "종료 날짜", in = ParameterIn.QUERY, example = "2021-12-31"),
            @Parameter(name = "ticket", description = "티켓 UUID", in = ParameterIn.QUERY, example = "asdasf-qweqw-czxc"),
            @Parameter(name = "isBlack", description = "블랙컨슈머 여부", in = ParameterIn.QUERY, example = "YES OR NO 빈칸 가능"),
            @Parameter(name = "keyword", description = "이름 검색어", in = ParameterIn.QUERY, example = "홍길동"),
            @Parameter(name = "orderByDesc", description = "내림차순", in = ParameterIn.QUERY, example = "true")
    })
    public ResponseEntity<AttendGetListResponse> getAttendList(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "50") int count,
            @RequestParam(value = "sortBy", defaultValue = "cAt") String sortBy,
            @RequestParam(value = "startDate", defaultValue = "") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") String endDate,
            @RequestParam(value = "ticket", defaultValue = "") String ticket,
            @RequestParam(value = "isBlack", defaultValue = "") String isBlack,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "orderByDesc", defaultValue = "true") Boolean orderByDesc
    ) {
        return new ResponseEntity<>(this.attendService.getAttendList(
                UUID.fromString(userId),
                centerId,
                page,
                count,
                sortBy,
                startDate,
                endDate,
                ticket,
                isBlack,
                keyword,
                orderByDesc), HttpStatus.OK);
    }
    @GetMapping("/timeGraph")
    @Operation(description = "센터 내 출석 시간대별 그래프")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "startDate", description = "시작 날짜", in = ParameterIn.QUERY, example = "2021-01-01"),
            @Parameter(name = "endDate", description = "종료 날짜", in = ParameterIn.QUERY, example = "2021-12-31")
    })
    public ResponseEntity<AttendTimeGraphResponse> getAttendTimeGraph(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "startDate", defaultValue = "") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") String endDate
    ) {
        return new ResponseEntity<>(this.attendService.getTimeGraph(UUID.fromString(userId), centerId, startDate, endDate), HttpStatus.OK);
    }

//    @PutMapping("/update")
//    @Operation(description = "출석 정보 수정")
//    @Parameters({
//            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER)
//    })
//    public ResponseEntity<String> updateAttend(
//            @RequestHeader("id") String userId,
//            @RequestBody AttendModifyRequest attendModifyRequest
//    ) {
//        return new ResponseEntity<>(this.attendService.updateAttend(UUID.fromString(userId), attendCheckRequest), HttpStatus.OK);
//    }
}
