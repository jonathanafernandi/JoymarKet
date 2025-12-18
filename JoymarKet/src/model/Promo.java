package model;

import java.util.List;

import database.PromoDA;

// Promo model class that represents discount promotion data
public class Promo {

	private String idPromo;
	private String code;
	private String headline;
	private double discountPercentage;
	
    // Constructor to initialize promo data
	public Promo(String idPromo, String code, String headline, double discountPercentage) {
		this.idPromo = idPromo;
		this.code = code;
		this.headline = headline;
		this.discountPercentage = discountPercentage;
	}

    // Get promo ID
	public String getIdPromo() {
		return idPromo;
	}

    // Set promo ID
	public void setIdPromo(String idPromo) {
		this.idPromo = idPromo;
	}

    // Get promo code
	public String getCode() {
		return code;
	}

    // Set promo code
	public void setCode(String code) {
		this.code = code;
	}

    // Get promo headline
	public String getHeadline() {
		return headline;
	}

    // Set promo headline
	public void setHeadline(String headline) {
		this.headline = headline;
	}

    // Get discount percentage
	public double getDiscountPercentage() {
		return discountPercentage;
	}

    // Set discount percentage
	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	
    // Get promo data using promo code
	public static Promo getPromo(String code) {
		PromoDA promoDA = new PromoDA();
		return promoDA.getPromoByCode(code);
	}
	
    // Get all promos from database
	public static List<Promo> getAllPromos() {
	    PromoDA promoDA = new PromoDA();
	    return promoDA.getAllPromos();
	}

}
