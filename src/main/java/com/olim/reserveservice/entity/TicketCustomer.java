package com.olim.reserveservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.reserveservice.enumeration.PaymentMethod;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketCustomer extends BaseEntity{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "TICKET_CUSTOMER_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;
    @Enumerated(value = EnumType.STRING)
    private TicketType ticketType;
    private UUID centerId;
    private Long customerId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer validCounts;
    @Enumerated(value = EnumType.STRING)
    private TicketCustomerType type;
    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String price;
    private String paidPrice;
    @Column(columnDefinition = "longtext")
    private String description;
    @Column(columnDefinition = "longtext")
    private String customJson;

    @Builder
    public TicketCustomer(
            Ticket ticket,
            TicketType ticketType,
            UUID centerId,
            Long customerId,
            String customerName,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            Integer validCounts,
            PaymentMethod paymentMethod,
            String price,
            String paidPrice,
            TicketCustomerType type,
            String description,
            String customJson
    ) {
        this.ticket = ticket;
        this.ticketType = ticketType;
        this.centerId = centerId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.validCounts = validCounts;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.paidPrice = paidPrice;
        this.type = type;
        this.description = description;
        this.customJson = customJson;
    }
}
