package tn.esprit.autoworld.vehiclesales.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompareRequest(
		@NotEmpty @Size(max = 3, message = "a comparison accepts at most 3 listings") List<@NotNull Long> ids) {
}
