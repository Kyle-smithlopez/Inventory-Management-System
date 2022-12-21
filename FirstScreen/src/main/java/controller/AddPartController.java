package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class creates the Add Part Controller. */
public class AddPartController implements Initializable {

    private static int partId = 1;


    Stage stage;
    Parent scene;

    @FXML
    private RadioButton inHouseRBtn;
    @FXML
    private RadioButton outsourcedRBtn;
    @FXML
    private TextField partIdTxt;
    @FXML
    private TextField partNameTxt;
    @FXML
    private TextField partInvTxt;
    @FXML
    private TextField partPriceTxt;
    @FXML
    private TextField partMinTxt;
    @FXML
    private TextField partMaxTxt;
    @FXML
    private TextField partMachineIdTxt;
    @FXML
    public Label machineIdTxt;

    /**
     *  This method saves a new part when the Save button is clicked.
     * RUNTIME ERROR: Data would not save as it should. Issue was due to scope of variable.
     */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(partIdTxt.getText());
            String name = partNameTxt.getText();
            int stock = Integer.parseInt(partInvTxt.getText());
            double price = Double.parseDouble(partPriceTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());

            if(name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: Name Field must not be blank.");
                alert.showAndWait();
                return;
            }
            if(min > stock || stock > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The Min must be less than the Max, and Inv must be greater than the Min and less than the Max");
                alert.showAndWait();
                return;
            }

            if (inHouseRBtn.isSelected()) {
                int machineId = Integer.parseInt(partMachineIdTxt.getText());
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
            }
            else if (outsourcedRBtn.isSelected()) {
                String companyName = partMachineIdTxt.getText();
                Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
            }
            partId = getPartIdCount();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each Text Field.");
            alert.showAndWait();
        }
    }

    /** The Cancel method discards data and returns to the Main Menu. */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all Text Field values, Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get()  == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


    /** The InHouse Radio Button Method assigns Machine ID Text to the form. */
@FXML
    void OnActionInHouse(ActionEvent actionEvent) {
        machineIdTxt.setText("Machine ID");
        inHouseRBtn.setSelected(true);
    }

    /**
     * The Outsourced Radio Button Method assigns Company Name Text to the form.
     * @param actionEvent - Button clicked.
     * */
    @FXML
    public void OnActionOutsourced(ActionEvent actionEvent) {
        machineIdTxt.setText("Company Name");
        inHouseRBtn.setSelected(false);
    }

    //ID Counter
    public static int getPartIdCount() {
        partId++;
        return partId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partIdTxt.setText(String.valueOf(partId));
    }
}

