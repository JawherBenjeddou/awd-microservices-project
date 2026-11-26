package tn.esprit.autoworld.vehiclesales.dto;

public record PhotoVehiculeResponse(
		Long id,
		String url,
		Integer ordre,
		boolean principale) {
}
