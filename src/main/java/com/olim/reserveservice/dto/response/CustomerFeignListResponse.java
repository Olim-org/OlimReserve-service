package com.olim.reserveservice.dto.response;


import java.util.ArrayList;
import java.util.List;

public record CustomerFeignListResponse(
        Boolean success,
        Long total,
        List<CustomerFeignResponse> hits
) {
}
