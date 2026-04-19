package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AjouterPatientsController implements Initializable {

    @FXML
    private TextField idnom;
    @FXML
    private TextField idprenom;
    @FXML
    private DatePicker iddate;
    @FXML
    private ComboBox<String> comboBox1;
    @FXML
    private TextField idtel;
    @FXML
    private TextField idnum;
    @FXML
    private JFXButton ButtonAj;
    @FXML
    private JFXButton closeButton;


    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int patientId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillComboBox1();
    }

    @FXML
    private void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    public void onDateSelected() {
        LocalDate selectedDate = iddate.getValue();
        // Faites quelque chose avec la date sélectionnée
        System.out.println("Date de naissance sélectionnée : " + selectedDate);
    }

    @FXML
    private void save(javafx.scene.input.MouseEvent mouseEvent) {

        connection = MySQLConnector.getConnect();
        String nom = idnom.getText();
        String prenom = idprenom.getText();
        java.sql.Date dateNaissance = null;
        if (iddate.getValue() != null) {
            LocalDate localDate = iddate.getValue();
            dateNaissance = java.sql.Date.valueOf(localDate);
        }
        String sexe = comboBox1.getValue() != null ? comboBox1.getValue() : "";
        String telephone = idtel.getText();
        String Numero = idnum.getText();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null || sexe.isEmpty() || telephone.isEmpty() || Numero.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
        } else {
            showConfirmationDialog();
        }
    }

    @FXML
    private void clean() {
        idnom.clear();
        idprenom.clear();
        iddate.setValue(null);
        comboBox1.setValue(null);
        idtel.clear();
        idnum.clear();
    }

    private void fillComboBox1() {
        ObservableList<String> items = FXCollections.observableArrayList("Homme", "Femme");
        comboBox1.setItems(items);
    }

    private void showConfirmationDialog() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir enregistrer ?");
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            getQuery();
            insert();
            clean();
            Alert Dialog = new Alert(Alert.AlertType.INFORMATION);
            Dialog.setTitle("Confirmation");
            Dialog.setHeaderText(null);
            Dialog.setContentText("Enrégistrer avec succès");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void getQuery() {
        if (!update) {
            query = "INSERT INTO `patient`(`nom`, `prenom`, `date_naissance`, `sexe`, `telephone`, `Numero`) VALUES (?,?,?,?,?,?)";
        } else {
            query = "UPDATE `patient` SET "
                    + "`nom`=?,"
                    + "`prenom`=?,"
                    + "`date_naissance`=?,"
                    + "`sexe`=?,"
                    + "`telephone`=?,"
                    + "`Numero`= ? WHERE id_patient = '" +patientId + "'";
        }
    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, idnom.getText());
            preparedStatement.setString(2, idprenom.getText());
            preparedStatement.setString(3, String.valueOf(iddate.getValue()));
            preparedStatement.setString(4, comboBox1.getValue());
            preparedStatement.setString(5, idtel.getText());
            preparedStatement.setString(6, idnum.getText());
            preparedStatement.execute();

            // Afficher la boîte de dialogue de confirmation
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText(null);
            dialog.setContentText("Patient Enregistré avec succès.");
            dialog.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(AjouterPatientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setTextField(int id, String name, String prenom, LocalDate toLocalDate, String sexe, String telephone, String Numero) {
        patientId = id;
        idnom.setText(name);
        idprenom.setText(prenom);
        iddate.setValue(toLocalDate);
        comboBox1.setValue(sexe);
        idtel.setText(telephone);
        idnum.setText(Numero);
    }

    void setUpdate(boolean b) {
        this.update = b;
    }

}
