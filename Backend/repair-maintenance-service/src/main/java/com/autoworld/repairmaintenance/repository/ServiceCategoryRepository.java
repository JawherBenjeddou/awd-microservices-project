package com.autoworld.repairmaintenance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoworld.repairmaintenance.domain.ServiceCategory;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, UUID> {
}
