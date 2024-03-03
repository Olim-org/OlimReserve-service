package com.olim.reserveservice.repository;

import com.olim.reserveservice.entity.Locker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LockerRepository extends JpaRepository<Locker, UUID> {
}
