package tn.esprit.autoworld.vehiclesales.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.dto.AnnonceVenteRequest;
import tn.esprit.autoworld.vehiclesales.dto.AnnonceVenteResponse;
import tn.esprit.autoworld.vehiclesales.dto.CompareRequest;
import tn.esprit.autoworld.vehiclesales.dto.FavoriRequest;
import tn.esprit.autoworld.vehiclesales.dto.FavoriResponse;
import tn.esprit.autoworld.vehiclesales.dto.OffreAchatRequest;
import tn.esprit.autoworld.vehiclesales.dto.OffreAchatResponse;
import tn.esprit.autoworld.vehiclesales.dto.PhotoVehiculeRequest;
import tn.esprit.autoworld.vehiclesales.dto.PhotoVehiculeResponse;
import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;
import tn.esprit.autoworld.vehiclesales.service.AnnonceVenteService;
import tn.esprit.autoworld.vehiclesales.service.FavoriService;
import tn.esprit.autoworld.vehiclesales.service.OffreAchatService;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
@Tag(name = "Listings", description = "Vehicle sale listings, photos, offers and favorites")
public class AnnonceVenteController {

	private final AnnonceVenteService annonceService;
	private final OffreAchatService offreService;
	private final FavoriService favoriService;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${server.port}")
	private String port;

	@GetMapping("/hello")
	@Operation(summary = "Shows which instance answered")
	public String hello() {
		return "Hello from " + applicationName + " on port " + port;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a listing and its vehicle (status BROUILLON)")
	public AnnonceVenteResponse create(@Valid @RequestBody AnnonceVenteRequest request) {
		return annonceService.create(request);
	}

	@GetMapping
	@Operation(summary = "List listings, optionally filtered by marque, price range and statut")
	public List<AnnonceVenteResponse> search(
			@RequestParam(required = false) String marque,
			@RequestParam(required = false) BigDecimal prixMin,
			@RequestParam(required = false) BigDecimal prixMax,
			@RequestParam(required = false) StatutAnnonce statut) {
		return annonceService.search(marque, prixMin, prixMax, statut);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get one listing")
	public AnnonceVenteResponse getById(@PathVariable Long id) {
		return annonceService.getById(id);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a listing and its vehicle")
	public AnnonceVenteResponse update(@PathVariable Long id, @Valid @RequestBody AnnonceVenteRequest request) {
		return annonceService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete a listing with its vehicle, photos, offers and favorites")
	public void delete(@PathVariable Long id) {
		annonceService.delete(id);
	}

	@PutMapping("/{id}/publish")
	@Operation(summary = "Publish a listing (BROUILLON -> PUBLIEE)")
	public AnnonceVenteResponse publish(@PathVariable Long id) {
		return annonceService.publish(id);
	}

	@PutMapping("/{id}/sold")
	@Operation(summary = "Mark a listing as sold (RESERVEE -> VENDUE)")
	public AnnonceVenteResponse markAsSold(@PathVariable Long id) {
		return annonceService.markAsSold(id);
	}

	@PostMapping("/{id}/photos")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add a photo to a listing")
	public PhotoVehiculeResponse addPhoto(@PathVariable Long id, @Valid @RequestBody PhotoVehiculeRequest request) {
		return annonceService.addPhoto(id, request);
	}

	@PostMapping("/compare")
	@Operation(summary = "Compare up to 3 listings")
	public List<AnnonceVenteResponse> compare(@Valid @RequestBody CompareRequest request) {
		return annonceService.compare(request.ids());
	}

	@PostMapping("/{id}/offers")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Make a purchase offer on a PUBLIEE listing")
	public OffreAchatResponse createOffer(@PathVariable Long id, @Valid @RequestBody OffreAchatRequest request) {
		return offreService.create(id, request);
	}

	@PostMapping("/{id}/favorites")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add a listing to a user's favorites")
	public FavoriResponse addFavorite(@PathVariable Long id, @Valid @RequestBody FavoriRequest request) {
		return favoriService.add(id, request.userId());
	}

	@DeleteMapping("/{id}/favorites")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Remove a listing from a user's favorites")
	public void removeFavorite(@PathVariable Long id, @RequestParam Long userId) {
		favoriService.remove(id, userId);
	}
}
