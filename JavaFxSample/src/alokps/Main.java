package alokps;
	
import java.awt.Label;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			StackPane root = new StackPane();
			Scene scene = new Scene(root, 150,150);
			Label lbl = new Label("Java Fx Simple Application");
			//lbl.setFont(Font.font("Serif",FontWeight.NORMAL,20));
			lbl.setFont(null);
			root.getChildren().add(root);
			primaryStage.setTitle("Simple Application");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
