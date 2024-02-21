package com.olim.reserveservice.client;

import com.olim.reserveservice.dto.request.CustomerAttendFeignRequest;
import com.olim.reserveservice.dto.response.CenterFeignResponse;
import com.olim.reserveservice.dto.response.CustomerFeignListResponse;
import com.olim.reserveservice.dto.response.CustomerFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "customer-service", path = "/customer-service")
public interface CustomerClient {
    String AUTHORIZATION = "Authorization";
    @GetMapping("/center/info")
    CenterFeignResponse getCenterInfo(@RequestHeader("id") String userId, @RequestParam(value = "centerId") String centerId);
    @GetMapping("/customer/info")
    CustomerFeignResponse getCustomerInfo(@RequestHeader("id") String userId, @RequestParam(value = "customerId") Long customerId);
    @GetMapping("/customer/pinfo")
    CustomerFeignListResponse getCustomerInfo(@RequestHeader("id") String userId, @RequestParam(value = "phoneNumber") String phoneNumber, @RequestParam(value = "centerId") String centerId);
    @PostMapping("/customer/attend")
    String attend(
            @RequestHeader("id") String userId,
            @RequestBody CustomerAttendFeignRequest customerAttendFeignRequest
    );
}
