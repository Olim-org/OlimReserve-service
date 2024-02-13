package com.olim.reserveservice.dto.response;

import java.util.UUID;

public record CenterFeignResponse(
        UUID centerId,
        UUID owner
) {

}


