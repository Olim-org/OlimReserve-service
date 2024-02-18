package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.TicketCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TicketCustomerRepository extends JpaRepository<TicketCustomer, UUID> {
    Page<TicketCustomer> findAllByCenterIdAndCustomerName(UUID centerId, String customerName, Pageable pageable);
    Page<TicketCustomer> findAllByCenterIdAndCustomerId(UUID centerId, Long customerId, Pageable pageable);
}
