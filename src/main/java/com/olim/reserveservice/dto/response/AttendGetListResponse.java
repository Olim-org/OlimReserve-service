package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Attend;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record AttendGetListResponse(
    Long total,
    List<AttendGetResponse> hits
) {
    public static AttendGetListResponse makeDto(Page<Attend> attendPage) {
        List<AttendGetResponse> attendGetResponseList = new ArrayList<>();
        for (Attend attend : attendPage.getContent()) {
            attendGetResponseList.add(AttendGetResponse.makeDto(attend));
        }
        return new AttendGetListResponse(attendPage.getTotalElements(), attendGetResponseList);
    }
}
