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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.service.AnnonceVenteService;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class AnnonceVenteController {

	private final AnnonceVenteService annonceService;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${server.port}")
	private String port;

	/** Shows which instance answered when several run on different ports. */
	@GetMapping("/hello")
	public String hello() {
		return "Hello from " + applicationName + " on port " + port;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AnnonceVente create(@Valid @RequestBody AnnonceVente annonce) {
		return annonceService.create(annonce);
	}

	@GetMapping
	public List<AnnonceVente> findAll() {
		return annonceService.findAll();
	}

	@GetMapping("/{id}")
	public AnnonceVente findById(@PathVariable Long id) {
		return annonceService.findById(id);
	}

	@PutMapping("/{id}")
	public AnnonceVente update(@PathVariable Long id, @Valid @RequestBody AnnonceVente annonce) {
		return annonceService.update(id, annonce);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		annonceService.delete(id);
	}
}
