package model;

import java.util.List;

import database.CourierDA;

// Courier model class that extends User
public class Courier extends User {

	private String vehicleType;
	private String vehiclePlate;
	
    // Constructor to initialize courier data
	public Courier(String idUser, String fullName, String email, String password, String phone, String address,
			String role, String vehicleType, String vehiclePlate) {
		super(idUser, fullName, email, password, phone, address, role);
		this.vehicleType = vehicleType;
		this.vehiclePlate = vehiclePlate;
	}

    // Get vehicle type
	public String getVehicleType() {
		return vehicleType;
	}

    // Set vehicle type
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

    // Get vehicle plate number
	public String getVehiclePlate() {
		return vehiclePlate;
	}

    // Set vehicle plate number
	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}
	
    // Get courier data by courier ID
	public static Courier getCourier(String idCourier) {
		CourierDA courierDA = new CourierDA();
		return courierDA.getCourier(idCourier);
	}
	
    // Get all couriers from database
	public static List<Courier> getAllCouriers() {
	    CourierDA courierDA = new CourierDA();
	    return courierDA.getAllCouriers();
	}

}
