package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InjectionController implements Initializable {

    @FXML
    private DatePicker iddate;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ComboBox<String> comboBox1;
    @FXML
    private ComboBox<String> comboBox2;
    @FXML
    private TextField idheure;
    @FXML
    private TextArea text;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton button1;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int injectionId;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fillComboBox1();

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
            preparedStatement.setString(1, "Infirmier");
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
        List<String> Infirmier = new ArrayList<>();
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Infirmier.add(rs.getString("nom"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        comboBox2.setItems(FXCollections.observableArrayList(Infirmier));


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
            comboBox.setItems(FXCollections.observableArrayList(patients));
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




    }

    private void fillComboBox1() {
        ObservableList<String> items = FXCollections.observableArrayList("Médicament", "Vaccin", "Autre");
        comboBox1.setItems(items);
    }

    @FXML
    private void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clean() {
        iddate.setValue(null);
        idheure.clear();
        text.clear();
        comboBox.setValue(null);
        comboBox1.setValue(null);
        comboBox2.setValue(null);

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
        String nom = comboBox.getValue() != null ? comboBox.getValue() : "";

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(timeFormatter);

        // Affichez l'heure actuelle dans le champ de texte idheure
        idheure.setText(formattedTime);

        java.sql.Date dateNaissance = null;
        if (iddate.getValue() != null) {
            LocalDate localDate = iddate.getValue();
            dateNaissance = java.sql.Date.valueOf(localDate);
        }
        String medecin = comboBox1.getValue() != null ? comboBox1.getValue() : "";
        String symptomes = text.getText();
        String examen = comboBox2.getValue() != null ? comboBox2.getValue() : "";

        if (nom.isEmpty() || dateNaissance == null || symptomes.isEmpty() || examen.isEmpty() || medecin.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
        } else {
            showConfirmationDialog();
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
            query = "INSERT INTO `injections`(`nom_patient`, `heure`, `date`, `type_injection`, `remarques`, `infirmier_en_charge`) VALUES (?,?,?,?,?,?)";
        } else {
            query = "UPDATE `injections` SET "
                    + "`heure`=?,"
                    + "`date`=?,"
                    + "`type_injection`=?,"
                    + "`remarques`=?,"
                    + "`infirmier_en_charge`= ? WHERE id_injection = '" + injectionId + "'";
        }
    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, comboBox.getValue());
            preparedStatement.setString(2, idheure.getText());
            preparedStatement.setString(3, String.valueOf(iddate.getValue()));
            preparedStatement.setString(4, comboBox1.getValue());
            preparedStatement.setString(5, text.getText());
            preparedStatement.setString(6, comboBox2.getValue());

            preparedStatement.execute();

            logActivity(comboBox2.getValue(), "Injection", "Injection de type: " + comboBox1.getValue(), iddate.getValue(), LocalTime.parse(idheure.getText()));

            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText(null);
            dialog.setContentText("Injection enregistrée avec succès.");
            dialog.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(InjectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
