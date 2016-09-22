/**
 * 
 */
/**
 * @author alokps
 *
 */
package alokps;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

class JavaFxQuit extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		initUI(primaryStage);
		
	}
	
	private void initUI(Stage stage){
		
		Button button = new Button();
		button.setText("Simple Button Quit");
		button.setOnAction((ActionEvent Event)->{
			Platform.exit();
		});
		
		HBox root = new HBox();
		root.setPadding(new Insets(25));
		root.getChildren().add(button);
		
		Scene scene = new Scene(root,150,150);
		stage.setTitle("Simple App");
		stage.setScene(scene);
		stage.show();
	
	}
	
	public static void main(String args[]){
		launch(args);
	}
	
	
}