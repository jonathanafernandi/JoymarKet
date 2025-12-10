package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.UserWindow;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("JoymarKet - Grocery Marketplace");
		primaryStage.setResizable(false);
		
		UserWindow.showLoginWindow(primaryStage);
		
		primaryStage.show();
		
	}

}
