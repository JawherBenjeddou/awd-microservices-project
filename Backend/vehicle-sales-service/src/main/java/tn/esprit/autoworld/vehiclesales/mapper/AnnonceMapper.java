package tn.esprit.autoworld.vehiclesales.mapper;

import tn.esprit.autoworld.vehiclesales.dto.AnnonceVenteResponse;
import tn.esprit.autoworld.vehiclesales.dto.FavoriResponse;
import tn.esprit.autoworld.vehiclesales.dto.OffreAchatResponse;
import tn.esprit.autoworld.vehiclesales.dto.PhotoVehiculeResponse;
import tn.esprit.autoworld.vehiclesales.dto.VehiculeRequest;
import tn.esprit.autoworld.vehiclesales.dto.VehiculeResponse;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.entity.Favori;
import tn.esprit.autoworld.vehiclesales.entity.OffreAchat;
import tn.esprit.autoworld.vehiclesales.entity.PhotoVehicule;
import tn.esprit.autoworld.vehiclesales.entity.Vehicule;

/**
 * Entity to DTO conversions. Call these inside a transaction: they read lazy collections.
 */
public final class AnnonceMapper {

	private AnnonceMapper() {
	}

	public static AnnonceVenteResponse toResponse(AnnonceVente annonce) {
		return new AnnonceVenteResponse(
				annonce.getId(),
				annonce.getVendeurId(),
				annonce.getTitre(),
				annonce.getDescription(),
				annonce.getPrix(),
				annonce.isNegociable(),
				annonce.getStatut(),
				annonce.getDateCreation(),
				toResponse(annonce.getVehicule()),
				annonce.getPhotos().stream().map(AnnonceMapper::toResponse).toList(),
				annonce.getOffres().size(),
				annonce.getFavoris().size());
	}

	public static VehiculeResponse toResponse(Vehicule vehicule) {
		return new VehiculeResponse(
				vehicule.getId(),
				vehicule.getMarque(),
				vehicule.getModele(),
				vehicule.getAnnee(),
				vehicule.getKilometrage(),
				vehicule.getCarburant());
	}

	public static PhotoVehiculeResponse toResponse(PhotoVehicule photo) {
		return new PhotoVehiculeResponse(photo.getId(), photo.getUrl(), photo.getOrdre(), photo.isPrincipale());
	}

	public static OffreAchatResponse toResponse(OffreAchat offre) {
		return new OffreAchatResponse(
				offre.getId(),
				offre.getAnnonce().getId(),
				offre.getAnnonce().getStatut(),
				offre.getAcheteurId(),
				offre.getMontant(),
				offre.getMessage(),
				offre.getStatut(),
				offre.getDateOffre());
	}

	public static FavoriResponse toResponse(Favori favori) {
		return new FavoriResponse(favori.getId(), favori.getAnnonce().getId(), favori.getUserId(),
				favori.getDateAjout());
	}

	public static Vehicule toEntity(VehiculeRequest request) {
		return Vehicule.builder()
				.marque(request.marque())
				.modele(request.modele())
				.annee(request.annee())
				.kilometrage(request.kilometrage())
				.carburant(request.carburant())
				.build();
	}

	public static void copy(VehiculeRequest request, Vehicule vehicule) {
		vehicule.setMarque(request.marque());
		vehicule.setModele(request.modele());
		vehicule.setAnnee(request.annee());
		vehicule.setKilometrage(request.kilometrage());
		vehicule.setCarburant(request.carburant());
	}
}
