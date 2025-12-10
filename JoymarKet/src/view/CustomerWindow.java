package view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import controller.CartItemHandler;
import controller.CustomerHandler;
import controller.OrderHeaderHandler;
import controller.ProductHandler;
import controller.UserHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.CartItem;
import model.Customer;
import model.OrderHeader;
import model.Product;

public class CustomerWindow {

	public static void showCustomerDashboard(Stage stage, Customer customer) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		HBox topBar = createTopBar(stage, customer);
		root.setTop(topBar);
		
		VBox centerBox = new VBox(25);
		centerBox.setPadding(new Insets(40));
		centerBox.setAlignment(Pos.CENTER);
		
		Label welcomeLabel = new Label("Welcome, " + customer.getFullName() + "!");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		
		Label balanceLabel = new Label("Balance: Rp" + formatCurrency(customer.getBalance()));
		balanceLabel.setFont(Font.font("Arial", 16));
		balanceLabel.setTextFill(Color.web("#555555"));
		
		VBox menuBox = new VBox(20);
		menuBox.setAlignment(Pos.CENTER);
		
		Button browseProductsButton = createMenuButton("Browse Products", "#4CAF50");
		Button viewCartButton = createMenuButton("View Cart", "#2196F3");
		Button myOrdersButton = createMenuButton("My Orders", "#FF9800");
		Button topUpButton = createMenuButton("Top Up Balance", "#9C27B0");
		Button editProfileButton = createMenuButton("Edit Profile", "#607D8B");
		Button logoutButton = createMenuButton("Logout", "#F44336");
		
		browseProductsButton.setOnAction(e -> showBrowseProducts(stage, customer));
		viewCartButton.setOnAction(e -> showCart(stage, customer));
		myOrdersButton.setOnAction(e -> showOrders(stage, customer));
		topUpButton.setOnAction(e -> showTopUpDialog(stage, customer));
		editProfileButton.setOnAction(e -> UserWindow.showEditProfileWindow(stage, customer));
		logoutButton.setOnAction(e -> {
			UserHandler.logout();
			UserWindow.showLoginWindow(stage);
		});
		
		menuBox.getChildren().addAll(browseProductsButton, viewCartButton, myOrdersButton, topUpButton, editProfileButton, logoutButton);
		
		centerBox.getChildren().addAll(welcomeLabel, balanceLabel, menuBox);
		
		ScrollPane scrollPane = new ScrollPane(centerBox);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: #F5F5F5; -fx-background: #F5F5F5;");
		
		root.setCenter(scrollPane);
		
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
	private static void showBrowseProducts(Stage stage, Customer customer) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		root.setTop(createTopBar(stage, customer));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("Browse Products");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<Product> products = ProductHandler.getAvailableProducts();
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: transparent;");
		
		VBox productList = new VBox(15);
		productList.setPadding(new Insets(10));
		
		for (Product product : products) {
			HBox productCard = createProductCard(product, stage, customer);
			productList.getChildren().add(productCard);
		}
		
		scrollPane.setContent(productList);
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
		backButton.setOnAction(e -> showCustomerDashboard(stage, customer));
		
