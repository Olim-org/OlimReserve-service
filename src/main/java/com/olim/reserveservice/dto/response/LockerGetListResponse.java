package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Locker;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record LockerGetListResponse(
        Long total,
        List<LockerGetResponse> hits
) {
    public static LockerGetListResponse makeDto(Page<Locker> lockers) {
        List<LockerGetResponse> lockerGetResponses = new ArrayList<>();
        for (Locker locker : lockers.getContent()) {
            LockerGetResponse lockerGetResponse = LockerGetResponse.makeDto(
                    locker.getId(),
                    locker.getCenterId(),
                    locker.getName(),
                    locker.getSection(),
                    locker.getDescription(),
                    locker.getHexColor(),
                    locker.getCustomJson()
            );
            lockerGetResponses.add(lockerGetResponse);
        }
        return new LockerGetListResponse(lockers.getTotalElements(), lockerGetResponses);
    }
}
