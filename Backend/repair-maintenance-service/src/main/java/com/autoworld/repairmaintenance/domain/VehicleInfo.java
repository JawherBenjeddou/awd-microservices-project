package com.autoworld.repairmaintenance.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class VehicleInfo {

	@Column(name = "vehicle_brand", length = 80)
	private String vehicleBrand;

	@Column(name = "vehicle_model", length = 80)
	private String vehicleModel;

	@Column(name = "vehicle_year")
	private Integer vehicleYear;

	@Column(name = "vehicle_license_plate", length = 32)
	private String licensePlate;

	@Column(name = "vehicle_mileage_km")
	private Integer mileageKm;

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Integer getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Integer getMileageKm() {
		return mileageKm;
	}

	public void setMileageKm(Integer mileageKm) {
		this.mileageKm = mileageKm;
	}

}
