package com.autoworld.repairmaintenance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoworld.repairmaintenance.domain.MaintenanceAppointment;

public interface MaintenanceAppointmentRepository extends JpaRepository<MaintenanceAppointment, UUID> {
}
