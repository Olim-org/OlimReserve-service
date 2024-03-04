package com.olim.reserveservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.reserveservice.enumeration.LockerCustomerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LockerCustomer {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "LOCKER_CUSTOMER_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private UUID centerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
    private Long customerId;
    private String customerName;
    @Enumerated(value = EnumType.STRING)
    private LockerCustomerStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String hexColor;
    @Column(columnDefinition = "longtext")
    private String customJson;
    @Builder
    public LockerCustomer(
            UUID centerId,
            Locker locker,
            Long customerId,
            String customerName,
            LockerCustomerStatus status,
            LocalDate startDate,
            LocalDate endDate,
            String hexColor,
            String customJson
    ) {
        this.centerId = centerId;
        this.locker = locker;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hexColor = hexColor;
        this.customJson = customJson;
    }
}
