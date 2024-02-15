package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.enumeration.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public record TicketGetResponse(
        @Schema(description = "티켓 UUID", example = "asdfw-rwqvc-vx")
        UUID id,
        @Schema(description = "센터 UUID", example = "asdfw-rwqvc-vx")
        UUID centerId,
        @Schema(description = "티켓 제목", example = "테스트 티켓")
        String title,
        @Schema(description = "티켓 설명", example = "테스트 티켓 설명")
        String description,
        @Schema(description = "티켓 가격", example = "10000")
        String price,
        @Schema(description = "티켓 할인 가격", example = "5000")
        String sale,
        @Schema(description = "티켓 타입", example = "GYM")
        TicketType type,
        @Schema(description = "티켓 적용 일수", example = "30")
        Integer applyDays,
        @Schema(description = "티켓 유효 횟수", example = "10")
        Integer validCount,
        @Schema(description = "티켓 시작 시간", example = "05:00")
        String startTime,
        @Schema(description = "티켓 종료 시간", example = "23:00")
        String endTime,
        @Schema(description = "티켓 상태", example = "SELL")
        TicketStatus status,
        String customJson
) {
    public static TicketGetResponse makeDto(Ticket ticket) {
        TicketGetResponse response = new TicketGetResponse(
                ticket.getId(),
                ticket.getCenterId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPrice(),
                ticket.getSale(),
                ticket.getType(),
                ticket.getApplyDays(),
                ticket.getValidCounts(),
                ticket.getStartTime().format(DateTimeFormatter.ISO_TIME),
                ticket.getEndTime().format(DateTimeFormatter.ISO_TIME),
                ticket.getStatus(),
                ticket.getCustomJson()
        );
        return response;
    }
}
