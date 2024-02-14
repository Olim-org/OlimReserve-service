package com.olim.reserveservice.controller;

import com.olim.reserveservice.dto.request.TicketCreateRequest;
import com.olim.reserveservice.dto.request.TicketModifyRequest;
import com.olim.reserveservice.dto.response.TicketGetListResonse;
import com.olim.reserveservice.service.TicketService;
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
@Tag(name = "헬스장 이용권", description = "헬스장 이용권 관련 API 구성")
@RequestMapping("/reserve-service/ticket")
@Validated
public class TicketController {
    private final TicketService ticketService;
    @PostMapping("/create")
    @Operation(description = "헬스장 이용권 등록")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER)
    })
    public ResponseEntity<String> createGymTicket(
            @RequestHeader("id") String userId,
            @RequestBody TicketCreateRequest ticketCreateRequest
    ) {
        return new ResponseEntity<>(this.ticketService.createGymTicket(UUID.fromString(userId), ticketCreateRequest), HttpStatus.OK);
    }
    @GetMapping("/list")
    @Operation(description = "헬스장 이용권 전체 조회")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "page", description = "페이지", in = ParameterIn.QUERY, required = false, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", in = ParameterIn.QUERY, required = false, example = "20"),
            @Parameter(name = "sortBy", description = "정렬 기준", in = ParameterIn.QUERY, example = "title"),
            @Parameter(name = "orderByDesc", description = "내림차순", in = ParameterIn.QUERY, example = "true")
    })
    public ResponseEntity<TicketGetListResonse> getTicketList(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "50") int count,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "orderByDesc", defaultValue = "true") Boolean orderByDesc,
            @RequestParam(value = "type", defaultValue = "GYM") String type
    ) {
        return new ResponseEntity<>(this.ticketService.getTicketList(UUID.fromString(userId), UUID.fromString(centerId), page, count, sortBy, orderByDesc, type), HttpStatus.OK);
    }
    @PutMapping("/modify/{gymTicketId}")
    @Operation(description = "헬스장 이용권 수정")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "gymTicketId", description = "헬스장 이용권 UUID", in = ParameterIn.PATH, required = true)
    })
    public ResponseEntity<String> updateTicket(
            @RequestHeader("id") String userId,
            @PathVariable String gymTicketId,
            @RequestBody TicketModifyRequest ticketModifyRequest
    ) {
        return new ResponseEntity<>(this.ticketService.updateTicket(UUID.fromString(userId), UUID.fromString(gymTicketId), ticketModifyRequest), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{gymTicketId}")
    @Operation(description = "헬스장 이용권 삭제")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "gymTicketId", description = "헬스장 이용권 UUID", in = ParameterIn.PATH, required = true)
    })
    public ResponseEntity<String> deleteTicket(
            @RequestHeader("id") String userId,
            @PathVariable String gymTicketId
    ) {
        return new ResponseEntity<>(this.ticketService.deleteTicket(UUID.fromString(userId), UUID.fromString(gymTicketId)), HttpStatus.OK);
    }
}
