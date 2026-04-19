package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AccouchementFormController implements Initializable {

    @FXML
    private ComboBox<String> patientComboBox;

    @FXML
    private TextField heureAccouchementField;

    @FXML
    private DatePicker dateAccouchementPicker;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton ButtonAj;

    @FXML
    private TextArea detailsComplicationsField;

    @FXML
    private CheckBox check1;

    @FXML
    private CheckBox check2;

    @FXML
    private ComboBox<String> typeAccouchementComboBox;

    @FXML
    private ComboBox<String> nomMedecinField;

    @FXML
    private ComboBox<String> nomSageFemmeField;

    @FXML
    private TextField poidsBebeField;

    @FXML
    private TextField idnom21;

    @FXML
    private TextField tailleBebeField;

    @FXML
    private ComboBox<String> sexeBebeComboBox;


    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int accouchementId;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fillComboBox1();

        connection = MySQLConnector.getConnect();

        dateAccouchementPicker.setEditable(false);
        dateAccouchementPicker.setValue(LocalDate.now());
        dateAccouchementPicker.setDisable(true);

        // Formatter pour l'heure
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Obtenir l'heure actuelle
        LocalTime currentTime = LocalTime.now();

        // Formater l'heure actuelle
        String formattedTime = currentTime.format(timeFormatter);

        // Définir l'heure actuelle dans le champ de texte
        heureAccouchementField.setText(formattedTime);


        // Ajouter des listeners pour rendre les checkboxes mutuellement exclusives
        check1.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                check2.setSelected(false);
                detailsComplicationsField.setText("");  // Vider le champ de texte
                detailsComplicationsField.setEditable(true);  // Rendre le champ éditable
            }
        });

        check2.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                check1.setSelected(false);
                detailsComplicationsField.setText("NULL");  // Définir le texte à "null"
                detailsComplicationsField.setEditable(false);  // Rendre le champ non éditable
            }
        });

        // Initialement, définir le champ de texte à non éditable si "Non" est sélectionné par défaut
        if (check2.isSelected()) {
            detailsComplicationsField.setText("NULL");
            detailsComplicationsField.setEditable(false);
        }


        // Ajouter des listeners pour rendre les checkboxes mutuellement exclusives
        check1.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                check2.setSelected(false);
            }
        });

        check2.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                check1.setSelected(false);
            }
        });


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
        List<String> Medecin = new ArrayList<>();
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Medecin.add(rs.getString("nom"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        nomMedecinField.setItems(FXCollections.observableArrayList(Medecin));

        /**//////////////////////////////////////////////////////////////////////**/

        query = "SELECT nom FROM personnel WHERE specialite = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            preparedStatement.setString(1, "Sage-femme");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rs = null;
        try {
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Ajoutez les noms des médecins au ComboBox
        List<String> Sagefemme = new ArrayList<>();
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Sagefemme.add(rs.getString("nom"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        nomSageFemmeField.setItems(FXCollections.observableArrayList(Sagefemme));




        // Utilisez un PreparedStatement pour éviter les injections SQL
        String query = "SELECT nom FROM patient";
        PreparedStatement preparedStatement = null;
        ResultSet Ss = null;

        try {
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();

            // Ajoutez les noms des patients au ComboBox
            List<String> patients = new ArrayList<>();
            while (rs.next()) {
                patients.add(rs.getString("nom"));
            }
            patientComboBox.setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (Ss != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        /**try {
         query = "SELECT nom FROM personnel WHERE specialite = ?";
         preparedStatement = connection.prepareStatement(query);
         preparedStatement.setString(1, "Infirmier");
         ResultSet rs = preparedStatement.executeQuery();
         List<String> infirmier = new ArrayList<>();
         while (rs.next()) {
         infirmier.add(rs.getString("nom"));
         }
         comboBox2.setItems(FXCollections.observableArrayList(infirmier));

         query = "SELECT nom FROM patient";
         preparedStatement = connection.prepareStatement(query);
         rs = preparedStatement.executeQuery();
         List<String> patients = new ArrayList<>();
         while (rs.next()) {
         patients.add(rs.getString("nom"));
         }
         comboBox.setItems(FXCollections.observableArrayList(patients));
         } catch (SQLException e) {
         e.printStackTrace();
         } finally {
         closeResources(preparedStatement, null);
         }**/
    }

    private void fillComboBox1() {
        ObservableList<String> sexeOptions = FXCollections.observableArrayList("Masculin", "Feminin");
        sexeBebeComboBox.setItems(sexeOptions);

        ObservableList<String> typeAccouchementOptions = FXCollections.observableArrayList("Naturel", "Césarienne");
        typeAccouchementComboBox.setItems(typeAccouchementOptions);
    }

    @FXML
    private void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clean() {
        dateAccouchementPicker.setValue(null);
        heureAccouchementField.clear();
        check1.setSelected(false);
        check2.setSelected(false);
        detailsComplicationsField.clear();
        poidsBebeField.clear();
        tailleBebeField.clear();
        patientComboBox.setValue(null);
        typeAccouchementComboBox.setValue(null);
        nomMedecinField.setValue(null);
        nomSageFemmeField.setValue(null);
        sexeBebeComboBox.setValue(null);
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
            String nomPatient = patientComboBox.getValue() != null ? patientComboBox.getValue() : "";
            String nomMedecin = nomMedecinField.getValue() != null ? nomMedecinField.getValue() : "";
            String nomSageFemme = nomSageFemmeField.getValue() != null ? nomSageFemmeField.getValue() : "";

            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);

            heureAccouchementField.setText(formattedTime);

            java.sql.Date dateAccouchement = null;
            if (dateAccouchementPicker.getValue() != null) {
                LocalDate localDate = dateAccouchementPicker.getValue();
                dateAccouchement = java.sql.Date.valueOf(localDate);
            }
            String typeAccouchement = typeAccouchementComboBox.getValue();
            boolean complications = check1.isSelected();  // Si "Oui" est sélectionné, complications = true
            String detailsComplications = detailsComplicationsField.getText();
            String poidsBebe = poidsBebeField.getText();
            String tailleBebe = tailleBebeField.getText();
            String sexeBebe = sexeBebeComboBox.getValue();

            if (typeAccouchement == null || nomPatient.isEmpty() || dateAccouchement == null ||  detailsComplications.isEmpty() || poidsBebe.isEmpty() || tailleBebe.isEmpty() || sexeBebe.isEmpty() || nomMedecin.isEmpty() || nomSageFemme.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
            } else {
                showConfirmationDialog();
            }
        }

    private void logActivity(String nomPersonnel, String service, String typeActivite, LocalDate date, String heure) {
        String query = "INSERT INTO activites_personnel (nom_personnel, service, type_activite, date_activite, heure_activite) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, nomPersonnel);
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
            query = "INSERT INTO `accouchements`(`Patient_ID`, `Date_accouchement`, `Heure_accouchement`, `Type_accouchement`, `Complications`, `Details_complications`, `Nom_medecin`, `Nom_sage_femme`, `Poids_bebe`, `Taille_bebe`, `Sexe_bebe`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        } else {
            query = "UPDATE `Accouchements` SET "
                    + "`Date_accouchement`=?,"
                    + "`Heure_accouchement`=?,"
                    + "`Type_accouchement`=?,"
                    + "`Complications`=?,"
                    + "`Details_complications`=?,"
                    + "`Nom_medecin`=?,"
                    + "`Nom_sage_femme`=?,"
                    + "`Poids_bebe`=?,"
                    + "`Taille_bebe`=?,"
                    + "`Sexe_bebe`=? WHERE ID = '" + accouchementId + "'";
        }
    }

    private void insert() {

        String nomPatient = patientComboBox.getValue();

        try {
            // Recherche de l'ID du patient en fonction de son nom
            String queryIdPatient = "SELECT id_patient FROM patient WHERE nom = ?";
            PreparedStatement preparedStatementIdPatient = connection.prepareStatement(queryIdPatient);
            preparedStatementIdPatient.setString(1, nomPatient);
            ResultSet resultSetIdPatient = preparedStatementIdPatient.executeQuery();

            int idPatient = -1; // Valeur par défaut si aucun ID n'est trouvé
            if (resultSetIdPatient.next()) {
                idPatient = resultSetIdPatient.getInt("id_patient");

                // Insérer l'ID du patient dans la table "accouchements"
                String insertQuery = "INSERT INTO accouchements (Patient_ID, Date_accouchement, Heure_accouchement, Type_accouchement, Complications, Details_complications, Nom_medecin, Nom_sage_femme, Poids_bebe, Taille_bebe, Sexe_bebe) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // Vérifier que les champs poidsBebeField et tailleBebeField contiennent des chiffres décimaux valides
                try {
                    BigDecimal poidsBebe = new BigDecimal(poidsBebeField.getText());
                    BigDecimal tailleBebe = new BigDecimal(tailleBebeField.getText());

                    // Utiliser les valeurs poidsBebe et tailleBebe dans votre PreparedStatement
                    // ...

                } catch (NumberFormatException e) {
                    // Gérer l'exception si les champs ne contiennent pas des chiffres décimaux valides
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("Erreur de format");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Les champs Poids bébé et Taille bébé doivent être des nombres décimaux valides.");
                    dialog.showAndWait();
                    return; // Arrêter l'exécution de la méthode insert en cas d'erreur
                }

                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, idPatient);
                insertStatement.setDate(2, Date.valueOf(dateAccouchementPicker.getValue()));
                insertStatement.setString(3, heureAccouchementField.getText());
                insertStatement.setString(4, typeAccouchementComboBox.getValue());
                insertStatement.setBoolean(5, check1.isSelected());
                insertStatement.setString(6, detailsComplicationsField.getText());
                insertStatement.setString(7, nomMedecinField.getValue());
                insertStatement.setString(8, nomSageFemmeField.getValue());
                insertStatement.setBigDecimal(9, new BigDecimal(poidsBebeField.getText()));
                insertStatement.setBigDecimal(10, new BigDecimal(tailleBebeField.getText()));
                insertStatement.setString(11, sexeBebeComboBox.getValue());

                insertStatement.executeUpdate();


                String nomMedecin = nomMedecinField.getValue();
                String nomSageFemme = nomSageFemmeField.getValue();
                String nomPersonnel = nomMedecin + " et " + nomSageFemme;

                logActivity(nomPersonnel, "Accouchement", "Accouchement de type: " + typeAccouchementComboBox.getValue(), dateAccouchementPicker.getValue(), heureAccouchementField.getText());


                // Afficher un message de confirmation
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText(null);
                dialog.setContentText("Accouchement enregistré avec succès.");
                dialog.showAndWait();
            } else {
                // Gérer le cas où aucun patient n'est trouvé avec le nom sélectionné
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("Erreur");
                dialog.setHeaderText(null);
                dialog.setContentText("Aucun patient trouvé avec le nom sélectionné.");
                dialog.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccouchementFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void setTextField(int id, int patientId, Date dateAccouchement, Time heureAccouchement, String typeAccouchement, boolean complications, String detailsComplications, String nomMedecin, String nomSageFemme, double poidsBebe, double tailleBebe, char sexeBebe) {
        this.accouchementId = id;
        this.patientComboBox.setValue(String.valueOf(patientId));
        this.dateAccouchementPicker.setValue(dateAccouchement.toLocalDate());
        this.heureAccouchementField.setText(heureAccouchement.toString());
        this.typeAccouchementComboBox.setValue(typeAccouchement);
        this.check1.setSelected(complications);
        this.detailsComplicationsField.setText(detailsComplications);
        this.nomMedecinField.setValue(nomMedecin);
        this.nomSageFemmeField.setValue(nomSageFemme);
        this.poidsBebeField.setText(String.valueOf(poidsBebe));
        this.tailleBebeField.setText(String.valueOf(tailleBebe));
        this.sexeBebeComboBox.setValue(String.valueOf(sexeBebe));
    }

    public void setAccouchement(int id, String nomPatient) {
        // Assure-toi que les champs idnom et idnom21 sont bien les champs pour le nom du patient
        accouchementId = id;
        idnom21.setText(nomPatient);
        idnom21.setEditable(false);
    }
    /**public void setIdNomField(String nomPatient) {
        idnom21.setText(nomPatient);
    }

    public void setIdNomEditable(boolean isEditable) {
        idnom21.setEditable(isEditable);
    }

    public void setMotifField(String motif) {
        idnom2.setText(motif);
    }**/

    void setUpdate(boolean b) {
        this.update = b;
    }


}
