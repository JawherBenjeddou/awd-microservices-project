package com.autoworld.repairmaintenance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoworld.repairmaintenance.domain.RepairRequest;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, UUID> {
}
