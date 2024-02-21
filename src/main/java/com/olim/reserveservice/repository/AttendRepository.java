package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.Attend;
import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.enumeration.BlackConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendRepository extends JpaRepository<Attend, UUID> {
    Page<Attend> findAllByCenterIdAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(UUID centerId, LocalDateTime time1, LocalDateTime time2, String customerName, Pageable pageable);
    Page<Attend> findAllByCenterIdAndBlackConsumerAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(UUID centerId, BlackConsumer blackConsumer, LocalDateTime time1, LocalDateTime time2, String customerName, Pageable pageable);
    Page<Attend> findAllByCenterIdAndTicketAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(UUID centerId, Ticket ticket, LocalDateTime time1, LocalDateTime time2, String customerName, Pageable pageable);
    Page<Attend> findAllByCenterIdAndTicketAndBlackConsumerAndAttendTimeAfterAndAttendTimeBeforeAndCustomerNameContaining(UUID centerId, Ticket ticket, BlackConsumer blackConsumer, LocalDateTime time1, LocalDateTime time2, String customerName, Pageable pageable);
    Integer countAllByCenterIdAndAttendTimeAfterAndAttendTimeBeforeAndTimeGraphAfterAndTimeGraphBefore(UUID centerId, LocalDateTime time1, LocalDateTime time2, LocalTime time3, LocalTime time4);
    Optional<Attend> findTop1ByCenterIdAndCustomerIdAndAttendTimeBeforeAndAttendTimeAfterOrderByAttendTimeDesc(UUID centerId, Long customerId, LocalDateTime time1, LocalDateTime time2);
}
