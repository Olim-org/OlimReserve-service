package com.olim.reserveservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Center extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "CENTER_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private List<CenterEndUser> users;
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private List<Reserving> reservings;
    private String title; // 센터 이름
    private String description; // 센터 짧은 소개
    private String imageUrl; // 센터 이미지

}


