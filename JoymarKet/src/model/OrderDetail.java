package model;

import java.util.List;

import database.OrderDetailDA;

// Model class that represents order detail (items in an order)
public class OrderDetail {

	private String idOrder;
	private String idProduct;
	private int qty;
	
    // Constructor to initialize order detail data
	public OrderDetail(String idOrder, String idProduct, int qty) {
		this.idOrder = idOrder;
		this.idProduct = idProduct;
		this.qty = qty;
	}

    // Get order ID
	public String getIdOrder() {
		return idOrder;
	}

    // Set order ID
	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

    // Get product ID
	public String getIdProduct() {
		return idProduct;
	}

    // Set product ID
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

    // Get quantity of product
	public int getQty() {
		return qty;
	}

    // Set quantity of product
	public void setQty(int qty) {
		this.qty = qty;
	}
	
    // Create new order detail in database
	public static boolean createOrderDetail(String idOrder, String idProduct, int qty) {
		OrderDetail orderDetail = new OrderDetail(idOrder, idProduct, qty);
		OrderDetailDA orderDetailDA = new OrderDetailDA();
		return orderDetailDA.saveOrderDetail(orderDetail);
	}
	
    // Get specific order detail by order ID and product ID
	public static OrderDetail getOrderDetail(String idOrder, String idProduct) {
		OrderDetailDA orderDetailDA = new OrderDetailDA();
		return orderDetailDA.getOrderDetail(idOrder, idProduct);
	}
	
    // Get order detail for customer order
	public static OrderDetail getCustomerOrderDetail(String idOrder, String idProduct) {
		OrderDetailDA orderDetailDA = new OrderDetailDA();
		return orderDetailDA.getCustomerOrderDetail(idOrder, idProduct);
	}
	
    // Get all order details for a specific order
	public static List<OrderDetail> getOrderDetails(String idOrder) {
	    OrderDetailDA orderDetailDA = new OrderDetailDA();
	    return orderDetailDA.getOrderDetails(idOrder);
	}

}
