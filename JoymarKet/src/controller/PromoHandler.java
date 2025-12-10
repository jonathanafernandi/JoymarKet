package controller;

import java.util.List;

import model.Promo;

public class PromoHandler {
	
	public static List<Promo> getAllPromos() {
		return Promo.getAllPromos();
	}
	
	public static Promo getPromo(String code) {
		return Promo.getPromo(code);
	}
	
	public static String validatePromoCode(String code) {
		if (code == null || code.trim().isEmpty()) {
			return null;
		}
		
		Promo promo = Promo.getPromo(code.trim());
		if (promo == null) {
			return "Invalid promo code.";
		}
		
		return null;
	}

}
