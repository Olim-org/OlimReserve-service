package com.olim.reserveservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olim.reserveservice.dto.request.RouteAndIdRequest;
import com.olim.reserveservice.dto.request.TicketCustomerGiveRequest;
import com.olim.reserveservice.dto.request.TicketCustomerPutRequest;
import com.olim.reserveservice.dto.response.*;
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

import java.util.List;
import java.util.Map;
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
            @Parameter(name = "orderByDesc", description = "내림차순", in = ParameterIn.QUERY, example = "true"),
            @Parameter(name = "type", description = "이용권 타입", in = ParameterIn.QUERY, example = "GYM")
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
    @GetMapping("/list/{customerId}")
    @Operation(description = "고객의 이용권 목록 가져오기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "customerId", description = "고객 UUID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "page", description = "페이지", in = ParameterIn.QUERY, required = false, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", in = ParameterIn.QUERY, required = false, example = "20"),
            @Parameter(name = "sortBy", description = "정렬 기준", in = ParameterIn.QUERY, example = "title"),
            @Parameter(name = "orderByDesc", description = "내림차순", in = ParameterIn.QUERY, example = "true"),
            @Parameter(name = "type", description = "이용권 타입", in = ParameterIn.QUERY, example = "GYM")
    })
    public ResponseEntity<TicketCustomerGetListResponse> getListTicketCustomerByCustomerId(
            @RequestHeader("id") String userId,
            @PathVariable(value = "customerId") String customerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "50") int count,
            @RequestParam(value = "sortBy", defaultValue = "cAt") String sortBy,
            @RequestParam(value = "orderByDesc", defaultValue = "true") Boolean orderByDesc,
            @RequestParam(value = "type", defaultValue = "GYM") String type
    ) {
        return ResponseEntity.ok(this.ticketCustomerService.getTicketCustomer(UUID.fromString(userId), Long.parseLong(customerId), page, count, sortBy, orderByDesc, type));
    }
    @PutMapping("/edit/{ticketCustomerId}")
    @Operation(description = "고객 이용권 수정하기")
    public ResponseEntity<String> modifyTicketCustomer(
            @RequestHeader("id") String userId,
            @PathVariable(value = "ticketCustomerId") String ticketCustomerId,
            @RequestBody TicketCustomerPutRequest ticketCustomerGiveRequest
    ) {
        return ResponseEntity.ok(this.ticketCustomerService.modifyTicketCustomer(UUID.fromString(userId), UUID.fromString(ticketCustomerId), ticketCustomerGiveRequest));
    }
    @DeleteMapping("/delete/{ticketCustomerId}")
    @Operation(description = "고객 이용권 삭제하기")
    public ResponseEntity<String> deleteTicketCustomer(
            @RequestHeader("id") String userId,
            @PathVariable(value = "ticketCustomerId") String ticketCustomerId
    ) {
        return ResponseEntity.ok(this.ticketCustomerService.deleteTicketCustomer(UUID.fromString(userId), UUID.fromString(ticketCustomerId)));
    }
    @GetMapping("/isValid")
    @Operation(description = "센터 내 티켓 유효자 조회")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "customerIds", description = "고객 ID 리스트", in = ParameterIn.QUERY, required = true)
    })
    public ResponseEntity<CenterNewCustomerResponse> getTicketIsValid(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "customerIds") List<Long> customerIds
    ) {
        return ResponseEntity.ok(this.ticketCustomerService.getTicketCustomersIsValid(UUID.fromString(userId), UUID.fromString(centerId), customerIds));
    }
    @GetMapping("/sales")
    public ResponseEntity<List<TicketSalesResponse>> getTicketSales(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate
    ) {
        return ResponseEntity.ok(this.ticketCustomerService.getTicketSales(UUID.fromString(userId), UUID.fromString(centerId), startDate, endDate));
    }
    @GetMapping("/routeSales")
    public ResponseEntity<List<RouteSalseResponse>> getRouteTicketSales(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "routeAndId") String routeAndId
    ) throws JsonProcessingException {
        return ResponseEntity.ok(this.ticketCustomerService.getRouteTicketSales(UUID.fromString(userId), UUID.fromString(centerId), routeAndId));
    }
}
