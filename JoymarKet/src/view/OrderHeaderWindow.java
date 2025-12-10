package view;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import controller.OrderHeaderHandler;
import controller.ProductHandler;
import controller.PromoHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.OrderDetail;
import model.OrderHeader;
import model.Product;
import model.Promo;

public class OrderHeaderWindow {

	public static void showCustomerOrders(Stage parentStage, Customer customer) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("My Orders - JoymarKet");
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #667EEA");
		
		Label titleLabel = new Label("My Order History");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label customerLabel = new Label(customer.getFullName());
		customerLabel.setFont(Font.font("Arial", 14));
		customerLabel.setTextFill(Color.WHITE);
		
		topBar.getChildren().addAll(titleLabel, spacer, customerLabel);
		root.setTop(topBar);
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(20));
		
		List<OrderHeader> orders = OrderHeaderHandler.getCustomerOrders(customer.getIdUser());
		
		if (orders.isEmpty()) {
			VBox emptyBox = new VBox(20);
			emptyBox.setAlignment(Pos.CENTER);
			emptyBox.setPadding(new Insets(50));
			
			Label messageLabel = new Label("No orders yet");
			messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
			messageLabel.setTextFill(Color.GRAY);
			
			Label subLabel = new Label("Start shopping to place your first order!");
			subLabel.setFont(Font.font("Arial", 14));
			subLabel.setTextFill(Color.GRAY);
			
			Button shopButton = new Button("Start Shopping");
			shopButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding 12px 30px; -fx-cursor: hand; -fx-background-radius: 5;");
			shopButton.setOnAction(e -> {
				stage.close();
				ProductWindow.showProductsForCustomer(parentStage, customer);
			});
			
			emptyBox.getChildren().addAll(messageLabel, subLabel, shopButton);
			centerBox.getChildren().add(emptyBox);
		} else {
			Label infoLabel = new Label("Total Orders: " + orders.size());
			infoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			scrollPane.setPrefWidth(450);
			
			VBox orderList = new VBox(15);
			orderList.setPadding(new Insets(10));
			
			for (OrderHeader order : orders) {
				VBox orderCard = createOrderCard(order, stage);
				orderList.getChildren().add(orderCard);
			}
			
			scrollPane.setContent(orderList);
			
			centerBox.getChildren().addAll(infoLabel, scrollPane);
		}
		
		Button closeButton = new Button("Close");
		closeButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-cursor: hand;");
		closeButton.setOnAction(e -> stage.close());
		
		HBox bottomBox = new HBox();
		bottomBox.setPadding(new Insets(15));
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.getChildren().add(closeButton);
		
		root.setCenter(centerBox);
		root.setBottom(bottomBox);
		
		Scene scene = new Scene(root, 800, 650);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void showAllOrders(Stage parentStage) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("All Orders - JoymarKet Admin");
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #2196F3;");
		
		Label titleLabel = new Label("All Orders (Admin View)");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		titleLabel.setTextFill(Color.WHITE);
		
		topBar.getChildren().add(titleLabel);
		root.setTop(topBar);
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(20));
		
		HBox filterBox = new HBox(15);
		filterBox.setAlignment(Pos.CENTER_LEFT);
		
		Label filterLabel = new Label("Filter by Status:");
		filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
		
		ComboBox<String> statusCombo = new ComboBox<>();
		statusCombo.getItems().addAll("All", "Pending", "In Progress", "Delivered");
		statusCombo.setValue("All");
		statusCombo.setStyle("-fx-font-size: 13px;");
		
		Button refreshButton = new Button("Refresh");
		refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 8px 15px; -fx-cursor: hand;");
		
		filterBox.getChildren().addAll(filterLabel, statusCombo, refreshButton);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefHeight(450);
		
		VBox orderList = new VBox(15);
		orderList.setPadding(new Insets(10));
		
		Runnable loadOrders = () -> {
			orderList.getChildren().clear();
			List<OrderHeader> orders = OrderHeaderHandler.getAllOrders();
			
			String selectedStatus = statusCombo.getValue();
			int count = 0;
			
			for (OrderHeader order : orders) {
				if (selectedStatus.equals("All") || order.getStatus().equals(selectedStatus)) {
					VBox orderCard = createOrderCardForAdmin(order, stage);
					orderList.getChildren().add(orderCard);
					count++;
				}
			}
			
			if (count == 0) {
				Label emptyLabel = new Label("No orders found with status: " + selectedStatus);
				emptyLabel.setFont(Font.font("Arial", 14));
				emptyLabel.setTextFill(Color.GRAY);
				orderList.getChildren().add(emptyLabel);;
			}
		};
		
		loadOrders.run();
		
		statusCombo.setOnAction(e -> loadOrders.run());
		refreshButton.setOnAction(e -> loadOrders.run());
		
		scrollPane.setContent(orderList);
		
		Button closeButton = new Button("Close");
		closeButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-cursor: hand;");
		closeButton.setOnAction(e -> stage.close());
		
		centerBox.getChildren().addAll(filterBox, scrollPane, closeButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 850, 650);
		stage.setScene(scene);
		stage.show();;
	}
	
	private static void showOrderDetails(Stage parentStage, OrderHeader order) {
		Alert detailsDialog = new Alert(Alert.AlertType.INFORMATION);
		detailsDialog.setTitle("Order Details - " + order.getIdOrder());
		detailsDialog.setHeaderText("Order #" + order.getIdOrder());
		
		List<OrderDetail> details = OrderHeaderHandler.getOrderDetails(order.getIdOrder());
		
		StringBuilder content = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm::ss");
		
		content.append("Order Date: ").append(dateFormat.format(order.getOrderedAt())).append("\n");
		content.append("Customer ID: ").append(order.getIdCustomer()).append("\n");
		content.append("Status: ").append(order.getStatus()).append("\n\n");
		
		content.append("ORDERED ITEMS:\n\n");
		
		for (OrderDetail detail : details) {
			Product product = ProductHandler.getProduct(detail.getIdProduct());
			if (product != null) {
				double itemTotal = product.getPrice() * detail.getQty();
				content.append(product.getName()).append("\n");
				content.append("Price: Rp").append(formatCurrency(product.getPrice())).append(" x ").append(detail.getQty()).append("\n");
				content.append("Subtotal: Rp").append(formatCurrency(itemTotal)).append("\n\n");
			}
		}
		
		content.append("\n");
		
		if (order.getIdPromo() != null) {
			Promo promo = PromoHandler.getPromo(order.getIdPromo());
			if (promo != null) {
				content.append("Promo Applied: ").append(promo.getCode()).append(" (").append((int) (promo.getDiscountPercentage() * 100)).append("% discount)\n");
			}
		}
		
		content.append("\nTOTAL AMOUNT: Rp").append(formatCurrency(order.getTotalAmount()));
		
		detailsDialog.setContentText(content.toString());
		detailsDialog.getDialogPane().setPrefWidth(500);
		detailsDialog.showAndWait();
	}
	
	private static VBox createOrderCard(OrderHeader order, Stage parentStage) {
		VBox card = new VBox(15);
		card.setPadding(new Insets(20));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);");
		
		HBox headerRow = new HBox(20);
		headerRow.setAlignment(Pos.CENTER_LEFT);
		
		Label orderIdLabel = new Label("Order #" + order.getIdOrder());
		orderIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label statusLabel = new Label(order.getStatus());
		statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		statusLabel.setPadding(new Insets(5, 15, 5, 15));
		statusLabel.setStyle("-fx-background-radius: 15; -fx-background-color: " + getStatusBackgroundColor(order.getStatus()) + ";");
		statusLabel.setTextFill(Color.WHITE);
		
		headerRow.getChildren().addAll(orderIdLabel, spacer, statusLabel);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm");
		Label dateLabel = new Label(dateFormat.format(order.getOrderedAt()));
		dateLabel.setFont(Font.font("Arial", 13));
		dateLabel.setTextFill(Color.web("#666666"));
		
		Label amountLabel = new Label("Total: Rp" + formatCurrency(order.getTotalAmount()));
		amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		amountLabel.setTextFill(Color.web("#2E7D32"));
		
		Label promoLabel = new Label();
		if (order.getIdPromo() != null) {
			Promo promo = PromoHandler.getPromo(order.getIdPromo());
			if (promo != null) {
				promoLabel.setText("Promo: " + promo.getCode() + " (" + (int) (promo.getDiscountPercentage() * 100) + "% off)");
				promoLabel.setFont(Font.font("Arial", 12));
				promoLabel.setTextFill(Color.web("#9C27B0"));
			}
		}
		
		Separator separator = new Separator();
		
		Button viewDetailsButton = new Button("View Order Details");
		viewDetailsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 8px 20px; -fx-cursor: hand;");
		
		viewDetailsButton.setOnAction(e -> showOrderDetails(parentStage, order));
		
		card.getChildren().addAll(headerRow, dateLabel, amountLabel);
		if (order.getIdPromo() != null) {
			card.getChildren().add(promoLabel);
		}
		card.getChildren().addAll(separator, viewDetailsButton);
		
		return card;
	}
	
	private static VBox createOrderCardForAdmin(OrderHeader order, Stage parentStage) {
		VBox card = new VBox(12);
		card.setPadding(new Insets(20));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);");
		
		HBox topRow = new HBox(20);
		topRow.setAlignment(Pos.CENTER_LEFT);
		
		Label orderIdLabel = new Label("Order #" + order.getIdOrder());
		orderIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label customerLabel = new Label("Customer: " + order.getIdCustomer());
		customerLabel.setFont(Font.font("Arial", 13));
		customerLabel.setTextFill(Color.web("#666666"));
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label statusLabel = new Label(order.getStatus());
		statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
		statusLabel.setPadding(new Insets(5, 12, 5, 12));
		statusLabel.setStyle("-fx-background-radius: 12; -fx-background-color: " + getStatusBackgroundColor(order.getStatus()) + ";");
		statusLabel.setTextFill(Color.WHITE);
		
		topRow.getChildren().addAll(orderIdLabel, customerLabel, spacer, statusLabel);
		
		HBox detailsRow = new HBox(30);
		detailsRow.setAlignment(Pos.CENTER_LEFT);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Label dateLabel = new Label(dateFormat.format(order.getOrderedAt()));
		dateLabel.setFont(Font.font("Arial", 12));
		
		Label amountLabel = new Label("Rp" + formatCurrency(order.getTotalAmount()));
		amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		amountLabel.setTextFill(Color.web("#2E7D32"));
		
		detailsRow.getChildren().addAll(dateLabel, amountLabel);
		
		Button viewDetailsButton = new Button("View Details");
		viewDetailsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF; -fx-font-size: 12px; -fx-padding: 6px 15px; -fx-cursor: handl");
		viewDetailsButton.setOnAction(e -> showOrderDetails(parentStage, order));
		
		card.getChildren().addAll(topRow, detailsRow, viewDetailsButton);
		return card;
	}
	
	private static String getStatusBackgroundColor(String status) {
		switch (status) {
			case "Pending":
				
				return "#FF9800";
				
			case "In Progress":
				
				return "#2196F3";
				
			case "Delivered":
				
				return "#4CAF50";
	
			default:
				return "#9E9E9E";
		}
	}
	
	private static String formatCurrency(double amount) {
		return NumberFormat.getNumberInstance(new Locale("id", "ID")).format(amount);
	}

}
