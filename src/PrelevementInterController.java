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


public class PrelevementInterController implements Initializable {

    @FXML
    private DatePicker iddate;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ComboBox<TypePrelevement> comboBox1;
    @FXML
    private ComboBox<String> comboBox2;
    @FXML
    private TextField idheure;
    @FXML
    private TextArea text;
    @FXML
    private TextArea text1;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton button1;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int prelevementId;
    private boolean important = false; // Nouvel attribut

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fillComboBox1();

        connection = MySQLConnector.getConnect();

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

    enum TypePrelevement {
        SANGUIN("Sanguin"),
        URINAIRE("Urinaire"),
        SALIVAIRE("Salivaire"),
        AUTRE("Autre");

        private final String label;

        TypePrelevement(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static TypePrelevement fromLabel(String label) {
            for (TypePrelevement type : TypePrelevement.values()) {
                if (type.label.equalsIgnoreCase(label)) {
                    return type;
                }
            }
            return null;
        }
    }

    private void fillComboBox1() {
        ObservableList<TypePrelevement> items = FXCollections.observableArrayList(TypePrelevement.values());
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
        text1.clear();
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

        idheure.setText(formattedTime);

        java.sql.Date datePrelevement = null;
        if (iddate.getValue() != null) {
            LocalDate localDate = iddate.getValue();
            datePrelevement = java.sql.Date.valueOf(localDate);
        }
        TypePrelevement typePrelevement = comboBox1.getValue();
        String remarques = text.getText();
        String remarquess = text1.getText();
        String infirmier = comboBox2.getValue() != null ? comboBox2.getValue() : "";

        if (typePrelevement == null || nom.isEmpty() || datePrelevement == null || remarques.isEmpty() ||  remarquess.isEmpty() || infirmier.isEmpty()) {
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
            query = "INSERT INTO `prelevement_sanguin`(`nom_patient`, `heure_prelevement`, `date_prelevement`, `type_prelevement`, `resultat_prelevement`, `commentaire`, `nom_infirmier`) VALUES (?,?,?,?,?,?,?)";
        } else {
            query = "UPDATE `prelevement_sanguin` SET "
                    + "`heure_prelevement`=?,"
                    + "`date_prelevement`=?,"
                    + "`type_prelevement`=?,"
                    + "`resultat_prelevement`=?,"
                    + "`commentaire`=?,"
                    + "`nom_infirmier`=? WHERE id_prelevement = '" + prelevementId + "'";
        }
    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, comboBox.getValue());
            preparedStatement.setString(2, idheure.getText());
            preparedStatement.setDate(3, java.sql.Date.valueOf(iddate.getValue()));
            preparedStatement.setString(4, String.valueOf(comboBox1.getValue()));
            preparedStatement.setString(5, text.getText());
            preparedStatement.setString(6, text1.getText());
            preparedStatement.setString(7, comboBox2.getValue());

            preparedStatement.execute();

            logActivity(comboBox2.getValue(), "Prélèvement", "Type de prélèvement: " + comboBox1.getValue(), iddate.getValue(), LocalTime.parse(idheure.getText()));


            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText(null);
            dialog.setContentText("Prélèvement sanguin enregistré avec succès.");
            dialog.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(InjectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
