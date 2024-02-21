package com.olim.reserveservice.dto.response;

public record AttendTimeResponse(
        String attendTime,
        Integer attendCount
) {
    public static AttendTimeResponse makeDto(String attendTime, Integer attendCount) {
        AttendTimeResponse response = new AttendTimeResponse(attendTime, attendCount);
        return response;
    }
}
