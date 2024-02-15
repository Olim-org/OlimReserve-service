package com.olim.reserveservice.dto.request;

import com.olim.reserveservice.enumeration.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Map;
import java.util.UUID;

public record TicketCreateRequest(
        @Schema(description = "센터 UUID", example = "asdfw-rwqvc-vx")
        @NotBlank
        UUID centerId,
        @Schema(description = "티켓 제목", example = "테스트 티켓")
        @NotBlank
        String title,
        @Schema(description = "티켓 설명", example = "테스트 티켓 설명")
        @NotBlank
        String description,
        @Schema(description = "티켓 가격", example = "10000")
        @NotBlank
        @Pattern(regexp = "^\\d+$", message = "숫자만 입력해주세요.")
        String price,
        @Schema(description = "티켓 할인 가격", example = "5000")
        @Pattern(regexp = "^\\d+$", message = "숫자만 입력해주세요.")
        @Nullable
        String sale,
        @Schema(description = "티켓 타입", example = "GYM")
        @NotBlank
        TicketType type,
        @Schema(description = "티켓 적용 일수", example = "30")
        Integer applyDays,
        @Schema(description = "티켓 유효 횟수", example = "10")
        Integer validCount,
        @Schema(description = "티켓 시작 시간", example = "09:00")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식이 올바르지 않습니다.")
        String startTime,
        @Schema(description = "티켓 종료 시간", example = "18:00")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식이 올바르지 않습니다.")
        String endTime,
        String customJson
) {
}
