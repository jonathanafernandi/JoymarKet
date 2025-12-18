package controller;

import java.util.List;

import model.Promo;

// Controller that handles promo-related logic
public class PromoHandler {
	
    // Get all available promo codes
	public static List<Promo> getAllPromos() {
		return Promo.getAllPromos();
	}
	
    // Get promo data based on promo code
	public static Promo getPromo(String code) {
		return Promo.getPromo(code);
	}
	
    // Validate promo code input
	public static String validatePromoCode(String code) {

        // Promo code is optional
		if (code == null || code.trim().isEmpty()) {
			return null;
		}
		
        // Check if promo exists
		Promo promo = Promo.getPromo(code.trim());
		if (promo == null) {
			return "Invalid promo code.";
		}
		
		return null;
	}

}
