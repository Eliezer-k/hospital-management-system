package kangamoyet.djadou.kouassid.applicationcentre2sante;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HospitalisationController implements Initializable {
    @FXML
    private TextField idnom21;
    @FXML
    private DatePicker date2;
    @FXML
    private DatePicker date1;
    @FXML
    private TextField idnom2;
    @FXML
    private ComboBox<String> comboBox;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int patientId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connection = MySQLConnector.getConnect();
        date1.setEditable(false);
        date1.setValue(LocalDate.now());
        date1.setDisable(true);
        loadChambres();

        initializeForm();

        // Écouteur de sélection pour le ComboBox
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Chambre sélectionnée : " + newValue);
            }
        });

        date2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && date1.getValue() != null && newValue.isBefore(date1.getValue())) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de sortie ne peut pas être avant la date d'entrée.");
                date2.setValue(null); // Effacer la date incorrecte
            }
        });
    }

    @FXML
    private void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    private void loadChambres() {
        try {
            comboBox.getItems().clear(); // Effacer les éléments précédents
            String query = "SELECT nom FROM chambre WHERE disponibilite = 1 AND nom NOT IN " +
                    "(SELECT Chambre_occupé FROM hospitalisation WHERE date_fin >= CURDATE())";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String chambre = resultSet.getString("nom");
                if (!comboBox.getItems().contains(chambre)) { // Vérifiez si la chambre est déjà dans la liste
                    comboBox.getItems().add(chambre);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HospitalisationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    // Méthode personnalisée pour initialiser les données
    public void initializeForm() {
        setDefaultMotif();
    }

    // Définir le champ motif avec la valeur par défaut "Accouchement"
    private void setDefaultMotif() {
        idnom2.setText("Accouchement");
        idnom2.setEditable(false); // Rendre le champ non modifiable
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


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean isPatientCurrentlyHospitalized(int patientId) {
        String query = "SELECT COUNT(*) FROM hospitalisation WHERE id_patient = ? AND date_fin > CURDATE()";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HospitalisationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @FXML
    private void save(javafx.scene.input.MouseEvent mouseEvent) {
        connection = MySQLConnector.getConnect();
        String nom = idnom21.getText();
        int patientId = getPatientId(nom);
        java.sql.Date dateDebut = null;
        java.sql.Date dateFin = null;

        if (date1.getValue() != null) {
            LocalDate localDateDebut = date1.getValue();
            dateDebut = Date.valueOf(localDateDebut);
        }

        if (date2.getValue() != null) {
            LocalDate localDateFin = date2.getValue();
            dateFin = Date.valueOf(localDateFin);
        }

        String motif = idnom2.getText();
        String chambre = comboBox.getValue();

        if (nom.isEmpty() || dateDebut == null || dateFin == null || motif.isEmpty() || chambre.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
        } else if (isPatientCurrentlyHospitalized(patientId)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le patient est déjà hospitalisé.");
        } else {
            // Vérifier la durée maximale d'hospitalisation
            LocalDate maxDateFin = date1.getValue().plusDays(2);
            if (date2.getValue().isAfter(maxDateFin)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La durée maximale d'hospitalisation pour les femmes qui accouchent est de 2 jours.");
            } else {
                // Enregistrer la date de fin d'hospitalisation pour la chambre sélectionnée
                String queryUpdate = "UPDATE chambre SET disponibilite = 1 WHERE nom = ?";
                try {
                    preparedStatement = connection.prepareStatement(queryUpdate);
                    preparedStatement.setString(1, chambre);
                    preparedStatement.executeUpdate();
                    showConfirmationDialog();
                } catch (SQLException ex) {
                    Logger.getLogger(HospitalisationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        /**connection = MySQLConnector.getConnect();
        String nom = idnom21.getText();
        int patientId = getPatientId(nom);
        java.sql.Date dateDebut = null;
        java.sql.Date dateFin = null;

        if (date1.getValue() != null) {
            LocalDate localDateDebut = date1.getValue();
            dateDebut = Date.valueOf(localDateDebut);
        }

        if (date2.getValue() != null) {
            LocalDate localDateFin = date2.getValue();
            dateFin = Date.valueOf(localDateFin);
        }

        String motif = idnom2.getText();
        String chambre = comboBox.getValue();

        if (nom.isEmpty() || dateDebut == null || dateFin == null || motif.isEmpty() || chambre.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
        } else {
            // Vérifier la durée maximale d'hospitalisation
            LocalDate maxDateFin = date1.getValue().plusDays(2);
            if (date2.getValue().isAfter(maxDateFin)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La durée maximale d'hospitalisation pour les femmes qui accouchent est de 2 jours.");
            } else {
                // Enregistrer la date de fin d'hospitalisation pour la chambre sélectionnée
                String queryUpdate = "UPDATE chambre SET disponibilite = 1 WHERE nom = ?";
                try {
                    preparedStatement = connection.prepareStatement(queryUpdate);
                    preparedStatement.setString(1, chambre);
                    preparedStatement.executeUpdate();
                    showConfirmationDialog();
                } catch (SQLException ex) {
                    Logger.getLogger(HospitalisationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }**/
    }

    @FXML
    private void clean() {
        idnom21.clear();
        date1.setValue(null);
        date2.setValue(null);
        idnom2.clear();
        comboBox.getSelectionModel().clearSelection();
    }

    private void getQuery() {
        if (!update) {
            query = "INSERT INTO hospitalisation(id_patient, date_debut, date_fin, motif, Chambre_occupé) VALUES (?, ?, ?, ?, ?)";
        } else {
            query = "UPDATE hospitalisation SET "
                    + "date_debut = ?,"
                    + "date_fin = ?,"
                    + "motif = ?,"
                    + "Chambre_occupé = ? WHERE id_hospitalisation = ?";
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
            Logger.getLogger(HospitalisationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patientId; // Retourne 0 si le patient n'est pas trouvé
    }


    private void insert() {
        try {
            if (date1.getValue() == null || date2.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date de début et de fin.");
                return;
            }

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getPatientId(idnom21.getText())); // Utilisez l'ID du patient
            preparedStatement.setDate(2, java.sql.Date.valueOf(date1.getValue()));
            preparedStatement.setDate(3, java.sql.Date.valueOf(date2.getValue()));
            preparedStatement.setString(4, idnom2.getText());
            preparedStatement.setString(5, comboBox.getValue());
            preparedStatement.execute();

            String typeInteraction = "Hospitalisation";
            String detailsInteraction = "Hospitalisation de " + idnom21.getText() + " pour " + idnom2.getText() + " en " + comboBox.getValue();
            enregistrerInteraction(getPatientId(idnom21.getText()), typeInteraction, detailsInteraction);


            // Afficher la boîte de dialogue de confirmation
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText(null);
            dialog.setContentText("Hospitalisation enregistrée avec succès.");
            dialog.showAndWait();

            // Mettre à jour la liste des chambres disponibles
            loadChambres();
        } catch (SQLException ex) {
            Logger.getLogger(HospitalisationController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HospitalisationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }



    public void setPatient(int id, String nom) {
        patientId = id;
        idnom21.setText(nom);
        idnom21.setEditable(false);
    }
}
