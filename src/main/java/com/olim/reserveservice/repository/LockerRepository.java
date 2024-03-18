package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.Locker;
import com.olim.reserveservice.enumeration.LockerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LockerRepository extends JpaRepository<Locker, UUID> {
    Page<Locker> findAllByCenterIdAndSectionAndNameContaining(
            UUID centerId, String section, String keyword, Pageable pageable);
}
