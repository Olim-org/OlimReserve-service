package com.olim.reserveservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "auth-service", path = "/auth-service/user")
public interface UserClient {
    String AUTHORIZATION = "Authorization";
//    @GetMapping("/info")

}
