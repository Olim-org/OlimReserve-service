package com.olim.reserveservice.service;

import com.olim.reserveservice.entity.TicketCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TicketCustomerService extends JpaRepository<TicketCustomer, UUID> {
}
