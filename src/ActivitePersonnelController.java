package kangamoyet.djadou.kouassid.applicationcentre2sante;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ActivitePersonnel;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ActivitePersonnelController implements Initializable {

    @FXML
    private TableView<ActivitePersonnel> tableView;
    @FXML
    private TableColumn<ActivitePersonnel, String> nomPersonnelColumn;
    @FXML
    private TableColumn<ActivitePersonnel, String> serviceColumn;
    @FXML
    private TableColumn<ActivitePersonnel, String> typeActiviteColumn;
    @FXML
    private TableColumn<ActivitePersonnel, String> descriptionColumn;
    @FXML
    private TableColumn<ActivitePersonnel, LocalDate> dateColumn;
    @FXML
    private TableColumn<ActivitePersonnel, LocalTime> heureColumn;

    private ObservableList<ActivitePersonnel> activiteList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activiteList = FXCollections.observableArrayList();

        nomPersonnelColumn.setCellValueFactory(new PropertyValueFactory<>("nomPersonnel"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        typeActiviteColumn.setCellValueFactory(new PropertyValueFactory<>("typeActivite"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateActivite"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heureActivite"));

        tableView.setItems(activiteList);

        loadActivities();
    }

    private void loadActivities() {
        String query = "SELECT * FROM activites_personnel";
        try (Connection connection = MySQLConnector.getConnect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_activite");
                String nomPersonnel = resultSet.getString("nom_personnel");
                String service = resultSet.getString("service");
                String typeActivite = resultSet.getString("type_activite");
                String description = resultSet.getString("description");
                LocalDate date = resultSet.getDate("date_activite").toLocalDate();
                LocalTime heure = resultSet.getTime("heure_activite").toLocalTime();

                ActivitePersonnel activite = new ActivitePersonnel(id, nomPersonnel, service, typeActivite, description, date, heure);
                activiteList.add(activite);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
