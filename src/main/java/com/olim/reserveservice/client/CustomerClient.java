package com.olim.reserveservice.client;

import com.olim.reserveservice.dto.response.CenterFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "customer-service", path = "/customer-service")
public interface CustomerClient {
    String AUTHORIZATION = "Authorization";
    @GetMapping("/center/info")
    CenterFeignResponse getCenterInfo(@RequestHeader(AUTHORIZATION) String token, @RequestParam(value = "centerId") String centerId);
}
