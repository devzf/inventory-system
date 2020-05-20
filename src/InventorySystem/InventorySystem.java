/*
Zachary Fredricksen
999825
C482 Software I
Inventory Management System
*/
package InventorySystem;

import View_Controller.MainScreenController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author zachf
 */
public class InventorySystem extends Application {
    Stage window;

    public void initMainScreen() throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InventorySystem.class.getResource("/View_Controller/MainScreen.fxml"));
        AnchorPane MainScreenView = (AnchorPane) loader.load();
    
        Scene scene = new Scene(MainScreenView);
        
        window.setScene(scene);
        window.show();
    }
    
    public void showMainScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InventorySystem.class.getResource("/View_Controller/MainScreen.fxml"));
        AnchorPane MainScreenView = (AnchorPane) loader.load();
        
        MainScreenController controller = loader.getController();
        controller.runMain(this);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Inventory Management System"); 
        initMainScreen();
        showMainScreen();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
