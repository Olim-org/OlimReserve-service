package com.olim.reserveservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PtTicket {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "PT_TICKET_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private UUID centerId;
    private String title;
    private String description;
    private String price;
    private String sale;
    private Integer validDays;
    private Integer validCount;
    @OneToMany(mappedBy = "ptTicket", cascade = CascadeType.ALL)
    private List<PtTicketCustomer> ptTicketCustomers;
}
