package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Patients;
import models.Personnel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TableView2Controller implements Initializable {

        @FXML
        private TableView<Personnel> studentsTable;
        @FXML
        private TableColumn<Personnel, String> idCol;
        @FXML
        private TableColumn<Personnel, String> nameCol;
        @FXML
        private TableColumn<Personnel, String> prenomCol;
        @FXML
        private TableColumn<Personnel, String> sexeCol;
        @FXML
        private TableColumn<Personnel, String> SpecialiteCol;
        @FXML
        private TableColumn<Personnel, String> editCol;
        @FXML
        private Button Buttondeco;
        @FXML
        private JFXButton addUserButton;
        @FXML
        private JFXButton refreshButton;
        @FXML
        private JFXButton printButton;
        @FXML
        private Pane iconePane;
        @FXML
        private Pane iconePane1;
        @FXML
        private Pane iconePane11;
        @FXML
        private Pane iconePane12;



        String query = null;
        Connection connection = null ;
        PreparedStatement preparedStatement = null ;
        ResultSet resultSet = null ;
        Patients personnel = null ;

        ObservableList<Personnel> StudentList = FXCollections.observableArrayList();


        /**
         * Initializes the controller class.
         */
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            // TODO
            loadDate();
            addUserButton.setOnMouseClicked(event -> getAddView());
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


            Image iiimage = new Image(getClass().getResourceAsStream("/Icone/accueil.png"));
            Button Buttondeco= new Button();
            Buttondeco.setGraphic(new ImageView(iiimage));
            iconePane11.getChildren().add(Buttondeco);

            Image iiiimage = new Image(getClass().getResourceAsStream("/Icone/impression (2).png"));
            Button printButton = new Button();
            printButton.setGraphic(new ImageView(iiiimage));
            iconePane12.getChildren().add(printButton);**/



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
        private void getAddView() {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("AjouterPersonnel.fxml"));
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

        @FXML
        private void refreshTable() {
            try {
                StudentList.clear();

                query = "SELECT * FROM `personnel`";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                    StudentList.add(new  Personnel(
                            resultSet.getInt("id_personnel"),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("sexe"),
                            resultSet.getString("specialite")));
                    studentsTable.setItems(StudentList);

                }

            } catch (SQLException ex) {
                Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }



        }


        private void loadDate() {

            connection = MySQLConnector.getConnect();
            refreshTable();

            idCol.setCellValueFactory(new PropertyValueFactory<>("id_personnel"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            sexeCol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
            SpecialiteCol.setCellValueFactory(new PropertyValueFactory<>("specialite"));

            // add cell of button edit
            Callback<TableColumn<Personnel, String>, TableCell<Personnel, String>> cellFoctory = (TableColumn<Personnel, String> param) -> {
                // make cell containing buttons
                final TableCell<Personnel, String> cell = new TableCell<Personnel, String>() {
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

                            deleteBtn.setOnMouseClicked(event -> {

                                try {
                                    // delete operation
                                    Personnel personnel = getTableView().getItems().get(getIndex());
                                    String query = "DELETE FROM `personnel` WHERE id_personnel ="+personnel.getId_personnel();
                                    Connection connection = MySQLConnector.getConnect();
                                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                } catch (SQLException ex) {
                                    Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableView2Controller.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });

                            editBtn.setOnMouseClicked(event -> {
                                Personnel personnel = getTableView().getItems().get(getIndex());
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterPersonnel.fxml"));
                                try {
                                    loader.load();
                                } catch (IOException ex) {
                                    Logger.getLogger(kangamoyet.djadou.kouassid.applicationcentre2sante.TableView2Controller.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                AjouterPersonnelController PersonnelController = (AjouterPersonnelController) loader.getController();
                                PersonnelController.setUpdate(true);
                                PersonnelController.setTextField(personnel.getId_personnel(), personnel.getNom(), personnel.getPrenom(),
                                         personnel.getSexe(), personnel.getSpecialite());

                                Parent parent = loader.getRoot();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.show();
                            });

                            HBox manageBtn = new HBox(editBtn, deleteBtn);
                            manageBtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editBtn, new Insets(2, 3, 0, 2));

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
