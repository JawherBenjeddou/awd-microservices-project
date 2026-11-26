package com.autoworld.repairmaintenance.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

import com.autoworld.repairmaintenance.domain.GarageService;
import com.autoworld.repairmaintenance.repository.GarageServiceRepository;

@RestController
@RequestMapping("/api/garage-services")
public class GarageServiceController {

	private final GarageServiceRepository repository;

	public GarageServiceController(GarageServiceRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<GarageServiceResponse> list() {
		return repository.findAll().stream()
				.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
				.map(GarageServiceResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public GarageServiceResponse get(@PathVariable UUID id) {
		return repository.findById(id)
				.map(GarageServiceResponse::from)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation introuvable: " + id));
	}

	public record GarageServiceResponse(UUID id, UUID garageId, String name, String description,
			BigDecimal basePrice, String currency, Integer estimatedDurationMinutes, boolean active,
			UUID categoryId, String categoryName) {

		static GarageServiceResponse from(GarageService service) {
			return new GarageServiceResponse(service.getId(), service.getGarageId(), service.getName(),
					service.getDescription(), service.getBasePrice(), service.getCurrency(),
					service.getEstimatedDurationMinutes(), service.isActive(),
					service.getCategory() == null ? null : service.getCategory().getId(),
					service.getCategory() == null ? null : service.getCategory().getName());
		}

	}

}
