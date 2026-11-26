package tn.esprit.autoworld.vehiclesales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * ordre and principale are optional: by default the photo is added at the end,
 * and the first photo of a listing becomes its cover.
 */
public record PhotoVehiculeRequest(
		@NotBlank @Size(max = 500) String url,
		@Positive Integer ordre,
		Boolean principale) {
}
