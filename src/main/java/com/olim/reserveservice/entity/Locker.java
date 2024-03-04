package com.olim.reserveservice.entity;

import com.olim.reserveservice.enumeration.LockerStatus;
import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.enumeration.TicketType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Locker {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "LOCKER_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private UUID centerId;
    @OneToMany(mappedBy = "locker", cascade = CascadeType.PERSIST)
    private List<LockerCustomer> lockerCustomers;
    private String name;
    private String section;
    @Enumerated(value = EnumType.STRING)
    private LockerStatus status;
    private String description;
    private String hexColor;
    @Column(columnDefinition = "longtext")
    private String customJson;
    @Builder
    public Locker(
            UUID centerId,
            String name,
            String section,
            String description,
            LockerStatus status,
            String hexColor,
            String customJson
    ) {
        this.centerId = centerId;
        this.name = name;
        this.section = section;
        this.description = description;
        this.status = status;
        this.hexColor = hexColor;
        this.customJson = customJson;
        this.lockerCustomers = new ArrayList<>();
    }
}
