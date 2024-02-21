package com.olim.reserveservice.dto.request;

import java.util.UUID;

public record CustomerAttendFeignRequest(
        Long customerId,
        UUID attendId,
        Boolean isBlackConsumer
) {
    public static CustomerAttendFeignRequest makeDto(
            Long customerId,
            UUID attendId,
            Boolean isBlackConsumer
    ) {
        CustomerAttendFeignRequest request = new CustomerAttendFeignRequest(customerId, attendId, isBlackConsumer);
        return request;
    }
}
