package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.Ticket;
import com.olim.reserveservice.enumeration.TicketStatus;
import com.olim.reserveservice.enumeration.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findAllByCenterIdAndTypeAndStatusIsNotAndTitleContaining(UUID centerId, TicketType type, TicketStatus status, String title, Pageable pageable);
    Page<Ticket> findAllByCenterIdAndTypeAndStatusAndTitleContaining(UUID centerId, TicketType type, TicketStatus status, String title, Pageable pageable);
    List<Ticket> findAllByCenterIdAndStatusNotIn(UUID centerId, List<TicketStatus> status);
}
