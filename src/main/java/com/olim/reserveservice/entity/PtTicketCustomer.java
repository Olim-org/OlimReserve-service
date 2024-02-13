package com.olim.reserveservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Internal;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PtTicketCustomer {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "PT_TICKET_CUSTOMER_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "PT_TICKET_ID")
    private PtTicket ptTicket;
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer validCount;
}
