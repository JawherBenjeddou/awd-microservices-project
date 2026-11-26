package tn.esprit.autoworld.vehiclesales.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "annonce_vente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnonceVente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Seller profile id in MS5 (profiles). External reference: no foreign key. */
	@Column(nullable = false)
	private Long vendeurId;

	@Column(nullable = false, length = 150)
	private String titre;

	@Column(length = 2000)
	private String description;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal prix;

	@Column(nullable = false)
	private boolean negociable;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatutAnnonce statut;

	@Column(nullable = false, updatable = false)
	private LocalDateTime dateCreation;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
	@JoinColumn(name = "vehicule_id", nullable = false, unique = true)
	private Vehicule vehicule;

	@OneToMany(mappedBy = "annonce", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("ordre ASC")
	@Builder.Default
	private List<PhotoVehicule> photos = new ArrayList<>();

	// REMOVE only: offers and favorites are created through their own repositories,
	// but must disappear with the listing so that DELETE does not hit a foreign key.
	@OneToMany(mappedBy = "annonce", cascade = CascadeType.REMOVE)
	@JsonIgnore
	@Builder.Default
	private List<OffreAchat> offres = new ArrayList<>();

	@OneToMany(mappedBy = "annonce", cascade = CascadeType.REMOVE)
	@JsonIgnore
	@Builder.Default
	private List<Favori> favoris = new ArrayList<>();

	@PrePersist
	void onCreate() {
		if (dateCreation == null) {
			dateCreation = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		}
		if (statut == null) {
			statut = StatutAnnonce.BROUILLON;
		}
	}
}
