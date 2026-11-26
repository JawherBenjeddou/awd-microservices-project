package tn.esprit.autoworld.vehiclesales.entity;

/**
 * Life cycle of a listing: BROUILLON -> PUBLIEE -> RESERVEE -> VENDUE.
 * RETIREE is for a listing withdrawn by its seller.
 */
public enum StatutAnnonce {
	BROUILLON,
	PUBLIEE,
	RESERVEE,
	VENDUE,
	RETIREE
}
