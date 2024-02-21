package com.olim.reserveservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AttendCheckRequest(
        @Schema(description = "고객 ID", example = "1")
        @NotBlank
        Long customerId,
        @Schema(description = "센터 UUID", example = "asdasf-qweqw-czxc")
        @NotBlank
        UUID centerId
) {
}
