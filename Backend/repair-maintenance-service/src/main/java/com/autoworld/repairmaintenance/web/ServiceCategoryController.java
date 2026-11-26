package com.autoworld.repairmaintenance.web;

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

import com.autoworld.repairmaintenance.domain.ServiceCategory;
import com.autoworld.repairmaintenance.repository.ServiceCategoryRepository;

@RestController
@RequestMapping("/api/service-categories")
public class ServiceCategoryController {

	private final ServiceCategoryRepository repository;

	public ServiceCategoryController(ServiceCategoryRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<ServiceCategoryResponse> list() {
		return repository.findAll().stream()
				.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
				.map(ServiceCategoryResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public ServiceCategoryResponse get(@PathVariable UUID id) {
		return ServiceCategoryResponse.from(find(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ServiceCategoryResponse create(@RequestBody ServiceCategoryRequest request) {
		ServiceCategory category = new ServiceCategory(requireName(request), request.description());
		return ServiceCategoryResponse.from(repository.save(category));
	}

	@PutMapping("/{id}")
	public ServiceCategoryResponse update(@PathVariable UUID id, @RequestBody ServiceCategoryRequest request) {
		ServiceCategory category = find(id);
		category.setName(requireName(request));
		category.setDescription(request.description());
		return ServiceCategoryResponse.from(repository.save(category));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categorie introuvable: " + id);
		}
		repository.deleteById(id);
	}

	private ServiceCategory find(UUID id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categorie introuvable: " + id));
	}

	private String requireName(ServiceCategoryRequest request) {
		if (request.name() == null || request.name().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nom de la categorie est obligatoire");
		}
		return request.name().trim();
	}

	public record ServiceCategoryRequest(String name, String description) {
	}

	public record ServiceCategoryResponse(UUID id, String name, String description) {

		static ServiceCategoryResponse from(ServiceCategory category) {
			return new ServiceCategoryResponse(category.getId(), category.getName(), category.getDescription());
		}

	}

}
