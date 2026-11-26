package tn.esprit.autoworld.vehiclesales.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;

public record AnnonceVenteResponse(
		Long id,
		Long vendeurId,
		String titre,
		String description,
		BigDecimal prix,
		boolean negociable,
		StatutAnnonce statut,
		LocalDateTime dateCreation,
		VehiculeResponse vehicule,
		List<PhotoVehiculeResponse> photos,
		int nombreOffres,
		int nombreFavoris) {
}
