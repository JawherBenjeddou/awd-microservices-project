package tn.esprit.autoworld.vehiclesales.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.dto.AnnonceVenteRequest;
import tn.esprit.autoworld.vehiclesales.dto.AnnonceVenteResponse;
import tn.esprit.autoworld.vehiclesales.dto.PhotoVehiculeRequest;
import tn.esprit.autoworld.vehiclesales.dto.PhotoVehiculeResponse;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.entity.PhotoVehicule;
import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;
import tn.esprit.autoworld.vehiclesales.exception.BadRequestException;
import tn.esprit.autoworld.vehiclesales.exception.ConflictException;
import tn.esprit.autoworld.vehiclesales.exception.ResourceNotFoundException;
import tn.esprit.autoworld.vehiclesales.mapper.AnnonceMapper;
import tn.esprit.autoworld.vehiclesales.repository.AnnonceVenteRepository;
import tn.esprit.autoworld.vehiclesales.repository.AnnonceVenteSpecifications;
import tn.esprit.autoworld.vehiclesales.repository.PhotoVehiculeRepository;
import tn.esprit.autoworld.vehiclesales.service.AnnonceVenteService;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnonceVenteServiceImpl implements AnnonceVenteService {

	private final AnnonceVenteRepository annonceRepository;
	private final PhotoVehiculeRepository photoRepository;

	@Override
	public AnnonceVenteResponse create(AnnonceVenteRequest request) {
		AnnonceVente annonce = AnnonceVente.builder()
				.vendeurId(request.vendeurId())
				.titre(request.titre())
				.description(request.description())
				.prix(request.prix())
				.negociable(Boolean.TRUE.equals(request.negociable()))
				.statut(StatutAnnonce.BROUILLON)
				.vehicule(AnnonceMapper.toEntity(request.vehicule()))
				.build();
		return AnnonceMapper.toResponse(annonceRepository.save(annonce));
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnonceVenteResponse> search(String marque, BigDecimal prixMin, BigDecimal prixMax,
			StatutAnnonce statut) {
		if (prixMin != null && prixMax != null && prixMin.compareTo(prixMax) > 0) {
			throw new BadRequestException("prixMin must not be greater than prixMax");
		}
		return annonceRepository
				.findAll(AnnonceVenteSpecifications.withFilters(marque, prixMin, prixMax, statut),
						Sort.by(Sort.Direction.DESC, "dateCreation", "id"))
				.stream()
				.map(AnnonceMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public AnnonceVenteResponse getById(Long id) {
		return AnnonceMapper.toResponse(findAnnonce(id));
	}

	@Override
	public AnnonceVenteResponse update(Long id, AnnonceVenteRequest request) {
		AnnonceVente annonce = findAnnonce(id);
		annonce.setVendeurId(request.vendeurId());
		annonce.setTitre(request.titre());
		annonce.setDescription(request.description());
		annonce.setPrix(request.prix());
		annonce.setNegociable(Boolean.TRUE.equals(request.negociable()));
		AnnonceMapper.copy(request.vehicule(), annonce.getVehicule());
		return AnnonceMapper.toResponse(annonce);
	}

	@Override
	public void delete(Long id) {
		annonceRepository.delete(findAnnonce(id));
	}

	@Override
	public AnnonceVenteResponse publish(Long id) {
		AnnonceVente annonce = findAnnonce(id);
		requireStatut(annonce, StatutAnnonce.BROUILLON, "published");
		annonce.setStatut(StatutAnnonce.PUBLIEE);
		return AnnonceMapper.toResponse(annonce);
	}

	@Override
	public AnnonceVenteResponse markAsSold(Long id) {
		AnnonceVente annonce = findAnnonce(id);
		requireStatut(annonce, StatutAnnonce.RESERVEE, "marked as sold");
		annonce.setStatut(StatutAnnonce.VENDUE);
		return AnnonceMapper.toResponse(annonce);
	}

	@Override
	public PhotoVehiculeResponse addPhoto(Long id, PhotoVehiculeRequest request) {
		AnnonceVente annonce = findAnnonce(id);
		List<PhotoVehicule> photos = annonce.getPhotos();

		// The first photo is always the cover; later ones only when asked.
		boolean principale = photos.isEmpty() || Boolean.TRUE.equals(request.principale());
		if (principale) {
			photos.forEach(photo -> photo.setPrincipale(false));
		}

		PhotoVehicule photo = PhotoVehicule.builder()
				.url(request.url())
				.ordre(request.ordre() != null ? request.ordre() : photos.size() + 1)
				.principale(principale)
				.annonce(annonce)
				.build();
		return AnnonceMapper.toResponse(photoRepository.save(photo));
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnonceVenteResponse> compare(List<Long> ids) {
		if (ids.stream().distinct().count() != ids.size()) {
			throw new BadRequestException("ids must not contain duplicates");
		}
		return ids.stream().map(this::findAnnonce).map(AnnonceMapper::toResponse).toList();
	}

	private AnnonceVente findAnnonce(Long id) {
		return annonceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Listing " + id + " not found"));
	}

	private void requireStatut(AnnonceVente annonce, StatutAnnonce expected, String action) {
		if (annonce.getStatut() != expected) {
			throw new ConflictException("Listing " + annonce.getId() + " cannot be " + action + ": status is "
					+ annonce.getStatut() + ", expected " + expected);
		}
	}
}
