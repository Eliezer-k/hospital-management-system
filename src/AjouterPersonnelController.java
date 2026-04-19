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

public class AjouterPersonnelController implements Initializable {

        @FXML
        private TextField idnom;
        @FXML
        private TextField idprenom;
        @FXML
        private ComboBox<String> idsexe;
        @FXML
        private ComboBox<String> idspe;
        @FXML
        private JFXButton closeButton;
        @FXML
        private JFXButton ButtonAj;


        private String query = null;
        private Connection connection = null;
        private PreparedStatement preparedStatement = null;
        private boolean update = false;
        private int personnelId;

        @Override
        public void initialize(URL url, ResourceBundle rb) {

            fillComboBox1();
            fillComboBox2();
        }

        @FXML
        private void close(javafx.scene.input.MouseEvent mouseEvent) {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }


        @FXML
        private void save(javafx.scene.input.MouseEvent mouseEvent) {

            connection = MySQLConnector.getConnect();
            String nom = idnom.getText();
            String prenom = idprenom.getText();
            String sexe = idsexe.getValue() != null ? idsexe.getValue() : "";
            String specialite = idspe.getValue() != null ? idspe.getValue() : "";


            if (nom.isEmpty() || prenom.isEmpty() ||  sexe.isEmpty() || specialite.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
            } else {
                showConfirmationDialog();
            }
        }

        @FXML
        private void clean() {
            idnom.clear();
            idprenom.clear();
            idsexe.setValue(null);
            idspe.setValue(null);

        }

        private void fillComboBox1() {
            ObservableList<String> items = FXCollections.observableArrayList("Homme", "Femme");
            idsexe.setItems(items);
        }
        private void fillComboBox2() {
            ObservableList<String> items = FXCollections.observableArrayList("Medecin", "Infirmier", "Sage-femme");
            idspe.setItems(items);
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
                query = "INSERT INTO `personnel`(`nom`, `prenom`, `sexe`, `specialite`) VALUES (?,?,?,?)";
            } else {
                query = "UPDATE `personnel` SET "
                        + "`nom`=?,"
                        + "`prenom`=?,"
                        + "`sexe`=?,"
                        + "`specialite`= ? WHERE id_personnel = '" +personnelId + "'";
            }
        }

        private void insert() {
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, idnom.getText());
                preparedStatement.setString(2, idprenom.getText());
                preparedStatement.setString(3, idsexe.getValue());
                preparedStatement.setString(4, idspe.getValue());
                preparedStatement.execute();

                // Afficher la boîte de dialogue de confirmation
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText(null);
                dialog.setContentText("Personnel ajouter avec succès.");
                dialog.showAndWait();


            } catch (SQLException ex) {
                Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.AjouterPersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        void setTextField(int id, String name, String prenom, String sexe, String specialite) {
            personnelId = id;
            idnom.setText(name);
            idprenom.setText(prenom);
            idsexe.setValue(sexe);
            idspe.setValue(specialite);

        }

        void setUpdate(boolean b) {
            this.update = b;
        }


}
