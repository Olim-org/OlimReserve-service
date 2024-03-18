package com.olim.reserveservice.dto.response;

import java.util.List;

public record LockerCustomerGetListResponse(
        Long total,
        List<LockerCustomerGetResponse> hits
) {
}
