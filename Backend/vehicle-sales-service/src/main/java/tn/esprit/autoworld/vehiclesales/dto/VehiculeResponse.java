package tn.esprit.autoworld.vehiclesales.dto;

import tn.esprit.autoworld.vehiclesales.entity.Carburant;

public record VehiculeResponse(
		Long id,
		String marque,
		String modele,
		Integer annee,
		Integer kilometrage,
		Carburant carburant) {
}
