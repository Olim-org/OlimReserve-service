package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.TicketCustomer;
import com.olim.reserveservice.enumeration.PaymentMethod;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record TicketCustomerGetResponse(
        @Schema(description = "티켓-고객 고유번호", example = "asdasf-qweqw-czxc")
        UUID id,
        @Schema(description = "티켓 고유번호", example = "asdasf-qweqw-czxc")
        UUID ticketId,
        @Schema(description = "티켓 유형", example = "GYM")
        TicketType ticketType,
        @Schema(description = "고객 고유번호", example = "5")
        Long customerId,
        @Schema(description = "티켓 시작 날짜", example = "2021-10-01")
        String startDate,
        @Schema(description = "티켓 종료 날짜", example = "2021-10-31")
        String endDate,
        @Schema(description = "티켓 시작 시간", example = "09:00")
        String startTime,
        @Schema(description = "티켓 종료 시간", example = "18:00")
        String endTime,
        @Schema(description = "티켓 유효 횟수", example = "10")
        Integer validCounts,
        @Schema(description = "결제 방식", example = "CASH")
        PaymentMethod paymentMethod,
        @Schema(description = "티켓 가격", example = "10000")
        String price,
        @Schema(description = "티켓 결제 가격", example = "5000")
        String paidPrice,
        @Schema(description = "티켓-고객 유형", example = "VALID")
        TicketCustomerType type,
        @Schema(description = "티켓 설명", example = "테스트 티켓 설명")
        String description,
        @Schema(description = "티켓 커스텀 JSON", example = "{'key': 'value'}")
        String customJson
) {
    public static TicketCustomerGetResponse makeDto(
            TicketCustomer ticketCustomer
    ) {
        TicketCustomerGetResponse response = new TicketCustomerGetResponse(
                ticketCustomer.getId(),
                ticketCustomer.getTicket().getId(),
                ticketCustomer.getTicketType(),
                ticketCustomer.getCustomerId(),
                ticketCustomer.getStartDate().toString(),
                ticketCustomer.getEndDate().toString(),
                ticketCustomer.getStartTime().toString(),
                ticketCustomer.getEndTime().toString(),
                ticketCustomer.getValidCounts(),
                ticketCustomer.getPaymentMethod(),
                ticketCustomer.getPrice(),
                ticketCustomer.getPaidPrice(),
                ticketCustomer.getType(),
                ticketCustomer.getDescription(),
                ticketCustomer.getCustomJson()
        );
        return response;
    }

}
