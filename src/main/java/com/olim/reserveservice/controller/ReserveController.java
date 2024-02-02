package com.olim.reserveservice.controller;

import com.olim.reserveservice.service.ReserveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/reserve-service/reserve")
@Tag(name="Reserve", description = "Reserve API 구성")
@Validated
public class ReserveController {
    private final ReserveService reserveService;

}
