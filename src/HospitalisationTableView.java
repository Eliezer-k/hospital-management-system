package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Hospitalisation;
import models.Patients;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HospitalisationTableView implements Initializable {

    @FXML
    private TableView<Hospitalisation> HospitalisationTable;
    @FXML
    private TableColumn<Hospitalisation, Integer> idCol;
    @FXML
    private TableColumn<Hospitalisation, String> nameCol;
    @FXML
    private TableColumn<Hospitalisation, LocalDate> prenomCol;
    @FXML
    private TableColumn<Hospitalisation, LocalDate> sexeCol;
    @FXML
    private TableColumn<Hospitalisation, String> SpecialiteCol;
    @FXML
    private TableColumn<Hospitalisation, String> editCol;
    @FXML
    private TableColumn<Hospitalisation, String> editCol1;
    @FXML
    private TextField searchField;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Patients personnel = null;

    ObservableList<Hospitalisation> HospList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDate();

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterResults(newValue);
            }
        });
    }

    private void filterResults(String searchText) {
        ObservableList<Hospitalisation> filteredList = HospList.stream()
                .filter(resultat -> resultat.getNomPatient().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList));
        HospitalisationTable.setItems(filteredList);
    }

    @FXML
    private void getAddView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("AjouterAccouchement.fxml"));
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HospitalisationTableView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refreshTable() {
        try {
            HospList.clear();

            query = "SELECT a.id_hospitalisation, a.id_patient, p.nom AS nomPatient, a.date_debut, a.date_fin, " +
                    "a.motif, a.Chambre_occupé " +
                    "FROM hospitalisation a " +
                    "JOIN patient p ON a.id_patient = p.id_patient";

            connection = MySQLConnector.getConnect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HospList.add(new Hospitalisation(
                        resultSet.getInt("id_hospitalisation"),
                        resultSet.getInt("id_patient"),
                        resultSet.getString("nomPatient"),
                        resultSet.getDate("date_debut").toLocalDate(),
                        resultSet.getDate("date_fin").toLocalDate(),
                        resultSet.getString("motif"),
                        resultSet.getString("Chambre_occupé")));
                HospitalisationTable.setItems(HospList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(HospitalisationTableView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDate() {
        connection = MySQLConnector.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("idHospitalisation"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        SpecialiteCol.setCellValueFactory(new PropertyValueFactory<>("motif"));
        editCol.setCellValueFactory(new PropertyValueFactory<>("chambreOccupee"));

        Callback<TableColumn<Hospitalisation, String>, TableCell<Hospitalisation, String>> cellFactory = (TableColumn<Hospitalisation, String> param) -> {
            final TableCell<Hospitalisation, String> cell = new TableCell<Hospitalisation, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button deleteBtn = new Button("Supprimer");

                        deleteBtn.setOnMouseClicked(event -> {
                            try {
                                Hospitalisation hosp = getTableView().getItems().get(getIndex());
                                String query = "DELETE FROM hospitalisation WHERE id_hospitalisation =" + hosp.getIdHospitalisation();
                                Connection connection = MySQLConnector.getConnect();
                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                            } catch (SQLException ex) {
                                Logger.getLogger(HospitalisationTableView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        HBox manageBtn = new HBox(deleteBtn);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));

                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        editCol1.setCellFactory(cellFactory);
        HospitalisationTable.setItems(HospList);
    }
}
