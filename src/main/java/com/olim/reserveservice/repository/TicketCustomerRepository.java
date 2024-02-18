package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.TicketCustomer;
import com.olim.reserveservice.enumeration.TicketCustomerType;
import com.olim.reserveservice.enumeration.TicketType;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface TicketCustomerRepository extends JpaRepository<TicketCustomer, UUID> {
    Page<TicketCustomer> findAllByCenterIdAndTicketTypeAndCustomerNameContaining(UUID centerId, TicketType ticketType, String customerName, Pageable pageable);
    Page<TicketCustomer> findAllByCenterIdAndCustomerIdAndTicketType(UUID centerId, Long customerId, TicketType ticketType, Pageable pageable);
    Optional<TicketCustomer> findTop1ByCenterIdAndCustomerIdAndTypeAndTicketTypeAndStartDateBeforeAndEndDateAfterAndStartTimeBeforeAndEndTimeAfterAndValidCountsGreaterThanOrderByStartDateDesc(
            UUID centerId,
            Long customerId,
            TicketCustomerType type,
            TicketType ticketType,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            Integer validCounts);
}
