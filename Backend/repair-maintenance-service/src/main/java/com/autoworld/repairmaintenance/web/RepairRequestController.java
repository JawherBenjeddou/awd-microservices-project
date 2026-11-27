package com.autoworld.repairmaintenance.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.autoworld.repairmaintenance.domain.GarageService;
import com.autoworld.repairmaintenance.domain.RepairRequest;
import com.autoworld.repairmaintenance.domain.RepairRequestStatus;
import com.autoworld.repairmaintenance.domain.UrgencyLevel;
import com.autoworld.repairmaintenance.domain.VehicleInfo;
import com.autoworld.repairmaintenance.repository.GarageServiceRepository;
import com.autoworld.repairmaintenance.repository.RepairRequestRepository;

@RestController
@RequestMapping("/api/repair-requests")
public class RepairRequestController {

	private final RepairRequestRepository repository;

	private final GarageServiceRepository garageServiceRepository;

	public RepairRequestController(RepairRequestRepository repository,
			GarageServiceRepository garageServiceRepository) {
		this.repository = repository;
		this.garageServiceRepository = garageServiceRepository;
	}

	@GetMapping
	public List<RepairRequestResponse> list() {
		return repository.findAll().stream()
				.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
				.map(RepairRequestResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public RepairRequestResponse get(@PathVariable UUID id) {
		return RepairRequestResponse.from(find(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RepairRequestResponse create(@RequestBody RepairRequestRequest request) {
		RepairRequest entity = new RepairRequest();
		apply(entity, request, true);
		return RepairRequestResponse.from(repository.save(entity));
	}

	@PutMapping("/{id}")
	public RepairRequestResponse update(@PathVariable UUID id, @RequestBody RepairRequestRequest request) {
		RepairRequest entity = find(id);
		apply(entity, request, false);
		return RepairRequestResponse.from(repository.save(entity));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Demande de reparation introuvable: " + id);
		}
		repository.deleteById(id);
	}

	private RepairRequest find(UUID id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Demande de reparation introuvable: " + id));
	}

	private void apply(RepairRequest entity, RepairRequestRequest request, boolean creating) {
		if (request.problemDescription() == null || request.problemDescription().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La description du probleme est obligatoire");
		}
		entity.setCustomerId(request.customerId());
		entity.setGarageId(request.garageId());
		entity.setProblemDescription(request.problemDescription().trim());
		entity.setUrgency(request.urgency() == null ? UrgencyLevel.MEDIUM : request.urgency());
		if (request.status() != null) {
			entity.setStatus(request.status());
		}
		entity.setQuotedPrice(request.quotedPrice());
		entity.setCurrency(request.currency());
		entity.setGarageComment(request.garageComment());
		entity.setGarageService(resolveGarageService(request.garageServiceId()));
		if (request.vehicle() != null) {
			entity.setVehicle(toVehicleInfo(request.vehicle()));
		}
		else if (creating) {
			entity.setVehicle(new VehicleInfo());
		}
	}

	private GarageService resolveGarageService(UUID garageServiceId) {
		if (garageServiceId == null) {
			return null;
		}
		return garageServiceRepository.findById(garageServiceId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Prestation inconnue: " + garageServiceId));
	}

	private VehicleInfo toVehicleInfo(VehicleInfoRequest request) {
		VehicleInfo vehicle = new VehicleInfo();
		vehicle.setVehicleBrand(request.vehicleBrand());
		vehicle.setVehicleModel(request.vehicleModel());
		vehicle.setVehicleYear(request.vehicleYear());
		vehicle.setLicensePlate(request.licensePlate());
		vehicle.setMileageKm(request.mileageKm());
		return vehicle;
	}

	public record VehicleInfoRequest(String vehicleBrand, String vehicleModel, Integer vehicleYear,
			String licensePlate, Integer mileageKm) {
	}

	public record RepairRequestRequest(UUID customerId, UUID garageId, String problemDescription, UrgencyLevel urgency,
			RepairRequestStatus status, BigDecimal quotedPrice, String currency, String garageComment,
			UUID garageServiceId, VehicleInfoRequest vehicle) {
	}

	public record RepairRequestResponse(UUID id, UUID customerId, UUID garageId, String problemDescription,
			UrgencyLevel urgency, RepairRequestStatus status, BigDecimal quotedPrice, String currency,
			String garageComment, UUID garageServiceId, String garageServiceName, VehicleInfoRequest vehicle,
			Instant createdAt, Instant updatedAt) {

		static RepairRequestResponse from(RepairRequest entity) {
			VehicleInfo v = entity.getVehicle();
			VehicleInfoRequest vehicle = v == null ? null
					: new VehicleInfoRequest(v.getVehicleBrand(), v.getVehicleModel(), v.getVehicleYear(),
							v.getLicensePlate(), v.getMileageKm());
			return new RepairRequestResponse(entity.getId(), entity.getCustomerId(), entity.getGarageId(),
					entity.getProblemDescription(), entity.getUrgency(), entity.getStatus(),
					entity.getQuotedPrice(), entity.getCurrency(), entity.getGarageComment(),
					entity.getGarageService() == null ? null : entity.getGarageService().getId(),
					entity.getGarageService() == null ? null : entity.getGarageService().getName(),
					vehicle, entity.getCreatedAt(), entity.getUpdatedAt());
		}

	}

}
