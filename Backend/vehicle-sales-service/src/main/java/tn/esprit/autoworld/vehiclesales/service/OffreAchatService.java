package tn.esprit.autoworld.vehiclesales.service;

import tn.esprit.autoworld.vehiclesales.dto.OffreAchatRequest;
import tn.esprit.autoworld.vehiclesales.dto.OffreAchatResponse;
import tn.esprit.autoworld.vehiclesales.entity.StatutOffre;

public interface OffreAchatService {

	/** Only possible on a PUBLIEE listing. */
	OffreAchatResponse create(Long annonceId, OffreAchatRequest request);

	/**
	 * Accepts or refuses a pending offer. Accepting reserves the listing and
	 * refuses its other pending offers.
	 */
	OffreAchatResponse decide(Long offreId, StatutOffre decision);
}
