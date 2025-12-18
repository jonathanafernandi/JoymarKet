package model;

import java.util.List;

import database.ProductDA;

// Product model class that represents product data
public class Product {

	private String idProduct;
	private String name;
	private double price;
	private int stock;
	private String category;
	
    // Constructor to initialize product data
	public Product(String idProduct, String name, double price, int stock, String category) {
		this.idProduct = idProduct;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.category = category;
	}

    // Get product ID
	public String getIdProduct() {
		return idProduct;
	}

    // Set product ID
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

    // Get product name
	public String getName() {
		return name;
	}

    // Set product name
	public void setName(String name) {
		this.name = name;
	}

    // Get product price
	public double getPrice() {
		return price;
	}

    // Set product price
	public void setPrice(double price) {
		this.price = price;
	}

    // Get product stock
	public int getStock() {
		return stock;
	}

    // Set product stock
	public void setStock(int stock) {
		this.stock = stock;
	}

    // Get product category
	public String getCategory() {
		return category;
	}

    // Set product category
	public void setCategory(String category) {
		this.category = category;
	}
	
    // Get product data by product ID
	public static Product getProduct(String idProduct) {
		ProductDA productDA = new ProductDA();
		return productDA.getProduct(idProduct);
	}
	
    // Update product stock
	public boolean editProductStock(int newStock) {
		this.stock = newStock;
		ProductDA productDA = new ProductDA();
		return productDA.updateProductStock(this.idProduct, newStock);
	}
	
    // Get product if it is available (stock > 0)
	public static Product getAvailableProduct(String idProduct) {
		ProductDA productDA = new ProductDA();
		Product product = productDA.getProduct(idProduct);
		
		if (product != null && product.getStock() > 0) {
			return product;
		}
		return null;
	}
	
    // Get all products
	public static List<Product> getAllProducts() {
	    ProductDA productDA = new ProductDA();
	    return productDA.getAllProducts();
	}

    // Get products that are available
	public static List<Product> getAvailableProducts() {
	    ProductDA productDA = new ProductDA();
	    return productDA.getAvailableProducts();
	}

}
