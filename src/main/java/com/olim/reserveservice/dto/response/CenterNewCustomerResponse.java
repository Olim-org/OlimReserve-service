package com.olim.reserveservice.dto.response;

public record CenterNewCustomerResponse(
        String valid,
        String invalid
) {
    public static CenterNewCustomerResponse makeDto(Long valid, Long invalid) {
        return new CenterNewCustomerResponse(valid.toString(), invalid.toString());
    }
}
