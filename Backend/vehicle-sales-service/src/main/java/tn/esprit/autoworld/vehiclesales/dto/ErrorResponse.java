package tn.esprit.autoworld.vehiclesales.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/** JSON body returned for every 400 / 404 / 409. */
public record ErrorResponse(
		LocalDateTime timestamp,
		int status,
		String error,
		String message,
		String path,
		@JsonInclude(JsonInclude.Include.NON_EMPTY) Map<String, String> fieldErrors) {
}
