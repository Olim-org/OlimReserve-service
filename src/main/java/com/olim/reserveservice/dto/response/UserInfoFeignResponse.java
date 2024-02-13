package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.enumeration.Gender;

import java.time.LocalDate;
import java.util.UUID;


public record UserInfoFeignResponse(
        UUID id,
        String name,
        String phoneNumber,
        String address,
        LocalDate birthDate,
        Gender gender,
        String email,
        String nickname,
        String role
) {
}
