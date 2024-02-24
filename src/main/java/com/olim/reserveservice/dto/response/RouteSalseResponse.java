package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;

import java.util.List;
import java.util.Map;

public record RouteSalseResponse(
        String routeName,
        List<RouteTicketSalesResponse> value
) {
    public static RouteSalseResponse makeDto(String routeName, Map<Ticket, String> totalSales) {
        List<RouteTicketSalesResponse> routeTicketSalesResponses = RouteTicketSalesResponse.makeDto(totalSales);
        return new RouteSalseResponse(routeName, routeTicketSalesResponses);
    }
}
