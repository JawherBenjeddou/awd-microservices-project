package tn.esprit.autoworld.vehiclesales.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.service.AnnonceVenteService;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
@Tag(name = "Sale listings", description = "CRUD on vehicle sale listings (MS1)")
public class AnnonceVenteController {

	private final AnnonceVenteService annonceService;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${server.port}")
	private String port;

	/** Shows which instance answered when several run on different ports. */
	@Operation(summary = "Say which instance answered")
	@ApiResponse(responseCode = "200", description = "Message with the port of the instance")
	@GetMapping("/hello")
	public String hello() {
		return "Hello from " + applicationName + " on port " + port;
	}

	@Operation(summary = "Create a listing and its vehicle")
	@ApiResponse(responseCode = "201", description = "Listing created")
	@ApiResponse(responseCode = "400", description = "Invalid input")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AnnonceVente create(@Valid @RequestBody AnnonceVente annonce) {
		return annonceService.create(annonce);
	}

	@Operation(summary = "List all listings")
	@ApiResponse(responseCode = "200", description = "Listings returned")
	@GetMapping
	public List<AnnonceVente> findAll() {
		return annonceService.findAll();
	}

	@Operation(summary = "Get a listing by id")
	@ApiResponse(responseCode = "200", description = "Listing found")
	@ApiResponse(responseCode = "404", description = "Listing not found")
	@GetMapping("/{id}")
	public AnnonceVente findById(@PathVariable Long id) {
		return annonceService.findById(id);
	}

	@Operation(summary = "Update a listing and its vehicle")
	@ApiResponse(responseCode = "200", description = "Listing updated")
	@ApiResponse(responseCode = "400", description = "Invalid input")
	@ApiResponse(responseCode = "404", description = "Listing not found")
	@PutMapping("/{id}")
	public AnnonceVente update(@PathVariable Long id, @Valid @RequestBody AnnonceVente annonce) {
		return annonceService.update(id, annonce);
	}

	@Operation(summary = "Delete a listing")
	@ApiResponse(responseCode = "204", description = "Listing deleted")
	@ApiResponse(responseCode = "404", description = "Listing not found")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		annonceService.delete(id);
	}
}
