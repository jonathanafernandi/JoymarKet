package view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import controller.CourierHandler;
import controller.DeliveryHandler;
import controller.OrderHeaderHandler;
import controller.ProductHandler;
import controller.UserHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Admin;
import model.Courier;
import model.OrderHeader;
import model.Product;

public class AdminWindow {

	public static void showAdminDashboard(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5");
		
		HBox topBar = createTopBar(admin);
		root.setTop(topBar);
		
		VBox centerBox = new VBox(25);
		centerBox.setPadding(new Insets(40));
		centerBox.setAlignment(Pos.CENTER);
		
		Label welcomeLabel = new Label("Admin Dashboard");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		
		Label nameLabel = new Label(admin.getFullName());
		nameLabel.setFont(Font.font("Arial", 16));
		nameLabel.setTextFill(Color.web("#555555"));
		
		VBox menuBox = new VBox(20);
		menuBox.setAlignment(Pos.CENTER);
		
		Button viewOrdersButton = createMenuButton("View All Orders", "#2196F3");
		Button manageProductsButton = createMenuButton("Manage Products", "#4CAF50");
		Button assignCourierButton = createMenuButton("Assign Courier", "#FF9800");
		Button editProfileButton = createMenuButton("Edit Profile", "#607D8B");
		Button logoutButton = createMenuButton("Logout", "#F44336");
		
		viewOrdersButton.setOnAction(e -> showAllOrders(stage, admin));
		manageProductsButton.setOnAction(e -> showManageProducts(stage, admin));
		assignCourierButton.setOnAction(e -> showAssignCourier(stage, admin));
		editProfileButton.setOnAction(e -> UserWindow.showEditProfileWindow(stage, admin));
		logoutButton.setOnAction(e -> {
			UserHandler.logout();
			UserWindow.showLoginWindow(stage);
		});
		
		menuBox.getChildren().addAll(viewOrdersButton, manageProductsButton, assignCourierButton, editProfileButton, logoutButton);
		centerBox.getChildren().addAll(welcomeLabel, nameLabel, menuBox);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
	private static void showAllOrders(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		root.setTop(createTopBar(admin));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("All Orders");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<OrderHeader> orders = OrderHeaderHandler.getAllOrders();
		
		if (orders.isEmpty()) {
			Label emptyLabel = new Label("No orders found.");
			emptyLabel.setFont(Font.font("Arial", 16));
			emptyLabel.setTextFill(Color.GRAY);
			centerBox.getChildren().addAll(titleLabel, emptyLabel);
		} else {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			
			VBox orderList = new VBox(15);
			orderList.setPadding(new Insets(10));
			
			for (OrderHeader order : orders) {
				VBox orderCard = createOrderCardForAdmin(order);
				orderList.getChildren().add(orderCard);
			}
			
			scrollPane.setContent(orderList);
			centerBox.getChildren().addAll(titleLabel, scrollPane);
		}
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
		backButton.setOnAction(e -> showAdminDashboard(stage, admin));
		
		centerBox.getChildren().add(backButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
	private static void showManageProducts(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		root.setTop(createTopBar(admin));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("Manage Products");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<Product> products = ProductHandler.getAllProducts();
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		
		VBox productList = new VBox(15);
		productList.setPadding(new Insets(10));
		
		for (Product product : products) {
			VBox productCard = createProductCardForAdmin(product, stage, admin);
			productList.getChildren().add(productCard);
		}
		
		scrollPane.setContent(productList);
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
		backButton.setOnAction(e -> showAdminDashboard(stage, admin));
		
		centerBox.getChildren().addAll(titleLabel, scrollPane, backButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
	private static void showAssignCourier(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		root.setTop(createTopBar(admin));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("Assign Courier to Order");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<OrderHeader> orders = OrderHeaderHandler.getAllOrders();
		
		List<Courier> couriers = CourierHandler.getAllCouriers();
		
		VBox formBox = new VBox(15);
		formBox.setMaxWidth(600);
		formBox.setPadding(new Insets(25));
		formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		Label orderLabel = new Label("Select Order:");
		orderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		ComboBox<String> orderCombo = new ComboBox<>();
		for (OrderHeader order : orders) {
			orderCombo.getItems().add(order.getIdOrder() + " - " + order.getStatus() + " - Rp" + formatCurrency(order.getTotalAmount()));
		}
		orderCombo.setPromptText("Select an order");
		orderCombo.setPrefWidth(550);
		
		Label courierLabel = new Label("Select Courier:");
		courierLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		ComboBox<String> courierCombo = new ComboBox<>();
		for (Courier courier : couriers) {
			courierCombo.getItems().add(courier.getIdUser() + " - " + courier.getFullName() + " (" + courier.getVehicleType() + ")");
		}
		courierCombo.setPromptText("Select a courier");
		courierCombo.setPrefWidth(550);
		
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setVisible(false);
		
		Button assignButton = new Button("Assign");
		assignButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 30px; -fx-cursor: hand;");
		
		assignButton.setOnAction(e -> {
			String selectedOrder = orderCombo.getValue();
			String selectedCourier = courierCombo.getValue();
			
			if (selectedOrder == null || selectedCourier == null) {
				errorLabel.setText("Please select both order and courier.");
				errorLabel.setVisible(true);
				return;
			}
			
			String idOrder = selectedOrder.split(" - ")[0];
			String idCourier = selectedCourier.split(" - ")[0];
			
			String error = DeliveryHandler.assignCourierToOrder(idOrder, idCourier);
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Courier assigned successfully!");
				alert.showAndWait();
				showAdminDashboard(stage, admin);
			}
		});
		
		formBox.getChildren().addAll(orderLabel, orderCombo, courierLabel, courierCombo, errorLabel, assignButton);
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
		backButton.setOnAction(e -> showAdminDashboard(stage, admin));
		
		centerBox.getChildren().addAll(titleLabel, formBox, backButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
	private static HBox createTopBar(Admin admin) {
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #2196F3;");
		
		Label logoLabel = new Label("JoymarKet Admin");
		logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		logoLabel.setTextFill(Color.WHITE);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label userLabel = new Label(admin.getFullName());
		userLabel.setFont(Font.font("Arial", 14));
		userLabel.setTextFill(Color.WHITE);
		
		topBar.getChildren().addAll(logoLabel, spacer, userLabel);
		return topBar;
	}
	
	private static Button createMenuButton(String text, String color) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20px 40px; -fx-background-radius: 8; -fx-cursor: hand;");
		button.setPrefWidth(350);
		button.setPrefHeight(70);
		return button;
	}
	
	private static VBox createProductCardForAdmin(Product product, Stage stage, Admin admin) {
		VBox card = new VBox(12);
		card.setPadding(new Insets(15));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label priceLabel = new Label("Price: Rp" + formatCurrency(product.getPrice()));
		priceLabel.setFont(Font.font("Arial", 14));
		
		Label stockLabel = new Label("Stock: " + product.getStock());
		stockLabel.setFont(Font.font("Arial", 14));
		stockLabel.setTextFill(product.getStock() > 0 ? Color.GREEN : Color.RED);
		
		Label categoryLabel = new Label("Category: " + product.getCategory());
		categoryLabel.setFont(Font.font("Arial", 12));
		categoryLabel.setTextFill(Color.GRAY);
		
		HBox actionBox = new HBox(15);
		actionBox.setAlignment(Pos.CENTER_LEFT);
		
		TextField stockField = new TextField(String.valueOf(product.getStock()));
		stockField.setPrefWidth(100);
		stockField.setPromptText("New stock");
		
		Button updateButton = new Button("Update Stock");
		updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 8px 15px; -fx-cursor: hand;");
		
		updateButton.setOnAction(e -> {
			try {
				int newStock = Integer.parseInt(stockField.getText());
				if (newStock < 0) {
					showAlert(Alert.AlertType.ERROR, "Error", "Stock can not be negative.");
					return;
				}
				
				boolean success = product.editProductStock(newStock);
				if (success) {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Stock updated!");
					showManageProducts(stage, admin);
				} else {
					showAlert(Alert.AlertType.ERROR, "Error", "Failed to update stock.");
				}
			} catch (NumberFormatException e1) {
				showAlert(Alert.AlertType.ERROR, "Error", "Invalid stock value.");
			}
		});
		
		actionBox.getChildren().addAll(new Label("Update stock:"), stockField, updateButton);
		
		card.getChildren().addAll(nameLabel, priceLabel, stockLabel, categoryLabel, new Separator(), actionBox);
		return card;
	}
	
	private static VBox createOrderCardForAdmin(OrderHeader order) {
		VBox card = new VBox(10);
		card.setPadding(new Insets(15));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		Label idLabel = new Label("Order ID: " + order.getIdOrder());
		idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label customerLabel = new Label("Customer ID: " + order.getIdCustomer());
		customerLabel.setFont(Font.font("Arial", 12));
		
		Label dateLabel = new Label("Date: " + order.getOrderedAt().toString());
		dateLabel.setFont(Font.font("Arial", 12));
		
		Label amountLabel = new Label("Total: Rp" + formatCurrency(order.getTotalAmount()));
		amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		amountLabel.setTextFill(Color.web("#2E7D32"));
		
		Label statusLabel = new Label("Status: " + order.getStatus());
		statusLabel.setFont(Font.font("Arial", 12));
		statusLabel.setTextFill(getStatusColor(order.getStatus()));
		
		card.getChildren().addAll(idLabel, customerLabel, dateLabel, amountLabel, statusLabel);
		return card;
	}
	
	private static void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private static Color getStatusColor(String status) {
		switch (status) {
			case "Pending":
				
				return Color.ORANGE;
				
			case "In Progress":
				
				return Color.BLUE;
				
			case "Delivered":
				
				return Color.GREEN;
	
			default:
				return Color.GRAY;
		}
	}
	
	private static String formatCurrency(double amount) {
		return NumberFormat.getNumberInstance(new Locale("id", "ID")).format(amount);
	}

}
