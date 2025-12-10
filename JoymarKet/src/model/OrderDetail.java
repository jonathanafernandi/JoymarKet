package model;

import java.util.List;

import database.OrderDetailDA;

public class OrderDetail {

	private String idOrder;
	private String idProduct;
	private int qty;
	
	public OrderDetail(String idOrder, String idProduct, int qty) {
		this.idOrder = idOrder;
		this.idProduct = idProduct;
		this.qty = qty;
	}

	public String getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public static boolean createOrderDetail(String idOrder, String idProduct, int qty) {
		OrderDetail orderDetail = new OrderDetail(idOrder, idProduct, qty);
		OrderDetailDA orderDetailDA = new OrderDetailDA();
		return orderDetailDA.saveOrderDetail(orderDetail);
	}
	
	public static OrderDetail getOrderDetail(String idOrder, String idProduct) {
		OrderDetailDA orderDetailDA = new OrderDetailDA();
		return orderDetailDA.getOrderDetail(idOrder, idProduct);
	}
	
	public static OrderDetail getCustomerOrderDetail(String idOrder, String idProduct) {
		OrderDetailDA orderDetailDA = new OrderDetailDA();
		return orderDetailDA.getCustomerOrderDetail(idOrder, idProduct);
	}
	
	public static List<OrderDetail> getOrderDetails(String idOrder) {
	    OrderDetailDA orderDetailDA = new OrderDetailDA();
	    return orderDetailDA.getOrderDetails(idOrder);
	}

}
