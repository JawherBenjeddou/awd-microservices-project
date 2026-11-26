package tn.esprit.autoworld.vehiclesales.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import tn.esprit.autoworld.vehiclesales.entity.Carburant;

public record VehiculeRequest(
		@NotBlank @Size(max = 60) String marque,
		@NotBlank @Size(max = 60) String modele,
		@NotNull @Min(1900) @Max(2100) Integer annee,
		@NotNull @PositiveOrZero Integer kilometrage,
		@NotNull Carburant carburant) {
}
