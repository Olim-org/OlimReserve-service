package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.GymTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface GymTicketRepository extends JpaRepository<GymTicket, UUID> {
}
