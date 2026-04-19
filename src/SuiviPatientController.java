package kangamoyet.djadou.kouassid.applicationcentre2sante;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.SuiviPatient;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuiviPatientController implements Initializable {

    @FXML
    private TableView<SuiviPatient> tableSuiviPatient;

    @FXML
    private TableColumn<SuiviPatient, Date> colDate;

    @FXML
    private TableColumn<SuiviPatient, String> colType;

    @FXML
    private TableColumn<SuiviPatient, String> colDetails;

    private Connection connection = null;
    private ObservableList<SuiviPatient> suiviPatientList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connection = MySQLConnector.getConnect(); // Initialisez la connexion à la base de données
        refreshTable(); // Charge les données depuis la base de données
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateInteraction"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeInteraction"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("detailsInteraction"));
        tableSuiviPatient.setItems(suiviPatientList);

        tableSuiviPatient.getStylesheets().add(getClass().getResource("/css/Design.css").toExternalForm());

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void refreshTable() {
        try {
            suiviPatientList.clear();
            String query = "SELECT * FROM `suivi_patient`";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                suiviPatientList.add(new SuiviPatient(
                        resultSet.getInt("id_suivi"),
                        resultSet.getInt("id_patient"),
                        resultSet.getDate("date_interaction"),
                        resultSet.getString("type_interaction"),
                        resultSet.getString("details_interaction")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(SuiviPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
