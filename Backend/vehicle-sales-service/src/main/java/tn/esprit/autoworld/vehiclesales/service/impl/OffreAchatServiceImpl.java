package tn.esprit.autoworld.vehiclesales.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.dto.OffreAchatRequest;
import tn.esprit.autoworld.vehiclesales.dto.OffreAchatResponse;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.entity.OffreAchat;
import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;
import tn.esprit.autoworld.vehiclesales.entity.StatutOffre;
import tn.esprit.autoworld.vehiclesales.exception.BadRequestException;
import tn.esprit.autoworld.vehiclesales.exception.ConflictException;
import tn.esprit.autoworld.vehiclesales.exception.ResourceNotFoundException;
import tn.esprit.autoworld.vehiclesales.mapper.AnnonceMapper;
import tn.esprit.autoworld.vehiclesales.repository.AnnonceVenteRepository;
import tn.esprit.autoworld.vehiclesales.repository.OffreAchatRepository;
import tn.esprit.autoworld.vehiclesales.service.OffreAchatService;

@Service
@RequiredArgsConstructor
@Transactional
public class OffreAchatServiceImpl implements OffreAchatService {

	private final OffreAchatRepository offreRepository;
	private final AnnonceVenteRepository annonceRepository;

	@Override
	public OffreAchatResponse create(Long annonceId, OffreAchatRequest request) {
		AnnonceVente annonce = annonceRepository.findById(annonceId)
				.orElseThrow(() -> new ResourceNotFoundException("Listing " + annonceId + " not found"));
		if (annonce.getStatut() != StatutAnnonce.PUBLIEE) {
			throw new ConflictException("Listing " + annonceId + " does not accept offers: status is "
					+ annonce.getStatut() + ", expected " + StatutAnnonce.PUBLIEE);
		}
		OffreAchat offre = OffreAchat.builder()
				.acheteurId(request.acheteurId())
				.montant(request.montant())
				.message(request.message())
				.statut(StatutOffre.EN_ATTENTE)
				.annonce(annonce)
				.build();
		return AnnonceMapper.toResponse(offreRepository.save(offre));
	}

	@Override
	public OffreAchatResponse decide(Long offreId, StatutOffre decision) {
		if (decision != StatutOffre.ACCEPTEE && decision != StatutOffre.REFUSEE) {
			throw new BadRequestException("decision must be ACCEPTEE or REFUSEE");
		}
		OffreAchat offre = offreRepository.findById(offreId)
				.orElseThrow(() -> new ResourceNotFoundException("Offer " + offreId + " not found"));
		if (offre.getStatut() != StatutOffre.EN_ATTENTE) {
			throw new ConflictException("Offer " + offreId + " was already decided: status is " + offre.getStatut());
		}

		if (decision == StatutOffre.REFUSEE) {
			offre.setStatut(StatutOffre.REFUSEE);
			return AnnonceMapper.toResponse(offre);
		}

		AnnonceVente annonce = offre.getAnnonce();
		if (annonce.getStatut() != StatutAnnonce.PUBLIEE) {
			throw new ConflictException("Offer " + offreId + " cannot be accepted: listing " + annonce.getId()
					+ " has status " + annonce.getStatut());
		}
		// Accepting one offer reserves the vehicle and closes the competing offers.
		offreRepository.findByAnnonceIdAndStatut(annonce.getId(), StatutOffre.EN_ATTENTE).stream()
				.filter(other -> !other.getId().equals(offre.getId()))
				.forEach(other -> other.setStatut(StatutOffre.REFUSEE));
		offre.setStatut(StatutOffre.ACCEPTEE);
		annonce.setStatut(StatutAnnonce.RESERVEE);
		return AnnonceMapper.toResponse(offre);
	}
}
