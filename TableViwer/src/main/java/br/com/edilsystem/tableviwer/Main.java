package br.com.edilsystem.tableviwer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	public static String parameters;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		parameters = getParameters().getRaw().get(0);
		Parent root = FXMLLoader.load(getClass().getResource("view/TableViewer.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Edilsystem");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
