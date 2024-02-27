package com.olim.reserveservice.dto.response;

public record CenterNewCustomerResponse(
        String join,
        String valid,
        String invalid
) {
    public static CenterNewCustomerResponse makeDto(Long join, Long valid, Long invalid) {
        return new CenterNewCustomerResponse(join.toString(), valid.toString(), invalid.toString());
    }
}
