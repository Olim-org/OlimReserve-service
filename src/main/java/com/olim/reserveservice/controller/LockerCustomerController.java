package com.olim.reserveservice.controller;

import com.olim.reserveservice.dto.request.LockerCustomerCreateRequest;
import com.olim.reserveservice.dto.response.LockerCustomerGetListResponse;
import com.olim.reserveservice.service.LockerCustomerService;
import feign.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/reserve-service/locker-customer")
@Tag(name = "LockerCustomer", description = "LockerCustomer API 구성")
public class LockerCustomerController {
    private final LockerCustomerService lockerCustomerService;
    @PostMapping("/create")
    @Operation(summary = "사물함 고객 생성", description = "사물함 고객을 생성하는 엔드포인트")
    public ResponseEntity<String> createLockerCustomer(
            @RequestHeader("id") String userId,
            @RequestBody LockerCustomerCreateRequest lockerCustomerCreateRequest
    ) {
        return new ResponseEntity<>(this.lockerCustomerService.createLockerCustomer(userId, lockerCustomerCreateRequest), HttpStatus.OK);
    }
//    @GetMapping("/{lockerId}")
//    @Operation(summary = "사물함 고객 조회", description = "사물함의 모든 고객을 조회하는 엔드포인트")
//    public ResponseEntity<LockerCustomerGetListResponse> getLockerCustomerList(
//            @RequestHeader("id") String userId,
//            @PathVariable String lockerId
//    ) {
//        return ResponseEntity.ok(this.lockerCustomerService.getLockerCustomerList(userId, lockerId));
//    }
}
