package kangamoyet.djadou.kouassid.applicationcentre2sante;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConstanteController implements Initializable {
    @FXML
    private TextField idnom;
    @FXML
    private DatePicker iddate;
    @FXML
    private TextField idnom1;
    @FXML
    private TextField idnom11;
    @FXML
    private TextField idnom12;
    @FXML
    private TextField idnom120;
    @FXML
    private TextField idnom111;




    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int patientId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisez votre connexion ici
        connection = MySQLConnector.getConnect();
        // Autres initialisations...
        iddate.setEditable(false);
        iddate.setValue(LocalDate.now());
        iddate.setDisable(true);



    }
    @FXML
    private void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
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

        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void save(javafx.scene.input.MouseEvent mouseEvent) {

        connection = MySQLConnector.getConnect();
        String nom = idnom.getText();
        int patientId = getPatientId(nom);
        java.sql.Date dateNaissance = null;
        if (iddate.getValue() != null) {
            LocalDate localDate = iddate.getValue();
            dateNaissance = java.sql.Date.valueOf(localDate);
        }
        String temperature = idnom1.getText();
        String poids = idnom11.getText();
        String pression_arterielle = idnom12.getText();
        String frequence_cardiaque = idnom120.getText();
        String frequence_respiratoire = idnom111.getText();

        if (nom.isEmpty() || dateNaissance == null || temperature.isEmpty() || poids.isEmpty() || pression_arterielle.isEmpty() || frequence_cardiaque.isEmpty() || frequence_respiratoire.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
        } else {
            showConfirmationDialog();
        }

    }
    @FXML
    private void clean() {
        idnom.clear();
        iddate.setValue(null);
        idnom1.clear();
        idnom11.clear();
        idnom12.clear();
        idnom120.clear();
        idnom111.clear();

    }

    private void getQuery() {
        if (!update) {
            query = "INSERT INTO `constante`(`id_patient`, `date`, `temperature`, `poids`, `pression_arterielle`, `frequence_cardiaque`, `frequence_respiratoire`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            query = "UPDATE `constante` SET "
                    + "`date`=?,"
                    + "`temperature`=?,"
                    + "`poids`=?,"
                    + "`pression_arterielle`=?,"
                    + "`frequence_cardiaque`=?,"
                    + "`fréquence_respiratoire`= ? WHERE id = '" +patientId + "'";
        }
    }

    private int getPatientId(String nom) {
        int patientId = 0;
        try {
            String query = "SELECT id_patient FROM patient WHERE nom = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_patient");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConstanteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patientId ; // Retourne -1 si le patient n'est pas trouvé
    }


    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getPatientId(idnom.getText())); // Utilisez l'ID du patient
            preparedStatement.setDate(2, java.sql.Date.valueOf(iddate.getValue()));
            preparedStatement.setDouble(3, Double.parseDouble(idnom1.getText()));
            preparedStatement.setDouble(4, Double.parseDouble(idnom11.getText()));
            preparedStatement.setDouble(5, Double.parseDouble(idnom12.getText()));
            preparedStatement.setInt(6, Integer.parseInt(idnom120.getText()));
            preparedStatement.setInt(7, Integer.parseInt(idnom111.getText()));
            preparedStatement.execute();

            // Afficher la boîte de dialogue de confirmation
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText(null);
            dialog.setContentText("Constantes Enregistrés avec succès.");
            dialog.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(AjouterPatientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPatient(int id, String nom) {
        patientId = id;
        idnom.setText(nom);
        idnom.setEditable(false);

    }

}
