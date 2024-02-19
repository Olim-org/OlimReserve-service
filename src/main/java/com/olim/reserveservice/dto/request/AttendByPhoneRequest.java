package com.olim.reserveservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record AttendByPhoneRequest(
        @Schema(description = "센터 UUID", example = "asdasf-qweqw-czxc")
        @NotBlank
        UUID centerId,
        @Schema(description = "전화번호", example = "01012345678")
        @NotBlank
        @Pattern(regexp = "^\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
        String phoneNumber
) {
}
