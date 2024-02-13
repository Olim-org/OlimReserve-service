package com.olim.reserveservice.controller;

import com.olim.reserveservice.dto.request.GymTicketCreateRequest;
import com.olim.reserveservice.dto.response.GymTicketGetListResonse;
import com.olim.reserveservice.service.GymTicketService;
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
@RequestMapping("/reserve-service/ticket/gym")
@Validated
public class GymTicketController {
    private final GymTicketService gymTicketService;
    @PostMapping("/create")
    @Operation(description = "헬스장 이용권 등록")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER)
    })
    public ResponseEntity<String> createGymTicket(
            @RequestHeader("id") String userId,
            @RequestBody GymTicketCreateRequest gymTicketCreateRequest
    ) {
        return new ResponseEntity<>(this.gymTicketService.createGymTicket(UUID.fromString(userId), gymTicketCreateRequest), HttpStatus.OK);
    }
    @GetMapping("/list")
    @Operation(description = "헬스장 이용권 전체 조회")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, required = true)
    })
    public ResponseEntity<GymTicketGetListResonse> getTicketList(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId
    ) {
        return new ResponseEntity<>(this.gymTicketService.getTicketList(UUID.fromString(userId), UUID.fromString(centerId)), HttpStatus.OK);
    }
}
