package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Patients;
import models.Resultat;
import models.rendezVous;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



public class TableViewRDV implements Initializable {

        @FXML
        private TableView<rendezVous> studentsTable;
        @FXML
        private TableColumn<rendezVous, String> idCol;
        @FXML
        private TableColumn<rendezVous, String> nameCol;
        @FXML
        private TableColumn<rendezVous, String> prenomCol;
        @FXML
        private TableColumn<rendezVous, String> SpecialiteCol;
        @FXML
        private TableColumn<rendezVous, String> sexeCol;
        @FXML
        private TableColumn<rendezVous, String> editCol;
        @FXML
        private Pane iconPane;
        @FXML
        private Button Buttondeco;
        @FXML
        private JFXButton refreshButton;
        @FXML
        private ScrollPane scrollpane;
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
        private JFXButton closeButton;


        String query = null;
        Connection connection = null ;
        PreparedStatement preparedStatement = null ;
        ResultSet resultSet = null ;
        rendezVous rdv = null ;
        private boolean update = false;
        private int patientId;

        ObservableList<rendezVous> RDVList = FXCollections.observableArrayList();


        /**
         * Initializes the controller class.
         */
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            // TODO
            loadDate();
            refreshButton.setOnMouseClicked(event -> refreshTable());


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
        /**@FXML
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
        }**/

        @FXML
        private void handleDeconnexionButtonAction(javafx.scene.input.MouseEvent mouseEvent) throws IOException {

            try {
                // Charge le fichier FXML de la nouvelle interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Principale.fxml"));
                Parent root = loader.load();
                // Crée une nouvelle scène avec la nouvelle interface
                Scene scene = new Scene(root);
                // Obtient la fenêtre actuelle à partir du bouton
                Stage stage = (Stage) Buttondeco.getScene().getWindow();
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
        private void getAddView() {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("AjouterPatients.fxml"));
                Scene scene = new Scene(parent);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @FXML
        private void refreshTable() {
            System.out.println("Refresh button clicked!"); // Message de débogage
            try {
                RDVList.clear();

                query = "SELECT * FROM `rendez_vous`";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                    RDVList.add(new rendezVous(
                            resultSet.getInt("id"),
                            resultSet.getString("nom"),
                            resultSet.getString("date_rdv"),
                            resultSet.getString("heure_rdv"),
                            resultSet.getString("motif")));
                    // Définir les éléments de la TableView après avoir ajouté tous les rendez-vous
                    studentsTable.setItems(RDVList);
                }

            } catch (SQLException ex) {
                Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        private void loadDate() {

            connection = MySQLConnector.getConnect();
            refreshTable();

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            SpecialiteCol.setCellValueFactory(new PropertyValueFactory<>("heure"));
            sexeCol.setCellValueFactory(new PropertyValueFactory<>("motif"));


            // add cell of button edit
            Callback<TableColumn<rendezVous, String>, TableCell<rendezVous, String>> cellFoctory = (TableColumn<rendezVous, String> param) -> {
                // make cell containing buttons
                final TableCell<rendezVous, String> cell =  new TableCell<rendezVous, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Button deleteBtn = new Button("Supprimer");
                            Button editBtn = new Button("Modifier");
                            Button printBtn = new Button("Imprimer");

                            deleteBtn.setOnMouseClicked(event -> {

                                try {
                                    // delete operation
                                    rendezVous rdv = getTableView().getItems().get(getIndex());
                                    String query = "DELETE FROM `rendez_vous` WHERE id =" +rdv.getId();
                                    Connection connection = MySQLConnector.getConnect();
                                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                } catch (SQLException ex) {
                                    Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });

                             editBtn.setOnMouseClicked(event -> {
                                rendezVous rdv = getTableView().getItems().get(getIndex());
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterRDV.fxml"));
                                try {
                                    loader.load();
                                } catch (IOException ex) {
                                    Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                AjouterRDVcontroller AjouterRDV = loader.getController();
                                AjouterRDV.setUpdate(true);
                                AjouterRDV.setTextField(rdv.getId(), rdv.getNom(), rdv.getDate(), rdv.getHeure(), rdv.getMotif());

                                Parent parent = loader.getRoot();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.show();
                            });

                            printBtn.setOnAction(event -> {
                                PrinterJob job = PrinterJob.createPrinterJob();
                                if (job != null && job.showPrintDialog(studentsTable.getScene().getWindow())) {
                                    for (rendezVous resultat : studentsTable.getItems()) {
                                        Node content = createPrintContent(resultat);
                                        boolean success = job.printPage(content);
                                        if (!success) {
                                            // Gestion de l'échec de l'impression
                                            System.out.println("Erreur d'impression");
                                        }
                                    }
                                    job.endJob();
                                }
                            });

                            HBox manageBtn = new HBox(editBtn, deleteBtn, printBtn);
                            manageBtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editBtn, new Insets(2, 3, 0, 2));
                            HBox.setMargin(printBtn, new Insets(2, 3, 0, 2));

                            setGraphic(manageBtn);
                            setText(null);
                        }
                    }
                };

                return cell;
            };
            editCol.setCellFactory(cellFoctory);
            studentsTable.setItems(RDVList);

        }

        private Node createPrintContent(rendezVous rdv) {
            VBox content = new VBox();
            content.setSpacing(10);
            content.setPadding(new Insets(20));

            // Ajouter les détails du résultat à imprimer
            Label titleLabel = new Label("Résultat");
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
            Label idLabel = new Label("ID : " + rdv.getId());
            Label nomPatientLabel = new Label("Nom : " + rdv.getNom());
            Label nomMedecinLabel = new Label("Date : " + rdv.getDate());
            Label dateConsultationLabel = new Label("Heure : " + rdv.getHeure());
            Label dateConsultationLabel1 = new Label("Motif : " + rdv.getMotif());
            Label heureLabel = new Label("Heure de l'impression : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            content.getChildren().addAll(titleLabel, idLabel, nomPatientLabel, nomMedecinLabel, dateConsultationLabel,dateConsultationLabel1, heureLabel);


            return content;
        }

}
