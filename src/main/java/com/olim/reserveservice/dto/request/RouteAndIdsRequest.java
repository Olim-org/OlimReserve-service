package com.olim.reserveservice.dto.request;

import java.util.List;

public record RouteAndIdsRequest(
        List<RouteAndIdRequest> routeAndIdRequests
) {
}
