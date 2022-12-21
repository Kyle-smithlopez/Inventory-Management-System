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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

    /** The Modify Part controller pulls up the selected part information to modify. */
public class ModifyPartController implements Initializable {


    Stage stage;
    Parent scene;

    @FXML
    public RadioButton outsourcedRBtn;
    @FXML
    public RadioButton inHouseRBtn;
    @FXML
    public TextField modifyPartIdTxt;
    @FXML
    public TextField modifyMachineIdTxt;
    @FXML
    public Label machineIdTxt;
    @FXML
    public TextField modifyPartNameTxt;
    @FXML
    public TextField modifyPartInvTxt;
    @FXML
    public TextField modifyPartPriceTxt;
    @FXML
    public TextField modifyPartMinTxt;
    @FXML
    public TextField modifyPartMaxTxt;

    /** InHouse Radio Button assigns machine ID Text to form. */
    @FXML
    public void OnActionInHouse(ActionEvent actionEvent) {
        machineIdTxt.setText("Machine ID");
    }

    /** The Outsourced Radio Button assigns Company name text to form. */
    @FXML
    public void OnActionOutsourced(ActionEvent actionEvent) {
        machineIdTxt.setText("Company Name");
    }


    /** Send Part method retrieves the selected parts data and displays on the form.
     * RUNTIME ERROR: Early drafts had errors pulling the appropriate data, issue was due to reflection. Assigned accurate variable names resolved issue.
     * */
    public void sendPart(Part part) {
        modifyPartIdTxt.setText(String.valueOf(part.getId()));
        modifyPartInvTxt.setText(String.valueOf(part.getStock()));
        modifyPartNameTxt.setText(part.getName());
        modifyPartPriceTxt.setText(String.valueOf(part.getPrice()));
        modifyPartMinTxt.setText(String.valueOf(part.getMin()));
        modifyPartMaxTxt.setText(String.valueOf(part.getMax()));

        if(part instanceof InHouse) {
            inHouseRBtn.setSelected(true);
            machineIdTxt.setText("Machine ID");
            modifyMachineIdTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        else {
            outsourcedRBtn.setSelected(true);
            machineIdTxt.setText("Company Name");
            modifyMachineIdTxt.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
    }

    /** Modify Part method saves the new data and replaces the current data on the main menu. */
    @FXML
    void OnActionModifyPart(ActionEvent event) throws IOException {

        try {

        int id = Integer.parseInt(modifyPartIdTxt.getText());
        String name = modifyPartNameTxt.getText();
        int stock = Integer.parseInt(modifyPartInvTxt.getText());
        double price = Double.parseDouble(modifyPartPriceTxt.getText());
        int min = Integer.parseInt(modifyPartMinTxt.getText());
        int max = Integer.parseInt(modifyPartMaxTxt.getText());

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
                int machineID = Integer.parseInt(modifyMachineIdTxt.getText());
                Inventory.updatePart(id, new InHouse(id, name, price, stock, min, max, machineID));
            }
            else if (outsourcedRBtn.isSelected()) {
                String companyName = modifyMachineIdTxt.getText();
                Inventory.updatePart(id, new Outsourced(id, name, price, stock, min, max, companyName));
            }
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

    /** The cancel method discards the data and returns to the main menu. */
    @FXML
    void OnActionCancelModifyPart(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will not save any changes, Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get()  == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
