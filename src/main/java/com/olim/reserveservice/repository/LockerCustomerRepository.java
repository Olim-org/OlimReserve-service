package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.LockerCustomer;
import com.olim.reserveservice.enumeration.LockerCustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;
@Repository
public interface LockerCustomerRepository extends JpaRepository<LockerCustomer, UUID> {
    @Query("select l from LockerCustomer l where l.locker = ?1 and l.status = ?2 and (l.endDate > ?3 or l.endDate > ?4)")
    Boolean existsByLockerAndStatusAndStartDateAndEndDate(UUID lockerId, LockerCustomerStatus status, LocalDate startDate, LocalDate endDate);
}
