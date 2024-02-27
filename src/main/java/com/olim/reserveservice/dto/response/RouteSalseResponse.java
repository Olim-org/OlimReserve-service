package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;

import java.util.List;
import java.util.Map;

public record RouteSalseResponse(
        String routeName,
        String value
) {
    public static RouteSalseResponse makeDto(String routeName, String value) {
        return new RouteSalseResponse(routeName, value);
    }
}
