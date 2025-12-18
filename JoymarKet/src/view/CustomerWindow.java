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

// Entry point for customer dashboard UI
	public static void showCustomerDashboard(Stage stage, Customer customer) {

			// Root layout using BorderPane (top, center, bottom layout)
			BorderPane root = new BorderPane();
			root.setStyle("-fx-background-color: #F5F5F5;");
	
			// Top navigation bar (logo + user info)
			HBox topBar = createTopBar(stage, customer);
			root.setTop(topBar);
	
			// Main content container in the center
			VBox centerBox = new VBox(25);
			centerBox.setPadding(new Insets(40));
			centerBox.setAlignment(Pos.CENTER);
	
			// Welcome message using customer's name
			Label welcomeLabel = new Label("Welcome, " + customer.getFullName() + "!");
			welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
	
			// Display current customer balance
			Label balanceLabel = new Label("Balance: Rp" + formatCurrency(customer.getBalance()));
			balanceLabel.setFont(Font.font("Arial", 16));
			balanceLabel.setTextFill(Color.web("#555555"));
	
			// Menu container for dashboard actions
			VBox menuBox = new VBox(20);
			menuBox.setAlignment(Pos.CENTER);
	
			// Dashboard menu buttons
			Button browseProductsButton = createMenuButton("Browse Products", "#4CAF50");
			Button viewCartButton = createMenuButton("View Cart", "#2196F3");
			Button myOrdersButton = createMenuButton("My Orders", "#FF9800");
			Button topUpButton = createMenuButton("Top Up Balance", "#9C27B0");
			Button editProfileButton = createMenuButton("Edit Profile", "#607D8B");
			Button logoutButton = createMenuButton("Logout", "#F44336");
	
			// Navigation logic for each menu option
			browseProductsButton.setOnAction(e -> showBrowseProducts(stage, customer));
			viewCartButton.setOnAction(e -> showCart(stage, customer));
			myOrdersButton.setOnAction(e -> showOrders(stage, customer));
			topUpButton.setOnAction(e -> showTopUpDialog(stage, customer));
			editProfileButton.setOnAction(e -> UserWindow.showEditProfileWindow(stage, customer));
	
			// Logout clears session and returns to login screen
			logoutButton.setOnAction(e -> {
				UserHandler.logout();
				UserWindow.showLoginWindow(stage);
			});
	
			// Add all menu buttons to menu container
			menuBox.getChildren().addAll(
				browseProductsButton,
				viewCartButton,
				myOrdersButton,
				topUpButton,
				editProfileButton,
				logoutButton
			);
	
			// Add main components to center layout
			centerBox.getChildren().addAll(welcomeLabel, balanceLabel, menuBox);
	
			// Wrap center content in ScrollPane for smaller screens
			ScrollPane scrollPane = new ScrollPane(centerBox);
			scrollPane.setFitToWidth(true);
			scrollPane.setStyle("-fx-background-color: #F5F5F5; -fx-background: #F5F5F5;");
	
			// Set center content
			root.setCenter(scrollPane);
	
			// Final scene setup
			Scene scene = new Scene(root, 900, 650);
			stage.setScene(scene);
		}
		
		// Displays the product browsing page for customers
	private static void showBrowseProducts(Stage stage, Customer customer) {
	
		// Root layout for this page
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
	
		// Reuse top bar (logo + user info)
		root.setTop(createTopBar(stage, customer));
	
		// Main container for page content
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
	
		// Page title
		Label titleLabel = new Label("Browse Products");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
	
		// Fetch all available products from controller
		List<Product> products = ProductHandler.getAvailableProducts();
	
		// Scroll container so product list can scroll vertically
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: transparent;");
	
		// Container holding all product cards
		VBox productList = new VBox(15);
		productList.setPadding(new Insets(10));
	
		// Create a UI card for each product
		for (Product product : products) {
			HBox productCard = createProductCard(product, stage, customer);
			productList.getChildren().add(productCard);
		}
	
		// Set product list as scrollable content
		scrollPane.setContent(productList);
	
		// Button to return to customer dashboard
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle(
			"-fx-background-color: #607D8B; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 14px; " +
			"-fx-padding: 10px 20px; " +
			"-fx-cursor: hand;"
		);
	
		// Navigate back to dashboard when clicked
		backButton.setOnAction(e -> showCustomerDashboard(stage, customer));
	
		// Add all components to center layout
		centerBox.getChildren().addAll(titleLabel, scrollPane, backButton);
		root.setCenter(centerBox);
	
		// Set scene for this page
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
		// Displays the shopping cart page for the customer
	private static void showCart(Stage stage, Customer customer) {
	
		// Root layout for cart page
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
	
		// Top bar with logo and user info
		root.setTop(createTopBar(stage, customer));
	
		// Main content container
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
	
		// Page title
		Label titleLabel = new Label("My Shopping Cart");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
	
		// Retrieve all cart items for the current customer
		List<CartItem> cartItems = CartItemHandler.getCartItems(customer.getIdUser());
	
		// Case when cart is empty
		if (cartItems.isEmpty()) {
	
			// Message shown when cart has no items
			Label emptyLabel = new Label("Your cart is empty. Start shopping!");
			emptyLabel.setFont(Font.font("Arial", 16));
			emptyLabel.setTextFill(Color.GRAY);
	
			// Button to redirect customer to product browsing
			Button browseButton = new Button("Browse Products");
			browseButton.setStyle(
				"-fx-background-color: #4CAF50; " +
				"-fx-text-fill: #FFFFFF; " +
				"-fx-font-size: 14px; " +
				"-fx-padding: 10px 20px; " +
				"-fx-cursor: hand;"
			);
	
			// Navigate to browse products page
			browseButton.setOnAction(e -> showBrowseProducts(stage, customer));
	
			// Add components to layout
			centerBox.getChildren().addAll(titleLabel, emptyLabel, browseButton);
	
		} else {
	
			// Scroll container for cart items
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
	
			// Container holding individual cart item cards
			VBox cartList = new VBox(15);
			cartList.setPadding(new Insets(10));
	
			// Variable to calculate total cart price
			double subtotal = 0;
	
			// Loop through cart items and build UI cards
			for (CartItem cartItem : cartItems) {
				Product product = ProductHandler.getProduct(cartItem.getIdProduct());
				if (product != null) {
					HBox cartCard = createCartItemCard(cartItem, product, stage, customer);
					cartList.getChildren().add(cartCard);
	
					// Add item total to subtotal
					subtotal += product.getPrice() * cartItem.getCount();
				}
			}
	
			// Attach cart list to scroll pane
			scrollPane.setContent(cartList);
	
			// Checkout section container
			VBox checkoutBox = new VBox(15);
			checkoutBox.setPadding(new Insets(20));
			checkoutBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
	
			// Display subtotal price
			Label subtotalLabel = new Label("Subtotal: Rp" + formatCurrency(subtotal));
			subtotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	
			// Optional promo code input
			TextField promoField = new TextField();
			promoField.setPromptText("Promo Code (optional)");
			promoField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
	
			// Container for action buttons
			HBox buttonBox = new HBox(15);
			buttonBox.setAlignment(Pos.CENTER);
	
			// Checkout button
			Button checkoutButton = new Button("Checkout");
			checkoutButton.setStyle(
				"-fx-background-color: #4CAF50; " +
				"-fx-text-fill: #FFFFFF; " +
				"-fx-font-size: 14px; " +
				"-fx-padding: 10px 20px; " +
				"-fx-cursor: hand;"
			);
	
			// Back button
			Button backButton = new Button("Back");
			backButton.setStyle(
				"-fx-background-color: #607D8B; " +
				"-fx-text-fill: #FFFFFF; " +
				"-fx-font-size: 14px; " +
				"-fx-padding: 10px 20px; " +
				"-fx-cursor: hand;"
			);
	
			// Handle checkout process
			checkoutButton.setOnAction(e -> {
				String error = CustomerHandler.checkout(
					customer.getIdUser(),
					promoField.getText().trim()
				);
	
				if (error != null) {
					showAlert(Alert.AlertType.ERROR, "Checkout Failed", error);
				} else {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Order placed successfully.");
					Customer updatedCustomer = Customer.getCustomer(customer.getIdUser());
					showCustomerDashboard(stage, updatedCustomer);
				}
			});
	
			// Navigate back to dashboard
			backButton.setOnAction(e -> showCustomerDashboard(stage, customer));
	
			// Add buttons to button container
			buttonBox.getChildren().addAll(checkoutButton, backButton);
	
			// Add all checkout components
			checkoutBox.getChildren().addAll(subtotalLabel, promoField, buttonBox);
	
			// Add everything to main layout
			centerBox.getChildren().addAll(titleLabel, scrollPane, checkoutBox);
		}
	
		// Set center layout and display scene
		root.setCenter(centerBox);
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
		
	// Displays order history for the customer
	private static void showOrders(Stage stage, Customer customer) {
	
		// Root layout for orders page
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5");
	
		// Top navigation bar
		root.setTop(createTopBar(stage, customer));
	
		// Main content container
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
	
		// Page title
		Label titleLabel = new Label("My Orders");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
	
		// Fetch all orders made by the customer
		List<OrderHeader> orders =
			OrderHeaderHandler.getCustomerOrders(customer.getIdUser());
	
		// If customer has no orders
		if (orders.isEmpty()) {
	
			Label emptyLabel = new Label("You have not placed any orders yet.");
			emptyLabel.setFont(Font.font("Arial", 16));
			emptyLabel.setTextFill(Color.GRAY);
	
			centerBox.getChildren().addAll(titleLabel, emptyLabel);
	
		} else {
	
			// Scrollable container for orders
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
	
			// Container holding order cards
			VBox orderList = new VBox(15);
			orderList.setPadding(new Insets(10));
	
			// Create card for each order
			for (OrderHeader order : orders) {
				VBox orderCard = createOrderCard(order);
				orderList.getChildren().add(orderCard);
			}
	
			// Attach order list to scroll pane
			scrollPane.setContent(orderList);
			centerBox.getChildren().addAll(titleLabel, scrollPane);
		}
	
		// Button to return to dashboard
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle(
			"-fx-background-color: #607D8B; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 14px; " +
			"-fx-padding: 10px 20px; " +
			"-fx-cursor: hand;"
		);
	
		backButton.setOnAction(e -> showCustomerDashboard(stage, customer));
	
		centerBox.getChildren().add(backButton);
		root.setCenter(centerBox);
	
		// Display scene
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
	}
	
		
		// Shows a dialog window for topping up customer balance
	private static void showTopUpDialog(Stage stage, Customer customer) {
	
		// Create dialog for top-up process
		Dialog<Double> dialog = new Dialog<>();
		dialog.setTitle("Top Up Balance");
		dialog.setHeaderText("Add funds to your account");
	
		// Define Top Up button type
		ButtonType topUpButtonType =
			new ButtonType("Top Up", ButtonBar.ButtonData.OK_DONE);
	
		// Add Top Up and Cancel buttons to dialog
		dialog.getDialogPane().getButtonTypes()
			.addAll(topUpButtonType, ButtonType.CANCEL);
	
		// Main container for dialog content
		VBox content = new VBox(15);
		content.setPadding(new Insets(20));
	
		// Display customer's current balance
		Label currentBalanceLabel =
			new Label("Current Balance: Rp" + formatCurrency(customer.getBalance()));
		currentBalanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	
		// Informational label for minimum top-up amount
		Label infoLabel = new Label("Minimum top-up: Rp10.000");
		infoLabel.setFont(Font.font("Arial", 12));
		infoLabel.setTextFill(Color.web("#666666"));
	
		// Input field for top-up amount
		TextField amountField = new TextField();
		amountField.setPromptText("Enter amount (e.g., 100000)");
		amountField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
	
		// Restrict input to numeric values only
		amountField.setTextFormatter(new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		}));
	
		// Label to display validation or error messages
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		errorLabel.setWrapText(true);
	
		// Add all components to dialog layout
		content.getChildren().addAll(
			currentBalanceLabel,
			infoLabel,
			new Label("Amount:"),
			amountField,
			errorLabel
		);
	
		// Set dialog content
		dialog.getDialogPane().setContent(content);
	
		// Get reference to Top Up button
		Button topUpButton =
			(Button) dialog.getDialogPane().lookupButton(topUpButtonType);
	
		// Disable button initially until input is provided
		topUpButton.setDisable(true);
	
		// Enable Top Up button when input field is not empty
		amountField.textProperty().addListener((observable, oldValue, newValue) -> {
			topUpButton.setDisable(newValue.trim().isEmpty());
			errorLabel.setVisible(false);
		});
	
		// Handle Top Up button action with validation
		topUpButton.addEventFilter(ActionEvent.ACTION, event -> {
	
			String text = amountField.getText().trim();
	
			// Validate empty input
			if (text.isEmpty()) {
				errorLabel.setText("Amount can not be empty.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
	
			double amount;
	
			// Validate numeric input
			try {
				amount = Double.parseDouble(text);
			} catch (NumberFormatException e) {
				errorLabel.setText("Please enter a valid number.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
	
			// Validate minimum top-up amount
			if (amount < 10000) {
				errorLabel.setText("Minimum top-up is Rp10.000.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
	
			// Validate positive amount
			if (amount <= 0) {
				errorLabel.setText("Amount must be greater than Rp0.");
				errorLabel.setVisible(true);
				event.consume();
				return;
			}
	
			// Perform top-up operation
			String error =
				CustomerHandler.topUpBalance(customer.getIdUser(), amount);
	
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
				event.consume();
			} else {
				showAlert(
					Alert.AlertType.INFORMATION,
					"Success",
					"Successfully topped up Rp" + formatCurrency(amount) + "!"
				);
	
				// Refresh customer data and return to dashboard
				Customer updatedCustomer =
					Customer.getCustomer(customer.getIdUser());
				dialog.close();
				showCustomerDashboard(stage, updatedCustomer);
			}
		});
	
		// Display dialog and wait for user interaction
		dialog.showAndWait();
	}
	
		
	// Creates the top navigation bar for customer pages
	private static HBox createTopBar(Stage stage, Customer customer) {
	
		// Horizontal container for top bar
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #667EEA;");
	
		// Application logo/title
		Label logoLabel = new Label("JoymarKet");
		logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		logoLabel.setTextFill(Color.WHITE);
	
		// Spacer to push user name to the right
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
	
		// Display logged-in customer's name
		Label userLabel = new Label(customer.getFullName());
		userLabel.setFont(Font.font("Arial", 14));
		userLabel.setTextFill(Color.WHITE);
	
		// Add components to top bar
		topBar.getChildren().addAll(logoLabel, spacer, userLabel);
	
		return topBar;
	}
	
		
		// Creates a styled menu button used in the customer dashboard
	private static Button createMenuButton(String text, String color) {
	
		// Initialize button with display text
		Button button = new Button(text);
	
		// Apply consistent styling for menu buttons
		button.setStyle(
			"-fx-background-color: " + color + ";" +
			"-fx-text-fill: #FFFFFF;" +
			"-fx-font-size: 16px;" +
			"-fx-font-weight: bold;" +
			"-fx-padding: 20px 40px;" +
			"-fx-background-radius: 8;" +
			"-fx-cursor: hand;"
		);
	
		// Set fixed size for uniform appearance
		button.setPrefWidth(250);
		button.setPrefHeight(80);
	
		return button;
	}
	
		
		// Creates a product card displayed in the Browse Products page
	private static HBox createProductCard(Product product, Stage stage, Customer customer) {
	
		// Main horizontal container for product card
		HBox card = new HBox(20);
		card.setPadding(new Insets(15));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
	
		// Container for product information
		VBox infoBox = new VBox(8);
	
		// Product name
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	
		// Product price
		Label priceLabel = new Label("Rp" + formatCurrency(product.getPrice()));
		priceLabel.setFont(Font.font("Arial", 14));
		priceLabel.setTextFill(Color.web("#2E7D32"));
	
		// Product stock
		Label stockLabel = new Label("Stock: " + product.getStock());
		stockLabel.setFont(Font.font("Arial", 12));
		stockLabel.setTextFill(Color.GRAY);
	
		// Product category
		Label categoryLabel = new Label("Category: " + product.getCategory());
		categoryLabel.setFont(Font.font("Arial", 12));
		categoryLabel.setTextFill(Color.GRAY);
	
		// Add product details to info box
		infoBox.getChildren().addAll(
			nameLabel,
			priceLabel,
			stockLabel,
			categoryLabel
		);
	
		// Spacer to push controls to the right
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
	
		// Spinner for selecting purchase quantity
		Spinner<Integer> quantitySpinner =
			new Spinner<>(1, product.getStock(), 1);
		quantitySpinner.setPrefWidth(80);
	
		// Button to add product to cart
		Button addToCartButton = new Button("Add to Cart");
		addToCartButton.setStyle(
			"-fx-background-color: #4CAF50;" +
			"-fx-text-fill: #FFFFFF;" +
			"-fx-font-size: 13px;" +
			"-fx-padding: 8px 15px;" +
			"-fx-cursor: hand;"
		);
	
		// Handle add-to-cart action
		addToCartButton.setOnAction(e -> {
			int quantity = quantitySpinner.getValue();
	
			String error =
				CustomerHandler.addToCart(
					customer.getIdUser(),
					product.getIdProduct(),
					quantity
				);
	
			if (error != null) {
				showAlert(Alert.AlertType.ERROR, "Error", error);
			} else {
				showAlert(
					Alert.AlertType.INFORMATION,
					"Success",
					"Added to cart!"
				);
			}
		});
	
		// Assemble card layout
		card.getChildren().addAll(
			infoBox,
			spacer,
			quantitySpinner,
			addToCartButton
		);
	
		return card;
	}
	
		
	// Creates a cart item card displayed in the shopping cart
	private static HBox createCartItemCard(
		CartItem cartItem,
		Product product,
		Stage stage,
		Customer customer
	) {
	
		// Main horizontal container for cart item
		HBox card = new HBox(20);
		card.setPadding(new Insets(15));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
	
		// Container for item details
		VBox infoBox = new VBox(8);
	
		// Product name
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	
		// Price and quantity
		Label priceLabel =
			new Label("Rp" + formatCurrency(product.getPrice()) +
			" x " + cartItem.getCount());
		priceLabel.setFont(Font.font("Arial", 14));
	
		// Total price for this item
		Label totalLabel =
			new Label("Total: Rp" +
			formatCurrency(product.getPrice() * cartItem.getCount()));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		totalLabel.setTextFill(Color.web("#2E7D32"));
	
		// Add item details
		infoBox.getChildren().addAll(
			nameLabel,
			priceLabel,
			totalLabel
		);
	
		// Spacer to align remove button
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
	
		// Remove button
		Button removeButton = new Button("Remove");
		removeButton.setStyle(
			"-fx-background-color: #F44336;" +
			"-fx-text-fill: #FFFFFF;" +
			"-fx-font-size: 13px;" +
			"-fx-padding: 8px 15px;" +
			"-fx-cursor: hand;"
		);
	
		// Handle remove-from-cart action
		removeButton.setOnAction(e -> {
			String error =
				CustomerHandler.removeFromCart(
					customer.getIdUser(),
					product.getIdProduct()
				);
	
			if (error != null) {
				showAlert(Alert.AlertType.ERROR, "Error", error);
			} else {
				showCart(stage, customer);
			}
		});
	
		// Assemble cart item layout
		card.getChildren().addAll(infoBox, spacer, removeButton);
	
		return card;
	}
	
		
	// Creates an order card for displaying order history
	private static VBox createOrderCard(OrderHeader order) {
	
		// Vertical container for order details
		VBox card = new VBox(10);
		card.setPadding(new Insets(15));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
	
		// Order ID
		Label idLabel =
			new Label("Order ID: " + order.getIdOrder());
		idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	
		// Order date
		Label dateLabel =
			new Label("Date: " + order.getOrderedAt().toString());
		dateLabel.setFont(Font.font("Arial", 12));
	
		// Total order amount
		Label amountLabel =
			new Label("Total: Rp" +
			formatCurrency(order.getTotalAmount()));
		amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		amountLabel.setTextFill(Color.web("#2E7D32"));
	
		// Order status
		Label statusLabel =
			new Label("Status: " + order.getStatus());
		statusLabel.setFont(Font.font("Arial", 12));
		statusLabel.setTextFill(Color.web("#2E7D32"));
	
		// Add all order details
		card.getChildren().addAll(
			idLabel,
			dateLabel,
			amountLabel,
			statusLabel
		);
	
		return card;
	}
	
		
	// Displays an alert dialog for feedback messages
	private static void showAlert(
		Alert.AlertType type,
		String title,
		String message
	) {
	
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
		
		// Formats a number into Indonesian currency format
	private static String formatCurrency(double amount) {
		return NumberFormat
			.getNumberInstance(new Locale("id", "ID"))
			.format(amount);
	}


}
