package com.olim.reserveservice.dto.request;

import java.util.UUID;

public record LockerCustomerCreateRequest(
        UUID centerId,
        Long customerId,
        UUID lockerId,
        String startDate,
        String endDate,
        String hexColor,
        String customJson
) {
}
