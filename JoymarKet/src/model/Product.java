package model;

import java.util.List;

import database.ProductDA;

public class Product {

	private String idProduct;
	private String name;
	private double price;
	private int stock;
	private String category;
	
	public Product(String idProduct, String name, double price, int stock, String category) {
		this.idProduct = idProduct;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.category = category;
	}

	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public static Product getProduct(String idProduct) {
		ProductDA productDA = new ProductDA();
		return productDA.getProduct(idProduct);
	}
	
	public boolean editProductStock(int newStock) {
		this.stock = newStock;
		ProductDA productDA = new ProductDA();
		return productDA.updateProductStock(this.idProduct, newStock);
	}
	
	public static Product getAvailableProduct(String idProduct) {
		ProductDA productDA = new ProductDA();
		Product product = productDA.getProduct(idProduct);
		
		if (product != null && product.getStock() > 0) {
			return product;
		}
		return null;
	}
	
	public static List<Product> getAllProducts() {
	    ProductDA productDA = new ProductDA();
	    return productDA.getAllProducts();
	}

	public static List<Product> getAvailableProducts() {
	    ProductDA productDA = new ProductDA();
	    return productDA.getAvailableProducts();
	}

}
