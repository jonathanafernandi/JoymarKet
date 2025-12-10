package view;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import controller.DeliveryHandler;
import controller.OrderHeaderHandler;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import model.Courier;
import model.Delivery;
import model.OrderHeader;

public class DeliveryWindow {

	public static void showCourierDeliveries(Stage parentStage, Courier courier) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("My Deliveries - JoymarKet Courier");
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #FF9800;");
		
		Label titleLabel = new Label("My Deliveries");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		titleLabel.setTextFill(Color.WHITE);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		VBox courierInfo = new VBox(3);
		Label nameLabel = new Label(courier.getFullName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		nameLabel.setTextFill(Color.WHITE);
		
		Label vehicleLabel = new Label(courier.getVehicleType() + " - " + courier.getVehiclePlate());
		vehicleLabel.setFont(Font.font("Arial", 11));
		vehicleLabel.setTextFill(Color.web("#FFE0B2"));
		
		courierInfo.getChildren().addAll(nameLabel, vehicleLabel);
		
		topBar.getChildren().addAll(titleLabel, spacer, courierInfo);
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
		refreshButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 8px 15px; -fx-cursor: hand;");
		
		filterBox.getChildren().addAll(filterLabel, statusCombo, refreshButton);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefHeight(450);
		
		VBox deliveryList = new VBox(15);
		deliveryList.setPadding(new Insets(10));
		
		Runnable loadDeliveries = () -> {
			deliveryList.getChildren().clear();
			List<Delivery> deliveries = DeliveryHandler.getCourierDeliveries(courier.getIdUser());
			
			String selectedStatus = statusCombo.getValue();
			int count = 0;
			
			for (Delivery delivery : deliveries) {
				if (selectedStatus.equals("All") || delivery.getStatus().equals(selectedStatus)) {
					VBox deliveryCard = createDeliveryCard(delivery, courier, stage);
					deliveryList.getChildren().add(deliveryCard);
					count++;
				}
			}
			
			if (count == 0) {
				Label emptyLabel = new Label(selectedStatus.equals("All") ? "No deliveries assigned yet." : "No deliveries with status: " + selectedStatus);
				emptyLabel.setFont(Font.font("Arial", 14));
				emptyLabel.setTextFill(Color.GRAY);
				deliveryList.getChildren().add(emptyLabel);
			}
		};
		
		loadDeliveries.run();
		
		statusCombo.setOnAction(e -> loadDeliveries.run());
		refreshButton.setOnAction(e -> loadDeliveries.run());
		
		scrollPane.setContent(deliveryList);
		
		Button closeButton = new Button("Close");
		closeButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-cursor: hand;");
		closeButton.setOnAction(e -> stage.close());
		
		centerBox.getChildren().addAll(filterBox, scrollPane, closeButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 800, 650);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void showAllDeliveries(Stage parentStage) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("All Deliveries - JoymarKet Admin");
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER);
		topBar.setStyle("-fx-background-color: #2196F3;");
		
		Label titleLabel = new Label("All Deliveries (Admin View)");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		titleLabel.setTextFill(Color.WHITE);
		
