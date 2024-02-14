package com.olim.reserveservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "헬스장 이용권 구입", description = "헬스장 이용권 구입 관련 API 구성")
@RequestMapping("/reserve-service/ticket/customer")
@Validated
public class TicketCustomerController {
}
