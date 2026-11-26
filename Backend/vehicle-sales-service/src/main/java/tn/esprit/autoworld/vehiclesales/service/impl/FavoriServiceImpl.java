package tn.esprit.autoworld.vehiclesales.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.dto.FavoriResponse;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.entity.Favori;
import tn.esprit.autoworld.vehiclesales.exception.ConflictException;
import tn.esprit.autoworld.vehiclesales.exception.ResourceNotFoundException;
import tn.esprit.autoworld.vehiclesales.mapper.AnnonceMapper;
import tn.esprit.autoworld.vehiclesales.repository.AnnonceVenteRepository;
import tn.esprit.autoworld.vehiclesales.repository.FavoriRepository;
import tn.esprit.autoworld.vehiclesales.service.FavoriService;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriServiceImpl implements FavoriService {

	private final FavoriRepository favoriRepository;
	private final AnnonceVenteRepository annonceRepository;

	@Override
	public FavoriResponse add(Long annonceId, Long userId) {
		AnnonceVente annonce = annonceRepository.findById(annonceId)
				.orElseThrow(() -> new ResourceNotFoundException("Listing " + annonceId + " not found"));
		if (favoriRepository.existsByUserIdAndAnnonceId(userId, annonceId)) {
			throw new ConflictException("Listing " + annonceId + " is already a favorite of user " + userId);
		}
		Favori favori = Favori.builder().userId(userId).annonce(annonce).build();
		return AnnonceMapper.toResponse(favoriRepository.save(favori));
	}

	@Override
	public void remove(Long annonceId, Long userId) {
		Favori favori = favoriRepository.findByUserIdAndAnnonceId(userId, annonceId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Listing " + annonceId + " is not a favorite of user " + userId));
		favoriRepository.delete(favori);
	}
}
