package com.olim.reserveservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.reserveservice.enumeration.BlackConsumer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attend extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ATTEND_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private Long customerId;
    private String customerName;
    private UUID centerId;
    private LocalDateTime attendTime;
    private LocalTime timeGraph;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_CUSTOMER_ID")
    @ToString.Exclude
    @JsonIgnore
    private TicketCustomer ticketCustomer; // 사용된 티켓-고객
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID")
    @ToString.Exclude
    @JsonIgnore
    private Ticket ticket; // 사용된 티켓
    @Enumerated(value = EnumType.STRING)
    private BlackConsumer blackConsumer;
    @Builder
    public Attend(
            Long customerId,
            String customerName,
            UUID centerId,
            TicketCustomer ticketCustomer,
            Ticket ticket,
            LocalDateTime attendTime,
            BlackConsumer blackConsumer,
            LocalTime timeGraph
    ) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.centerId = centerId;
        this.ticketCustomer = ticketCustomer;
        this.ticket = ticket;
        this.attendTime = attendTime;
        this.blackConsumer = blackConsumer;
        this.timeGraph = timeGraph;
    }
    public void updateAttendTime(LocalDateTime attendTime) {
        this.attendTime = attendTime;
    }
}
