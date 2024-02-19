package com.olim.reserveservice.dto.request;

import com.olim.reserveservice.enumeration.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

public record TicketModifyRequest(
        @Schema(description = "티켓 제목", example = "티켓 제목")
        @NotBlank
        String title,
        @Schema(description = "티켓 설명", example = "티켓 설명")
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
        @Schema(description = "티켓 적용 일수", example = "30")
        Integer applyDays,
        @Schema(description = "티켓 유효 횟수", example = "10")
        Integer validCount,
        @Schema(description = "티켓 시작 시간", example = "05:00")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식이 올바르지 않습니다.")
        String startTime,
        @Schema(description = "티켓 종료 시간", example = "23:00")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식이 올바르지 않습니다.")
        String endTime,
        @Schema(description = "티켓 상태", example = "SELL")
        @NotNull
        TicketStatus ticketStatus,
        String customJson
) {
}
