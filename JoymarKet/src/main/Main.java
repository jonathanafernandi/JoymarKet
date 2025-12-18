package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.UserWindow;

// Main entry point of the application
public class Main extends Application {

    // Launch JavaFX application
	public static void main(String[] args) {
		launch(args);
	}

    // Start method called when application runs
	@Override
	public void start(Stage primaryStage) throws Exception {

        // Set application window title
		primaryStage.setTitle("JoymarKet - Grocery Marketplace");
		primaryStage.setResizable(false);
		
        // Show login window first
		UserWindow.showLoginWindow(primaryStage);
		
        // Display the main stage
		primaryStage.show();
	}

}