		centerBox.getChildren().addAll(titleLabel, scrollPane, backButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);;
	}
	
	private static void showCart(Stage stage, Customer customer) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		root.setTop(createTopBar(stage, customer));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("My Shopping Cart");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<CartItem> cartItems = CartItemHandler.getCartItems(customer.getIdUser());
		
		if (cartItems.isEmpty()) {
			Label emptyLabel = new Label("Your cart is empty. Start shopping!");
			emptyLabel.setFont(Font.font("Arial", 16));
			emptyLabel.setTextFill(Color.GRAY);
			
			Button browseButton = new Button("Browse Products");
			browseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
			browseButton.setOnAction(e -> showBrowseProducts(stage, customer));
			
			centerBox.getChildren().addAll(titleLabel, emptyLabel, browseButton);
		} else {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			
			VBox cartList = new VBox(15);
			cartList.setPadding(new Insets(10));
			
			double subtotal = 0;
			
			for (CartItem cartItem : cartItems) {
				Product product = ProductHandler.getProduct(cartItem.getIdProduct());
				if (product != null) {
					HBox cartCard = createCartItemCard(cartItem, product, stage, customer);
					cartList.getChildren().add(cartCard);
					subtotal += product.getPrice() * cartItem.getCount();
				}
			}
			
			scrollPane.setContent(cartList);
			
			VBox checkoutBox = new VBox(15);
			checkoutBox.setPadding(new Insets(20));
			checkoutBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
			
			Label subtotalLabel = new Label("Subtotal: Rp" + formatCurrency(subtotal));
			subtotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			
			TextField promoField = new TextField();
			promoField.setPromptText("Promo Code (optional)");
			promoField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
			
			HBox buttonBox = new HBox(15);
			buttonBox.setAlignment(Pos.CENTER);
			
			Button checkoutButton = new Button("Checkout");
			checkoutButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
			
			Button backButton = new Button("Back");
			backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
			
			checkoutButton.setOnAction(e -> {
				String error = CustomerHandler.checkout(customer.getIdUser(), promoField.getText().trim());
				if (error != null) {
					showAlert(Alert.AlertType.ERROR, "Checkout Failed", error);
				} else {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Order placed successfully.");
					Customer updatedCustomer = Customer.getCustomer(customer.getIdUser());
					showCustomerDashboard(stage, updatedCustomer);
				}
			});
			
			backButton.setOnAction(e -> showCustomerDashboard(stage, customer));
			
			buttonBox.getChildren().addAll(checkoutButton, backButton);
			checkoutBox.getChildren().addAll(subtotalLabel, promoField, buttonBox);
			
			centerBox.getChildren().addAll(titleLabel, scrollPane, checkoutBox);
		}
		
		root.setCenter(centerBox);
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
	private static void showOrders(Stage stage, Customer customer) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5");
		
		root.setTop(createTopBar(stage, customer));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("My Orders");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<OrderHeader> orders = OrderHeaderHandler.getCustomerOrders(customer.getIdUser());
		
		if (orders.isEmpty()) {
			Label emptyLabel = new Label("You have not placed any orders yet.");
			emptyLabel.setFont(Font.font("Arial", 16));
			emptyLabel.setTextFill(Color.GRAY);
			centerBox.getChildren().addAll(titleLabel, emptyLabel);
		} else {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			
			VBox orderList = new VBox(15);
			orderList.setPadding(new Insets(10));
			
			for (OrderHeader order : orders) {
				VBox orderCard = createOrderCard(order);
				orderList.getChildren().add(orderCard);
			}
			
			scrollPane.setContent(orderList);
			centerBox.getChildren().addAll(titleLabel, scrollPane);;
		}
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
		backButton.setOnAction(e -> showCustomerDashboard(stage, customer));
		
		centerBox.getChildren().add(backButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
	private static void showTopUpDialog(Stage stage, Customer customer) {
		Dialog<Double> dialog = new Dialog<>();
		dialog.setTitle("Top Up Balance");
		dialog.setHeaderText("Add funds to your account");
		
		ButtonType topUpButtonType = new ButtonType("Top Up", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(topUpButtonType, ButtonType.CANCEL);
		
		VBox content = new VBox(15);
		content.setPadding(new Insets(20));
		
		Label currentBalanceLabel = new Label("Current Balance: Rp" + formatCurrency(customer.getBalance()));
		currentBalanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		Label infoLabel = new Label("Minimum top-up: Rp10.000");
		infoLabel.setFont(Font.font("Arial", 12));
		infoLabel.setTextFill(Color.web("#666666"));
		
		TextField amountField = new TextField();
		amountField.setPromptText("Enter amount (e.g., 100000)");
		amountField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
		
		amountField.setTextFormatter(new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
	        if (newText.matches("\\d*")) {  
	        	return change;
	        }
        	return null;
		}));
		
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		errorLabel.setWrapText(true);
		
		content.getChildren().addAll(currentBalanceLabel, infoLabel, new Label("Amount:"), amountField, errorLabel);
		
		dialog.getDialogPane().setContent(content);
		
		Button topUpButton = (Button) dialog.getDialogPane().lookupButton(topUpButtonType);
		topUpButton.setDisable(true);
		
		amountField.textProperty().addListener((observable, oldValue, newValue) -> {
			topUpButton.setDisable(newValue.trim().isEmpty());
			errorLabel.setVisible(false);
		});
		
		topUpButton.addEventFilter(ActionEvent.ACTION, event -> {
			String text = amountField.getText().trim();
			
			if (text.isEmpty()) {
				errorLabel.setText("Amount can not be empty.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
			
			double amount;
			try {
				amount = Double.parseDouble(text);
			} catch (NumberFormatException e) {
				errorLabel.setText("Please enter a valid number.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
			
			if (amount < 10000) {
				errorLabel.setText("Minimum top-up is Rp10.000.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
			
			if (amount <= 0) {
				errorLabel.setText("Amount must be greater than Rp0.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
			
			String error = CustomerHandler.topUpBalance(customer.getIdUser(), amount);
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
				event.consume();
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Successfully topped up Rp" + formatCurrency(amount) + "!");
				Customer updatedCustomer = Customer.getCustomer(customer.getIdUser());
				dialog.close();
				showCustomerDashboard(stage, updatedCustomer);
			}
		});
		
		dialog.showAndWait();
	}
	
	private static HBox createTopBar(Stage stage, Customer customer) {
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #667EEA;");
		
		Label logoLabel = new Label("JoymarKet");
		logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		logoLabel.setTextFill(Color.WHITE);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label userLabel = new Label(customer.getFullName());
		userLabel.setFont(Font.font("Arial", 14));
		userLabel.setTextFill(Color.WHITE);
		
		topBar.getChildren().addAll(logoLabel, spacer, userLabel);
		return topBar;
	}
	
	private static Button createMenuButton(String text, String color) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20px 40px; -fx-background-radius: 8; -fx-cursor: hand;");
		button.setPrefWidth(250);
		button.setPrefHeight(80);
		return button;
	}
	
	private static HBox createProductCard(Product product, Stage stage, Customer customer) {
		HBox card = new HBox(20);
		card.setPadding(new Insets(15));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		VBox infoBox = new VBox(8);
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label priceLabel = new Label("Rp" + formatCurrency(product.getPrice()));
		priceLabel.setFont(Font.font("Arial", 14));
		priceLabel.setTextFill(Color.web("#2E7D32"));
		
		Label stockLabel = new Label("Stock: " + product.getStock());
		stockLabel.setFont(Font.font("Arial", 12));
		stockLabel.setTextFill(Color.GRAY);
		
		Label categoryLabel = new Label("Category: " + product.getCategory());
		categoryLabel.setFont(Font.font("Arial", 12));
		categoryLabel.setTextFill(Color.GRAY);
		
		infoBox.getChildren().addAll(nameLabel, priceLabel, stockLabel, categoryLabel);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Spinner<Integer> quantitySpinner = new Spinner<>(1, product.getStock(), 1);
		quantitySpinner.setPrefWidth(80);
		
		Button addToCartButton = new Button("Add to Cart");
		addToCartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 8px 15px; -fx-cursor: hand;");
		
		addToCartButton.setOnAction(e -> {
			int quantity = quantitySpinner.getValue();
			String error = CustomerHandler.addToCart(customer.getIdUser(), product.getIdProduct(), quantity);
			if (error != null) {
				showAlert(Alert.AlertType.ERROR, "Error", error);
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Added to cart!");
			}
		});
		
		card.getChildren().addAll(infoBox, spacer, quantitySpinner, addToCartButton);
		return card;
	}
	
	private static HBox createCartItemCard(CartItem cartItem, Product product, Stage stage, Customer customer) {
		HBox card = new HBox(20);
		card.setPadding(new Insets(15));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		VBox infoBox = new VBox(8);
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label priceLabel = new Label("Rp" + formatCurrency(product.getPrice()) + " x " + cartItem.getCount());
		priceLabel.setFont(Font.font("Arial", 14));
		
		Label totalLabel = new Label("Total: Rp" + formatCurrency(product.getPrice() * cartItem.getCount()));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		totalLabel.setTextFill(Color.web("#2E7D32"));
		
		infoBox.getChildren().addAll(nameLabel, priceLabel, totalLabel);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Button removeButton = new Button("Remove");
		removeButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 8px 15px; -fx-cursor: hand;");
		
		removeButton.setOnAction(e -> {
			String error = CustomerHandler.removeFromCart(customer.getIdUser(), product.getIdProduct());
			if (error != null) {
				showAlert(Alert.AlertType.ERROR, "Error", error);
			} else {
				showCart(stage, customer);
			}
		});
		
		card.getChildren().addAll(infoBox, spacer, removeButton);
		return card;
	}
	
	private static VBox createOrderCard(OrderHeader order) {
		VBox card = new VBox(10);
		card.setPadding(new Insets(15));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		Label idLabel = new Label("Order ID: " + order.getIdOrder());
		idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label dateLabel = new Label("Date: " + order.getOrderedAt().toString());
		dateLabel.setFont(Font.font("Arial", 12));
		
		Label amountLabel = new Label("Total: Rp" + formatCurrency(order.getTotalAmount()));
		amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		amountLabel.setTextFill(Color.web("#2E7D32"));
		
		Label statusLabel = new Label("Status: " + order.getStatus());
		statusLabel.setFont(Font.font("Arial", 12));
		statusLabel.setTextFill(Color.web("#2E7D32"));
		
		card.getChildren().addAll(idLabel, dateLabel, amountLabel, statusLabel);
		return card;
	}
	
	private static void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private static String formatCurrency(double amount) {
		return NumberFormat.getNumberInstance(new Locale("id", "ID")).format(amount);
	}

}
