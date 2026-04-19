package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.rendezVous;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AjouterRDVcontroller implements Initializable {

        @FXML
        private TableView<rendezVous> studentsTable;
        @FXML
        private DatePicker datep;
        @FXML
        private ComboBox<String> comboBox1;
        @FXML
        private ComboBox<String> combox2;
        @FXML
        private TextArea text;
        @FXML
        private JFXButton ButtonAj;
        @FXML
        private JFXButton refreshButton;
        @FXML
        private JFXButton closeButton;


        private String query = null;
        private Connection connection = null;
        private PreparedStatement preparedStatement = null;
        private boolean update = false;
        private int patientId;

        /**ResultSet resultSet = null ;
        rendezVous rdv = null ;

        ObservableList<rendezVous> RDVList = FXCollections.observableArrayList();**/

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            fillComboBox1();
            // Initialisez votre connexion ici
            connection = MySQLConnector.getConnect();


            // Utilisez un PreparedStatement pour éviter les injections SQL
            query = "SELECT DISTINCT nom_patient FROM resultat";
            try {
                preparedStatement = connection.prepareStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ResultSet rs = null;
            try {
                rs = preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Ajoutez les noms des patients au ComboBox
            List<String> patients = new ArrayList<>();
            while (true) {
                try {
                    if (!rs.next()) break;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    patients.add(rs.getString("nom_patient"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            comboBox1.setItems(FXCollections.observableArrayList(patients));

        }

        @FXML
        private void close(javafx.scene.input.MouseEvent mouseEvent) {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        public void onDateSelected() {
            LocalDate selectedDate = datep.getValue();
            // Faites quelque chose avec la date sélectionnée
            System.out.println("Date de naissance sélectionnée : " + selectedDate);
        }


    @FXML
        private void save(javafx.scene.input.MouseEvent mouseEvent) {

            connection = MySQLConnector.getConnect();

            String sexe = comboBox1.getValue() != null ? comboBox1.getValue() : "";
            java.sql.Date dateNaissance = null;
            if (datep.getValue() != null) {
                LocalDate localDate = datep.getValue();
                dateNaissance = java.sql.Date.valueOf(localDate);
            }
            String oui = combox2.getValue() != null ? combox2.getValue() : "";
            String telephone = text.getText();

            if ( sexe.isEmpty() || dateNaissance == null || oui.isEmpty() || telephone.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
            } else {
                showConfirmationDialog();
            }
        }

        @FXML
        private void clean() {
            comboBox1.setValue(null);
            combox2.setValue(null);
            datep.setValue(null);
            text.clear();

        }

        private void fillComboBox1() {
            ObservableList<String> items = FXCollections.observableArrayList("8:00:00", "10:00:00", "14:00:00", "16:00:00");
            combox2.setItems(items);
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
                query = "INSERT INTO `rendez_vous`(`nom`, `date_rdv`, `heure_rdv`, `motif`) VALUES (?,?,?,?)";
            } else {
                query = "UPDATE `rendez_vous` SET "
                        + "`nom`=?,"
                        + "`date_rdv`=?,"
                        + "`heure_rdv`=?,"
                        + "`motif`= ? WHERE id = '" +patientId + "'";
            }
        }

        private void insert() {
            try {
                preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, comboBox1.getValue());
                preparedStatement.setString(2, String.valueOf(datep.getValue()));
                preparedStatement.setString(3, combox2.getValue());
                preparedStatement.setString(4, text.getText());
                preparedStatement.execute();

                // Afficher la boîte de dialogue de confirmation
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText(null);
                dialog.setContentText("Rendez-vous Enregistré avec succès.");
                dialog.showAndWait();

            } catch (SQLException ex) {
                Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.AjouterPatientsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        void setTextField(int id, String name, String date, String heure, String motif) {
            patientId = id;
            comboBox1.setValue(name);
            datep.setValue(LocalDate.parse(date));
            combox2.setValue(heure.toString());
            text.setText(motif);
        }

    void setUpdate(boolean b) {
            this.update = b;
        }

}
