package com.olim.reserveservice.dto.response;

import com.olim.reserveservice.entity.TicketCustomer;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record TicketCustomerGetListResponse(
        Long total,
        List<TicketCustomerGetResponse> hits
) {
    public static TicketCustomerGetListResponse makeDto(Page<TicketCustomer> ticketCustomerPage) {
        List<TicketCustomerGetResponse> ticketCustomerGetResponseList = new ArrayList<>();
        for (TicketCustomer ticketCustomer : ticketCustomerPage.getContent()) {
            ticketCustomerGetResponseList.add(TicketCustomerGetResponse.makeDto(ticketCustomer));
        }
        return new TicketCustomerGetListResponse(ticketCustomerPage.getTotalElements(), ticketCustomerGetResponseList);
    }
}
