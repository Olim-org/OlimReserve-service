package com.olim.reserveservice.entity;

import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.enumeration.TicketType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseEntity {
    @Data
    static class customJson {
        private String key;
        private String value;
    }
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "TICKET_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private UUID centerId;
    @Column(length = 1000)
    private String title;
    @Column(columnDefinition = "longtext")
    private String description;
    private String price;
    private String sale;
    @Enumerated(value = EnumType.STRING)
    private TicketType type;
    private Integer applyDays;  // 사용 가능 일 수
    private Integer validCounts; // 사용 가능 횟 수
    private LocalTime startTime; // 사용 가능 시간
    private LocalTime endTime;  // 사용 가능 시간
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;
    @Column(columnDefinition = "longtext")
    private String customJson;
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketCustomer> ticketCustomers;
    @Builder
    public Ticket(
            UUID centerId,
            String title,
            String description,
            String price,
            String sale,
            Integer applyDays,
            Integer validCounts,
            LocalTime startTime,
            LocalTime endTime,
            TicketType type,
            String customJson
    ) {
        this.centerId = centerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sale = sale;
        this.type = type;
        this.status = TicketStatus.WAIT;
        this.applyDays = applyDays;
        this.validCounts = validCounts;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customJson = customJson;
        this.ticketCustomers = new ArrayList<>();
    }
    public void updateTicket(
            String title,
            String description,
            String price,
            String sale,
            Integer applyDays,
            Integer validCounts,
            LocalTime startTime,
            LocalTime endTime,
            String customJson,
            TicketStatus ticketStatus
    ) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.sale = sale;
        this.status = ticketStatus;
        this.applyDays = applyDays;
        this.validCounts = validCounts;
        this.startTime = startTime;
        this.customJson = customJson;
        this.endTime = endTime;
    }
    public void updateStatus(TicketStatus status) {
        this.status = status;
    }

}
