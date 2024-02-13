package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.PtTicketCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PtTicketCustomerRepository extends JpaRepository<PtTicketCustomer, UUID> {
}
