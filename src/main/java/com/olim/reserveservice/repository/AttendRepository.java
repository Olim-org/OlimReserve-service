package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.Attend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface AttendRepository extends JpaRepository<Attend, UUID> {
    Page<Attend> findAllByCenterIdAndCAtAfterAndCAtBeforeAndCustomerNameContaining(UUID centerId, LocalDate cAtAfter, LocalDate cAtBefore, String customerName, Pageable pageable);
}
