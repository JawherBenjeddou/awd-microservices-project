package tn.esprit.autoworld.vehiclesales.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.dto.DecisionOffreRequest;
import tn.esprit.autoworld.vehiclesales.dto.OffreAchatResponse;
import tn.esprit.autoworld.vehiclesales.service.OffreAchatService;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@Tag(name = "Offers", description = "Decisions on purchase offers")
public class OffreAchatController {

	private final OffreAchatService offreService;

	@PutMapping("/{id}/decision")
	@Operation(summary = "Accept or refuse a pending offer (accepting reserves the listing)")
	public OffreAchatResponse decide(@PathVariable Long id, @Valid @RequestBody DecisionOffreRequest request) {
		return offreService.decide(id, request.decision());
	}
}
