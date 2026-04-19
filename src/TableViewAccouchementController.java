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
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Accouchement;
import models.Patients;
import models.Personnel;
import models.Resultat;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TableViewAccouchementController implements Initializable {

    @FXML
    private TableView<Accouchement> accouchementTable;
    @FXML
    private TableColumn<Accouchement, String> idCol;
    @FXML
    private TableColumn<Accouchement, String> nameCol;
    @FXML
    private TableColumn<Accouchement, String> prenomCol;
    @FXML
    private TableColumn<Accouchement, String> sexeCol;
    @FXML
    private TableColumn<Accouchement, String> SpecialiteCol;
    @FXML
    private TableColumn<Accouchement, String> editCol;
    @FXML
    private TableColumn<Accouchement, String> editCol1;
    @FXML
    private TableColumn<Accouchement, String> editCol2;
    @FXML
    private TableColumn<Accouchement, String> editCol21;
    @FXML
    private TableColumn<Accouchement, String> editCol211;
    @FXML
    private TableColumn<Accouchement, String> editCol212;
    @FXML
    private TableColumn<Accouchement, String> editCol2121;
    @FXML
    private TableColumn<Accouchement, String> editCol2122;
    @FXML
    private JFXButton addUserButton;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private JFXButton printButton;
    @FXML
    private TextField searchField;


    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Patients personnel = null ;

    ObservableList<Accouchement> accouchementList = FXCollections.observableArrayList();


    @Override
    public void initialize (URL url, ResourceBundle rb) {
        // TODO
        loadDate();
        addUserButton.setOnMouseClicked(event -> getAddView());
        refreshButton.setOnMouseClicked(event -> refreshTable());
        printButton.setOnMouseClicked(event -> print());

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterResults(newValue);
            }
        });
    }

    private void filterResults(java.lang.String searchText) {
        // Filtrer les résultats en fonction du texte de recherche
        ObservableList<Accouchement> filteredList = accouchementList.stream()
                .filter(resultat -> resultat.getTypeAccouchement().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList));
        accouchementTable.setItems(filteredList);
    }
    /**@FXML
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
            Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(printButton.getScene().getWindow())) {
                boolean success = job.printPage(printButton);
                if (success) {
                    job.endJob();
                }
            }
        }
    }
    private String getPatientName(int id) {
        String patientName = "";
        try {
            String query = "SELECT nom FROM patient WHERE id_patient = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                patientName = resultSet.getString("nom");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableViewAccouchementController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patientName;
    }
    private String getDefaultMotif() {
        return "Accouchement";
    }



    @FXML
    private void refreshTable() {
        try {
            accouchementList.clear();

            query = "SELECT a.ID, a.Patient_ID, p.nom AS nomPatient, a.Date_accouchement, a.Heure_accouchement, " +
                    "a.Type_accouchement, a.Complications, a.Details_complications, " +
                    "a.Nom_medecin, a.Nom_sage_femme, a.Poids_bebe, a.Taille_bebe, a.Sexe_bebe " +
                    "FROM accouchements a " +
                    "JOIN patient p ON a.Patient_ID = p.id_patient";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accouchementList.add(new Accouchement(
                        resultSet.getInt("id"),
                        resultSet.getInt("Patient_ID"),
                        resultSet.getString("nomPatient"),
                        resultSet.getDate("Date_accouchement"),
                        resultSet.getTime("Heure_accouchement"),
                        resultSet.getString("Type_accouchement"),
                        resultSet.getBoolean("Complications"),
                        resultSet.getString("Details_complications"),
                        resultSet.getString("Nom_medecin"),
                        resultSet.getString("Nom_sage_femme"),
                        resultSet.getDouble("Poids_bebe"),
                        resultSet.getDouble("Taille_bebe"),
                        resultSet.getString("Sexe_bebe").charAt(0)));
                accouchementTable.setItems(accouchementList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    private void loadDate() {

        connection = MySQLConnector.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("heureAccouchement"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("dateAccouchement"));
        SpecialiteCol.setCellValueFactory(new PropertyValueFactory<>("typeAccouchement"));
        editCol.setCellValueFactory(new PropertyValueFactory<>("complications"));
        editCol1.setCellValueFactory(new PropertyValueFactory<>("detailsComplications"));
        editCol2.setCellValueFactory(new PropertyValueFactory<>("nomMedecin"));
        editCol21.setCellValueFactory(new PropertyValueFactory<>("nomSageFemme"));
        editCol211.setCellValueFactory(new PropertyValueFactory<>("poidsBebe"));
        editCol212.setCellValueFactory(new PropertyValueFactory<>("tailleBebe"));
        editCol2121.setCellValueFactory(new PropertyValueFactory<>("sexeBebe"));

        // add cell of button edit
        Callback<TableColumn<Accouchement, String>, TableCell<Accouchement, String>> cellFactory = (TableColumn<Accouchement, String> param) -> {
            // make cell containing buttons
            final TableCell<Accouchement, String> cell = new TableCell<Accouchement, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    // that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button deleteBtn = new Button("Supprimer");
                        Button editBtn = new Button("Modifier");
                        Button constant = new Button("Hospitalisé");

                        deleteBtn.setOnMouseClicked(event -> {

                            try {
                                // delete operation
                                Accouchement accouchement = getTableView().getItems().get(getIndex());
                                String query = "DELETE FROM `accouchements` WHERE id =" + accouchement.getId();
                                Connection connection = MySQLConnector.getConnect();
                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                            } catch (SQLException ex) {
                                Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewAccouchementController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        editBtn.setOnMouseClicked(event -> {
                            Accouchement accouchement = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterAccouchement.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewAccouchementController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            AccouchementFormController accouchementController = (AccouchementFormController) loader.getController();
                            accouchementController.setUpdate(true);
                            accouchementController.setTextField(accouchement.getId(), accouchement.getPatientId(), accouchement.getDateAccouchement(),
                                    accouchement.getHeureAccouchement(), accouchement.getTypeAccouchement(), accouchement.isComplications(),
                                    accouchement.getDetailsComplications(), accouchement.getNomMedecin(), accouchement.getNomSageFemme(),
                                    accouchement.getPoidsBebe(), accouchement.getTailleBebe(), accouchement.getSexeBebe());


                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.show();
                        });

                        constant.setOnMouseClicked(event -> {

                            Accouchement accouchement= getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterHospitalisation.fxml"));

                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewAccouchementController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            HospitalisationController hospitalisationC = loader.getController();
                            hospitalisationC.setPatient(accouchement.getPatientId(), accouchement.getNomPatient());

                            hospitalisationC.initializeForm();

                                /** Remplir le champ idnom avec le nom du patient et le rendre non modifiable
                                accouchementController.setIdNomField(accouchement.getNomPatient());
                                accouchementController.setIdNomEditable(false);
                                // Remplir le champ motif avec la valeur par défaut "Accouchement"
                                accouchementController.setMotifField("Accouchement");**/


                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.show();
                            /** Fermer l'interface actuelle
                             ((Node)(event.getSource())).getScene().getWindow().hide();**/
                        });



                        HBox manageBtn = new HBox(editBtn, deleteBtn, constant);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editBtn, new Insets(2, 3, 0, 2));
                        HBox.setMargin(constant, new Insets(2, 2, 0, 3));

                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };

        editCol2122.setCellFactory(cellFactory);
        accouchementTable.setItems(accouchementList);
    }


}
