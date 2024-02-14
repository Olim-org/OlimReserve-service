package com.olim.reserveservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    private TicketType ticketType;
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer validDays;
    private Integer validCounts;
    private TicketCustomerType type;

}
