package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.GymTicketCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface GymTicketCustomerRepository extends JpaRepository<GymTicketCustomer, UUID> {
}
