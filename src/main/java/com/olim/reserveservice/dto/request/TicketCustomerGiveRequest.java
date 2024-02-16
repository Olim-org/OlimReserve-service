package com.olim.reserveservice.dto.request;

import com.olim.reserveservice.enumeration.PaymentMethod;
import com.olim.reserveservice.enumeration.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record TicketCustomerGiveRequest(
        @Schema(description = "티켓 UUID", example = "asdfw-rwqvc-vx")
        @NotBlank
        UUID ticketId,
        @Schema(description = "티켓 타입", example = "GYM")
        @NotBlank
        TicketType ticketType,
        @Schema(description = "고객 UID", example = "5")
        @NotBlank
        Long customerId,
        @Schema(description = "티켓 시작 날짜", example = "2021-10-01")
        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식이 올바르지 않습니다.")
        String startDate,
        @Schema(description = "티켓 종료 날짜", example = "2021-10-31")
        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식이 올바르지 않습니다.")
        String endDate,
        @Schema(description = "티켓 시작 시간", example = "09:00")
        @NotBlank
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식이 올바르지 않습니다.")
        String startTime,
        @Schema(description = "티켓 종료 시간", example = "18:00")
        @NotBlank
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식이 올바르지 않습니다.")
        String endTime,
        @Schema(description = "티켓 유효 횟수", example = "10")
        Integer validCounts,
        @Schema(description = "결제 방식", example = "CASH")
        PaymentMethod paymentMethod,
        @Schema(description = "티켓 가격", example = "10000")
        @Pattern(regexp = "^\\d+$", message = "숫자만 입력해주세요.")
        @NotBlank
        String price,
        @Schema(description = "티켓 할인 가격", example = "5000")
        @Nullable
        @Pattern(regexp = "^\\d+$", message = "숫자만 입력해주세요.")
        String paidPrice,
        @Schema(description = "티켓 설명", example = "테스트 티켓 설명")
        String description,
        @Schema(description = "티켓 커스텀 JSON", example = "{'key': 'value'}")
        String customJson
) {
}
