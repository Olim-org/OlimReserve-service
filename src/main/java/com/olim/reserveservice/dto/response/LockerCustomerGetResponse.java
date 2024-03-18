package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.LockerCustomer;
import com.olim.reserveservice.enumeration.LockerCustomerStatus;

import java.util.UUID;

public record LockerCustomerGetResponse(
        UUID id,
        UUID centerId,
        UUID lockerId,
        Long customerId,
        String customerName,
        LockerCustomerStatus status,
        String startDate,
        String endDate,
        String hexColor,
        String customJson
) {
}
