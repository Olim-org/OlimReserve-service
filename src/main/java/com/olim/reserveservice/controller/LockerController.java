package com.olim.reserveservice.controller;

import com.olim.reserveservice.dto.request.LockerCreateRequest;
import com.olim.reserveservice.service.LockerService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<String> createLocker(
            @RequestHeader(value = "id") String userId,
            @RequestBody LockerCreateRequest lockerCreateRequest
            ) {
        return new ResponseEntity<>(lockerService.createLocker(userId, lockerCreateRequest), HttpStatus.OK);
    }
}
