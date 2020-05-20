/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import static Model.Inventory.getAllParts;
import Model.OutsourcedPart;
import Model.Part;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import static View_Controller.MainScreenController.partToModifyIndex;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zachf
 */
public class ModifyPartScreenController implements Initializable {

    @FXML
    private RadioButton ModifyPartOutsourced;
    @FXML
    private RadioButton ModifyPartInHouse;
    @FXML
    private TextField PartID;
    @FXML
    private TextField PartName;
    @FXML
    private TextField PartCompanyName;
    @FXML
    private TextField PartInv;
    @FXML
    private TextField PartPrice;
    @FXML
    private TextField PartMax;
    @FXML
    private TextField PartMin;
    @FXML
    private Label PartLabelCompanyName;
    @FXML
    private TextField PartMachineID;
    @FXML
    private Label PartLabelMachineID;
    @FXML
    private Button ModifyPartSave;
    
    private boolean isOutsourced;
    int partIndex = partToModifyIndex();
    private String errorMessage = new String();
    private int partID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = getAllParts().get(partIndex);
        partID = getAllParts().get(partIndex).getID();
        PartID.setText("Part ID: " + partID);
        PartName.setText(part.getName());
        PartInv.setText(Integer.toString(part.getStock()));
        PartPrice.setText(Double.toString(part.getPrice()));
        PartMax.setText(Integer.toString(part.getMax()));
        PartMin.setText(Integer.toString(part.getMin()));
        
        if (part instanceof InhousePart) {
            PartMachineID.setText(Integer.toString(((InhousePart) getAllParts().get(partIndex)).getMachineID()));
            PartCompanyName.setText("");
            PartLabelMachineID.setVisible(true);
            PartMachineID.setVisible(true);
            PartCompanyName.setText("");
            PartLabelCompanyName.setVisible(false);
            PartCompanyName.setVisible(false);
            ModifyPartInHouse.setSelected(true);
            ModifyPartOutsourced.setSelected(false);
            isOutsourced = false;
        } else {
            PartCompanyName.setText(((OutsourcedPart) getAllParts().get(partIndex)).getCompanyName());
            PartMachineID.setText("");
            PartLabelMachineID.setVisible(false);
            PartMachineID.setVisible(false);
            PartMachineID.setText("");
            PartLabelCompanyName.setVisible(true);
            PartCompanyName.setVisible(true);
            ModifyPartOutsourced.setSelected(true);
            isOutsourced = true;
        } 
    }

    @FXML
    private void enableModifyOutsourcedPart(ActionEvent event) {
        ModifyPartInHouse.setSelected(false);
        ModifyPartOutsourced.setSelected(true);
        PartLabelMachineID.setVisible(false);
        PartMachineID.setVisible(false);
        PartMachineID.setText("");
        PartLabelCompanyName.setVisible(true);
        PartCompanyName.setVisible(true);
        isOutsourced = true;
    }

    @FXML
    private void enableModifyInHousePart(ActionEvent event) {
        ModifyPartInHouse.setSelected(true);
        ModifyPartOutsourced.setSelected(false);
        PartLabelMachineID.setVisible(true);
        PartMachineID.setVisible(true);
        PartCompanyName.setText("");
        PartLabelCompanyName.setVisible(false);
        PartCompanyName.setVisible(false);
        isOutsourced = false;
    }

    @FXML
    private void cancelModifyPart(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Part Delete");
        alert.setContentText("Are you sure you want to cancel update of part " + PartName.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Part add has been cancelled.");
            Parent partsCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel. Please complete part info.");
        }
    }

    @FXML
    private void saveModifyPart(ActionEvent event) throws IOException {
        String partName = PartName.getText();
        String partInv = PartInv.getText();
        String partPrice = PartPrice.getText();
        String partMax = PartMax.getText();
        String partMin = PartMin.getText();
        String partCompanyName = PartCompanyName.getText();
        String partMachineID = PartMachineID.getText();
        try {
            errorMessage = Part.validPart(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), errorMessage);
            
            if (errorMessage.length() > 0) {
                System.out.println(errorMessage);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                
                if (isOutsourced == false) {
                    System.out.println("In-House part.");
                    InhousePart inhousePart = new InhousePart();
                    inhousePart.setID(partID);
                    inhousePart.setName(partName);
                    inhousePart.setPrice(Double.parseDouble(partPrice));
                    inhousePart.setStock(Integer.parseInt(partInv));
                    inhousePart.setMin(Integer.parseInt(partMin));
                    inhousePart.setMax(Integer.parseInt(partMax));
                    inhousePart.setMachineID(Integer.parseInt(partMachineID));
                    Inventory.updatePart(partIndex, inhousePart);
                } else {
                    System.out.println("Outsourced part.");
                    OutsourcedPart outsourcedPart = new OutsourcedPart();
                    outsourcedPart.setID(partID);
                    outsourcedPart.setName(partName);
                    outsourcedPart.setPrice(Double.parseDouble(partPrice));
                    outsourcedPart.setStock(Integer.parseInt(partInv));
                    outsourcedPart.setMin(Integer.parseInt(partMin));
                    outsourcedPart.setMax(Integer.parseInt(partMax));
                    outsourcedPart.setCompanyName(partCompanyName);
                    Inventory.updatePart(partIndex, outsourcedPart);
                }

                Parent modifyProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductCancel);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            
            System.out.println("Blank Fields");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }
    }
    
}
