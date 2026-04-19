package kangamoyet.djadou.kouassid.applicationcentre2sante;


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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Patients;


import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableViewController implements Initializable {

    @FXML
    private TableView<Patients> studentsTable;
    @FXML
    private TableColumn<Patients, String> idCol;
    @FXML
    private TableColumn<Patients, String> nameCol;
    @FXML
    private TableColumn<Patients, String> prenomCol;
    @FXML
    private TableColumn<Patients, String> birthCol;
    @FXML
    private TableColumn<Patients, String> sexeCol;
    @FXML
    private TableColumn<Patients, String> telephoneCol;
    @FXML
    private TableColumn<Patients, String> NumeroCol;
    @FXML
    private TableColumn<Patients, String> editCol;
    @FXML
    private Pane iconPane;
    @FXML
    private Button Buttondeco;
    @FXML
    private Button addUserButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button printButton;
    @FXML
    private ScrollPane scrollpane;


    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Patients patients = null ;

    ObservableList<Patients> StudentList = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDate();
        addUserButton.setOnMouseClicked(event -> getAddView1());
        refreshButton.setOnMouseClicked(event -> refreshTable());
        printButton.setOnMouseClicked(event -> print());

         /**Image image = new Image(getClass().getResourceAsStream("/Icone/ajout-dutilisateur (1).png"));
         Button addUserButton = new Button();
         addUserButton.setGraphic(new ImageView(image));
         iconePane.getChildren().add(addUserButton);


        Image iimage = new Image(getClass().getResourceAsStream("/Icone/rafraichir (1).png"));
        Button refreshButton = new Button();
        refreshButton.setGraphic(new ImageView(iimage));
        iconePane1.getChildren().add(refreshButton);

        Image iiimage = new Image(getClass().getResourceAsStream("/Icone/impression (2).png"));
        Button printButton = new Button();
        printButton.setGraphic(new ImageView(iiimage));
        iconePane2.getChildren().add(printButton);


        Image iiiimage = new Image(getClass().getResourceAsStream("/Icone/retour.png"));
        Button Buttondeco= new Button();
        Buttondeco.setGraphic(new ImageView(iiiimage));
        iconePane21.getChildren().add(Buttondeco);**/



        /**FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon.setOnMouseClicked(event -> {
            close();
        });
        closeIcon.setSize("20");
        closeIcon.getStyleClass().add("btn");
        iconPane.getChildren().add(closeIcon);




        FontAwesomeIconView userPlusIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
        userPlusIcon.setFill(javafx.scene.paint.Color.web("#2196f3"));
        userPlusIcon.setOnMouseClicked(event -> {
            getAddView();
        });
        userPlusIcon.setSize("45");
        userPlusIcon.getStyleClass().add("btn");

        // Crée une marge à droite pour l'icône
        HBox.setMargin(userPlusIcon, new javafx.geometry.Insets(0, 0, 0, 10));
        iconPane.getChildren().add(userPlusIcon);



        FontAwesomeIconView refreshIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH);
        refreshIcon.setFill(javafx.scene.paint.Color.web("#00e676"));
        refreshIcon.setOnMouseClicked(event -> refreshTable());
        refreshIcon.setSize("45");
        refreshIcon.getStyleClass().add("btn");

        // Crée une marge à droite pour l'icône
        HBox.setMargin(refreshIcon, new Insets(0, 0, 0, 10));
        iconPane.getChildren().add(refreshIcon);



        FontAwesomeIconView printIcon = new FontAwesomeIconView(FontAwesomeIcon.PRINT);
        printIcon.setFill(javafx.scene.paint.Color.web("#455a64"));
        printIcon.setOnMouseClicked(event -> {
            print();
        });
        printIcon.setSize("45");
        printIcon.getStyleClass().add("btn");

        // Crée une marge à droite pour l'icône
        HBox.setMargin(printIcon, new Insets(0, 0, 0, 10));

        iconPane.getChildren().add(printIcon);**/

    }
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
    private void getAddView1() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("AjouterPatients.fxml"));
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void refreshTable() {
        try {
            StudentList.clear();

            query = "SELECT * FROM `patient`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                StudentList.add(new  Patients(
                        resultSet.getInt("id_patient"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getDate("date_naissance"),
                        resultSet.getString("sexe"),
                        resultSet.getString("telephone"),
                        resultSet.getString("Numero")));
                studentsTable.setItems(StudentList);

            }

        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(iconPane.getScene().getWindow())) {
                boolean success = job.printPage(iconPane);
                if (success) {
                    job.endJob();
                }
            }
        }
    }

    private void loadDate() {

        connection = MySQLConnector.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id_patient"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("date_naissance"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        NumeroCol.setCellValueFactory(new PropertyValueFactory<>("Numero"));

        // add cell of button edit
        Callback<TableColumn<Patients, String>, TableCell<Patients, String>> cellFoctory = (TableColumn<Patients, String> param) -> {
            // make cell containing buttons
            final TableCell<Patients, String> cell = new TableCell<Patients, String>() {
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
                        Button constant = new Button("Constante");

                        deleteBtn.setOnMouseClicked(event -> {

                            try {
                                // delete operation
                                Patients patients = getTableView().getItems().get(getIndex());
                                String query = "DELETE FROM `patient` WHERE id_patient ="+patients.getId_patient();
                                Connection connection = MySQLConnector.getConnect();
                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                            } catch (SQLException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        editBtn.setOnMouseClicked(event -> {
                            Patients patients = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterPatients.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            AjouterPatientsController AjouterPatientsController = loader.getController();
                            AjouterPatientsController.setUpdate(true);
                            AjouterPatientsController.setTextField(patients.getId_patient(), patients.getNom(), patients.getPrenom(),
                                    patients.getDate_naissance().toLocalDate(), patients.getSexe(), patients.getTelephone(), patients.getNumero());

                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.show();
                        });


                        constant.setOnMouseClicked(event -> {

                            Patients patients = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Constante.fxml"));

                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            ConstanteController constanteController = loader.getController();
                            constanteController.setPatient(patients.getId_patient(), patients.getNom());

                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.show();
                            /** Fermer l'interface actuelle
                             ((Node)(event.getSource())).getScene().getWindow().hide();**/
                             });


                        /**consulBtn.setOnMouseClicked(event -> {

                            Patients patients = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceConsultation.fxml"));

                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            InterfaceConsultationController consultationController = loader.getController();
                            consultationController.setPatient(patients.getId_patient(), patients.getNom());

                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.TRANSPARENT); // Utiliser StageStyle.TRANSPARENT
                            stage.show();
                            /** Fermer l'interface actuelle
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                        });**/



                        HBox manageBtn = new HBox(editBtn, deleteBtn, constant);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editBtn, new Insets(2, 3, 0, 2));
                        HBox.setMargin(constant, new Insets(2, 3, 0, 2));

                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        editCol.setCellFactory(cellFoctory);
        studentsTable.setItems(StudentList);

    }



}
