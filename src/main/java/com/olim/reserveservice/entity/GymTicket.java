package com.olim.reserveservice.entity;

import com.olim.reserveservice.enumeration.TicketStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymTicket extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "GYM_TICKET_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private UUID centerId;
    private String title;
    private String description;
    private String price;
    private String sale;
    private Integer applyDays;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;
    @OneToMany(mappedBy = "gymTicket", cascade = CascadeType.ALL)
    private List<GymTicketCustomer> gymTicketCustomers;
    @Builder
    public GymTicket(
            UUID centerId,
            String title,
            String description,
            String price,
            String sale,
            Integer applyDays,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.centerId = centerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sale = sale;
        this.status = TicketStatus.WAIT;
        this.applyDays = applyDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gymTicketCustomers = new ArrayList<>();
    }
    public void updateTicket(
            String title,
            String description,
            String price,
            String sale,
            Integer applyDays,
            LocalDateTime startTime,
            LocalDateTime endTime,
            TicketStatus ticketStatus
    ) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.sale = sale;
        this.status = ticketStatus;
        this.applyDays = applyDays;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