		topBar.getChildren().add(titleLabel);
		root.setTop(topBar);
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(20));
		
		List<Delivery> allDeliveries = DeliveryHandler.getAllDeliveries();
		
		Label statisticsLabel = new Label("Total Deliveries: " + allDeliveries.size());
		statisticsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefHeight(450);
		
		VBox deliveryList = new VBox(15);
		deliveryList.setPadding(new Insets(10));
		
		for (Delivery delivery : allDeliveries) {
			VBox deliveryCard = createDeliveryCardForAdmin(delivery);
			deliveryList.getChildren().add(deliveryCard);
		}
		
		if (allDeliveries.isEmpty()) {
			Label emptyLabel = new Label("No deliveries found.");
			emptyLabel.setFont(Font.font("Arial", 14));
			emptyLabel.setTextFill(Color.GRAY);
			deliveryList.getChildren().add(emptyLabel);
		}
		
		scrollPane.setContent(deliveryList);
		
		Button closeButton = new Button("Close");
		closeButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-cursor: hand;");
		closeButton.setOnAction(e -> stage.close());
		
		centerBox.getChildren().addAll(statisticsLabel, scrollPane, closeButton);
		root.setCenter(centerBox);
		
		Scene scene = new Scene(root, 800, 650);
		stage.setScene(scene);
		stage.show();
	}
	
	private static VBox createDeliveryCard(Delivery delivery, Courier courier, Stage parentStage) {
		VBox card = new VBox(15);
		card.setPadding(new Insets(20));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);");
		
		OrderHeader order = OrderHeaderHandler.getOrderHeader(delivery.getIdOrder());
		
		HBox headerRow = new HBox(20);
		headerRow.setAlignment(Pos.CENTER_LEFT);
		
		Label orderIdLabel = new Label("Order #" + delivery.getIdCourier());
		orderIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label statusBadge = new Label(delivery.getStatus());
		statusBadge.setFont(Font.font("Arial", FontWeight.BOLD, 13));
		statusBadge.setPadding(new Insets(6, 15, 6, 15));
		statusBadge.setStyle("-fx-background-radius: 15; -fx-background-color: " + getDeliveryStatusColor(delivery.getStatus()) + ";");
		
		headerRow.getChildren().addAll(orderIdLabel, spacer, statusBadge);
		
		if (order != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
			Label orderDateLabel = new Label("Ordered: " + dateFormat.format(order.getOrderedAt()));
			orderDateLabel.setFont(Font.font("Arial", 12));
			orderDateLabel.setTextFill(Color.web("#666666"));
			
			Label amountLabel = new Label("Amount: Rp" + formatCurrency(order.getTotalAmount()));
			amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			amountLabel.setTextFill(Color.web("#2E7D32"));
			
			card.getChildren().addAll(headerRow, orderDateLabel, amountLabel);
		} else {
			card.getChildren().add(headerRow);
		}
		
		Separator separator = new Separator();
		card.getChildren().add(separator);
		
		VBox updateBox = new VBox(12);
		
		Label updateLabel = new Label("Update Delivery Status:");
		updateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		HBox actionRow = new HBox(15);
		actionRow.setAlignment(Pos.CENTER_LEFT);
		
		ComboBox<String> statusCombo = new ComboBox<>();
		statusCombo.getItems().addAll("Pending", "In Progress", "Delivered");
		statusCombo.setValue(delivery.getStatus());
		statusCombo.setStyle("-fx-font-size: 14px; -fx-pref-width: 150px;");
		
		Button updateButton = new Button("Update Status");
		updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 25px; -fx-cursor: hand; -fx-background-radius: 5;");
		
		Label feedbackLabel = new Label();
		feedbackLabel.setFont(Font.font("Arial", 12));
		
		updateButton.setOnAction(e -> {
			String newStatus = statusCombo.getValue();
			String error = DeliveryHandler.editDeliveryStatus(delivery.getIdOrder(), courier.getIdUser(), newStatus);
			
			if (error != null) {
				feedbackLabel.setText(error);
				feedbackLabel.setTextFill(Color.RED);
			} else {
				feedbackLabel.setText("Status updated successfully!");
				feedbackLabel.setTextFill(Color.GREEN);
				
				new Thread(() -> {
					try {
						Thread.sleep(1500);
						Platform.runLater(() -> {
							parentStage.close();
							showCourierDeliveries(parentStage.getOwner() instanceof Stage ? (Stage) parentStage.getOwner() : null, courier);
						});
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}).start();
			}
		});
		
		actionRow.getChildren().addAll(statusCombo, updateButton, feedbackLabel);
		updateBox.getChildren().addAll(updateLabel, actionRow);
		
		card.getChildren().add(updateBox);
		
		return card;
	}
	
	private static VBox createDeliveryCardForAdmin(Delivery delivery) {
		VBox card = new VBox(12);
		card.setPadding(new Insets(15));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
		
		HBox topRow = new HBox(20);
		topRow.setAlignment(Pos.CENTER_LEFT);
		
		Label orderIdLabel = new Label("Order: " + delivery.getIdOrder());
		orderIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		Label courierLabel = new Label("Courier: " + delivery.getIdCourier());
		courierLabel.setFont(Font.font("Arial", 13));
		courierLabel.setTextFill(Color.web("#666666"));
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label statusLabel = new Label(delivery.getStatus());
		statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		statusLabel.setPadding(new Insets(4, 12, 4, 12));
		statusLabel.setStyle("-fx-background-radius: 12; -fx-background-color: " + getDeliveryStatusColor(delivery.getStatus()) + ";");
		statusLabel.setTextFill(Color.WHITE);
		
		topRow.getChildren().addAll(orderIdLabel, courierLabel, spacer, statusLabel);
		
		card.getChildren().add(topRow);
		return card;
	}
	
	private static String getDeliveryStatusColor(String status) {
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
