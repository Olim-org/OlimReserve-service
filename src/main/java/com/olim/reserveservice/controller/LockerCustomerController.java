package com.olim.reserveservice.controller;

import com.olim.reserveservice.dto.request.LockerCustomerCreateRequest;
import com.olim.reserveservice.service.LockerCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/reserve-service/locker-customer")
public class LockerCustomerController {
    private final LockerCustomerService lockerCustomerService;
    @PostMapping("/create")
    @Operation(summary = "사물함 고객 생성", description = "사물함 고객을 생성하는 엔드포인트")
    public void createLockerCustomer(
            @RequestHeader("id") String userId,
            @RequestBody LockerCustomerCreateRequest lockerCustomerCreateRequest
    ) {
this.lockerCustomerService.createLockerCustomer(userId, lockerCustomerCreateRequest);
    }
}
