package tn.esprit.autoworld.vehiclesales.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "photo_vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoVehicule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 500)
	private String url;

	/** Display position, starting at 1. */
	@Column(nullable = false)
	private Integer ordre;

	/** Cover photo of the listing. At most one per listing. */
	@Column(nullable = false)
	private boolean principale;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "annonce_id", nullable = false)
	@JsonIgnore
	private AnnonceVente annonce;
}
