package com.autoworld.repairmaintenance.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "repair_request")
public class RepairRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "customer_id")
	private UUID customerId;

	@Column(name = "garage_id")
	private UUID garageId;

	@Column(name = "problem_description", nullable = false, length = 1000)
	private String problemDescription;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UrgencyLevel urgency = UrgencyLevel.MEDIUM;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RepairRequestStatus status = RepairRequestStatus.SUBMITTED;

	@Column(name = "quoted_price", precision = 10, scale = 2)
	private BigDecimal quotedPrice;

	@Column(length = 3)
	private String currency;

	@Column(name = "garage_comment", length = 500)
	private String garageComment;

	@ManyToOne
	@JoinColumn(name = "garage_service_id")
	private GarageService garageService;

	@Embedded
	private VehicleInfo vehicle = new VehicleInfo();

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@PrePersist
	void onCreate() {
		Instant now = Instant.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		this.updatedAt = Instant.now();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public UUID getGarageId() {
		return garageId;
	}

	public void setGarageId(UUID garageId) {
		this.garageId = garageId;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public UrgencyLevel getUrgency() {
		return urgency;
	}

	public void setUrgency(UrgencyLevel urgency) {
		this.urgency = urgency;
	}

	public RepairRequestStatus getStatus() {
		return status;
	}

	public void setStatus(RepairRequestStatus status) {
		this.status = status;
	}

	public BigDecimal getQuotedPrice() {
		return quotedPrice;
	}

	public void setQuotedPrice(BigDecimal quotedPrice) {
		this.quotedPrice = quotedPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGarageComment() {
		return garageComment;
	}

	public void setGarageComment(String garageComment) {
		this.garageComment = garageComment;
	}

	public GarageService getGarageService() {
		return garageService;
	}

	public void setGarageService(GarageService garageService) {
		this.garageService = garageService;
	}

	public VehicleInfo getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleInfo vehicle) {
		this.vehicle = vehicle;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
