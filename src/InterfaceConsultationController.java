package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceConsultationController implements Initializable {

    @FXML
    private TextField idnom;
    @FXML
    private DatePicker iddate;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button button21;
    @FXML
    private TextArea li1;
    @FXML
    private TextArea li2;
    @FXML
    private TextArea li3;
    @FXML
    private TextArea li4;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton button1;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int patientId;
    private String medecinNom;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisez votre connexion ici
        connection = MySQLConnector.getConnect();
        // Autres initialisations...
        iddate.setEditable(false);
        iddate.setValue(LocalDate.now());
        iddate.setDisable(true);


        // Utilisez un PreparedStatement pour éviter les injections SQL
        query = "SELECT nom FROM personnel WHERE specialite = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            preparedStatement.setString(1, "Medecin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajoutez les noms des médecins au ComboBox

        List<String> medecins = new ArrayList<>();
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                medecins.add(rs.getString("nom"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        comboBox.setItems(FXCollections.observableArrayList(medecins));



    }

    @FXML
    private void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Afficher(javafx.scene.input.MouseEvent mouseEvent) throws IOException {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableViewConsultation.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) button21.getScene().getWindow();

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void enregistrerInteraction(int patientId, String typeInteraction, String detailsInteraction) {
        String query = "INSERT INTO suivi_patient (id_patient, date_interaction, type_interaction, details_interaction) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setDate(2, new java.sql.Date(System.currentTimeMillis())); // Utilisez la date actuelle
            preparedStatement.setString(3, typeInteraction);
            preparedStatement.setString(4, detailsInteraction);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeconnexionButtonAction(javafx.scene.input.MouseEvent mouseEvent) throws IOException {

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            Parent root = loader.load();
            // Crée une nouvelle scène avec la nouvelle interface
            Scene scene = new Scene(root);
            // Obtient la fenêtre actuelle à partir du bouton
            Stage stage = (Stage) button21.getScene().getWindow();
            // Remplace la scène actuelle par la nouvelle scène
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**FXMLLoader loader = new FXMLLoader(getClass().getResource("Principale.fxml"));
         Parent root = loader.load();
         Scene scene = new Scene(root,700,396);
         stage.setScene(scene);
         stage.show();**/
    }
    @FXML
    private void clean() {
        idnom.clear();
        iddate.setValue(null);
        li1.clear();
        li2.clear();
        li3.clear();
        li4.clear();
        comboBox.setValue(null);

    }

    private int getMedecinId(String medecinNom) {
        int medecinId = 0; // ou une valeur par défaut appropriée
        try {
            String query = "SELECT id_personnel FROM personnel WHERE nom = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, medecinNom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                medecinId = rs.getInt("id_personnel");
            }
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceConsultationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return medecinId;
    }



    private void showConfirmationDialog(int medecinId) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir enregistrer ?");
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            getQuery();
            insert(medecinId);
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
    /**private void enregistrerInteraction(int patientId, String typeInteraction, String detailsInteraction) {
        String query = "INSERT INTO suivi_patient (id_patient, date_interaction, type_interaction, details_interaction) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setDate(2, new java.sql.Date(System.currentTimeMillis())); // Utilisez la date actuelle
            preparedStatement.setString(3, typeInteraction);
            preparedStatement.setString(4, detailsInteraction);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }**/

    @FXML
    private void save(javafx.scene.input.MouseEvent mouseEvent) {

        connection = MySQLConnector.getConnect();
        String nom = idnom.getText();
        java.sql.Date dateNaissance = null;
        if (iddate.getValue() != null) {
            LocalDate localDate = iddate.getValue();
            dateNaissance = java.sql.Date.valueOf(localDate);
        }
        String Symptomes = li1.getText();
        String Examen = li2.getText();
        String Diagnostic = li3.getText();
        String Traitement = li4.getText();
        String medecin = comboBox.getValue() != null ? comboBox.getValue() : "";

        if (nom.isEmpty()  || dateNaissance == null || Symptomes.isEmpty() || Examen.isEmpty() || Diagnostic.isEmpty() || Traitement.isEmpty()||  medecin.isEmpty() ) {
            showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
        } else {
            String medecinNom = comboBox.getValue();
            int medecinId = getMedecinId(medecinNom);
            showConfirmationDialog(medecinId);

            /** // Ajoutez ce bloc de code après la confirmation pour mettre à jour la table suivi_patient
            String typeInteraction = "Hospitalisation";
            String detailsInteraction = "Hospitalisation de " + nom + " pour " + motif + " dans la chambre " + chambre;
            enregistrerInteraction(patientId, typeInteraction, detailsInteraction);**/
        }
    }
    private void logActivity(String personnel, String service, String typeActivite, LocalDate date, LocalTime heure) {
        String query = "INSERT INTO activites_personnel (nom_personnel, service, type_activite, date_activite, heure_activite) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, personnel);
            pst.setString(2, service);
            pst.setString(3, typeActivite);
            pst.setDate(4, Date.valueOf(date));
            pst.setTime(5, Time.valueOf(heure));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getQuery() {
        if (!update) {
            query = "INSERT INTO `consultation`(`id_patient`, `id_personnel`, `date_consultation`, `symptome`, `examensRequis`, `diagnostic`, `traitement_prescrit`) VALUES (?,?,?,?,?,?,?)";
        } else {
            query = "UPDATE `consultation` SET "
                    + "`date_consultation`=?,"
                    + "`symptome`=?,"
                    + "`examensRequis`=?,"
                    + "`diagnostic`=?,"
                    + "`traitement_prescrit`= ? WHERE id_consultation = '" +patientId + "'";
        }
    }

    private void insert(int medecinId) {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, medecinId);
            preparedStatement.setString(3, String.valueOf(iddate.getValue()));
            preparedStatement.setString(4, li1.getText());
            preparedStatement.setString(5, li2.getText());
            preparedStatement.setString(6, li3.getText());

            // Vérifie si le champ traitement_prescrit est vide ou non
            String traitementPrescrit = li4 == null || li4.getText().length() == 0 ? null : li4.getText();
            preparedStatement.setString(7, traitementPrescrit);
            preparedStatement.execute();

            // Ajoutez ce bloc de code après la confirmation pour mettre à jour la table suivi_patient
            String typeInteraction = "Consultation";
            String detailsInteraction = "Consultation de " + idnom.getText() + " pour " + li1.getText();
            enregistrerInteraction(patientId, typeInteraction, detailsInteraction);

            logActivity(comboBox.getValue(), "Consultation", " Pour: " + li1.getText(), iddate.getValue(), LocalTime.now());

            // Afficher la boîte de dialogue de confirmation
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText(null);
            dialog.setContentText("Consultation Enregistré avec succès.");
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
