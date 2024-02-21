package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Attend;

import java.util.ArrayList;
import java.util.List;

public record AttendTimeGraphResponse(
        int total,
        List<AttendTimeResponse> hits
) {
}
