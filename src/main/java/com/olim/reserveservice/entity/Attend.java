package com.olim.reserveservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attend extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "RESERVATION_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private Long customerId;
    private String customerName;
    private UUID centerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_CUSTOMER_ID")
    @ToString.Exclude
    @JsonIgnore
    private TicketCustomer ticketCustomer; // 사용된 티켓
    @Builder
    public Attend(
            Long customerId,
            String customerName,
            UUID centerId,
            TicketCustomer ticketCustomer
    ) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.centerId = centerId;
        this.ticketCustomer = ticketCustomer;
    }

}
