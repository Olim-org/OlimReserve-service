package com.olim.reserveservice.controller;

import com.olim.reserveservice.dto.request.TicketCustomerGiveRequest;
import com.olim.reserveservice.dto.response.TicketCustomerGetListResponse;
import com.olim.reserveservice.service.TicketCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "헬스장 이용권 구입", description = "헬스장 이용권 구입 관련 API 구성")
@RequestMapping("/reserve-service/ticket-customer")
@Validated
public class TicketCustomerController {
    private final TicketCustomerService ticketCustomerService;
    @PostMapping("/give")
    @Operation(description = "이용권 주기")
    public ResponseEntity<String> giveTicket(
            @RequestHeader("id") String userId,
            @RequestHeader("Authorization") String token,
            @RequestBody TicketCustomerGiveRequest ticketCustomerGiveRequest
    ) {
        return ResponseEntity.ok(this.ticketCustomerService.giveTicket(ticketCustomerGiveRequest, UUID.fromString(userId), token));
    }
    @GetMapping("/list")
    @Operation(description = "이용권 구매자 목록 가져오기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "page", description = "페이지", in = ParameterIn.QUERY, required = false, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", in = ParameterIn.QUERY, required = false, example = "20"),
            @Parameter(name = "name", description = "검색어", in = ParameterIn.QUERY, example = "김영웅"),
            @Parameter(name = "sortBy", description = "정렬 기준", in = ParameterIn.QUERY, example = "title"),
            @Parameter(name = "orderByDesc", description = "내림차순", in = ParameterIn.QUERY, example = "true")
    })
    public ResponseEntity<TicketCustomerGetListResponse> getListTicketCustomer(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "50") int count,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "sortBy", defaultValue = "cAt") String sortBy,
            @RequestParam(value = "orderByDesc", defaultValue = "true") Boolean orderByDesc,
            @RequestParam(value = "type", defaultValue = "GYM") String type
    ) {
        return ResponseEntity.ok(this.ticketCustomerService.getListTicketCustomer(UUID.fromString(userId), centerId, page, count, name, sortBy, orderByDesc, type));
    }

}
