package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import com.sun.prism.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Consultation;
import models.Patients;
import models.Personnel;
import models.Resultat;

import java.awt.print.Printable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.util.logging.Level;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TableView4Controller implements Initializable {
    @FXML
    private TableView<Consultation> studentsTable;
    @FXML
    private TableColumn<Consultation, String> idCol;
    @FXML
    private TableColumn<Consultation, String> nameCol;
    @FXML
    private TableColumn<Consultation, String> prenomCol;
    @FXML
    private TableColumn<Consultation, String> sexeCol;
    @FXML
    private TableColumn<Consultation, String> SpecialiteCol;
    @FXML
    private TableColumn<Consultation, String> SpecialiteCol1;
    @FXML
    private TableColumn<Consultation, String> SpecialiteCol2;
    @FXML
    private TableColumn<Consultation, String> SpecialiteCol3;
    @FXML
    private TableColumn<Consultation, String> editCol;
    @FXML
    private Pane iconPane;
    @FXML
    private Button Buttondeco;
    @FXML
    private Button button2;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private JFXButton printButton;



    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Patients personnel = null ;

    ObservableList<Consultation> consulList = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDate();
        refreshButton.setOnMouseClicked(event -> refreshTable());
        printButton.setOnMouseClicked(event -> print());
        /**Image iiimage = new Image(getClass().getResourceAsStream("/Icone/accueil.png"));
         Button Buttondeco= new Button();
         Buttondeco.setGraphic(new ImageView(iiimage));
         iconePane11.getChildren().add(Buttondeco);**/
    }
    @FXML
    private void handleDeconnexionButtonAction(javafx.scene.input.MouseEvent mouseEvent) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Principale.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) Buttondeco.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void print() {
        if (iconPane.getScene() != null) {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                if (job.showPrintDialog(iconPane.getScene().getWindow())) {
                    boolean success = job.printPage(iconPane);
                    if (success) {
                        job.endJob();
                    }
                }
            }
        } else {
            System.out.println("La scène de iconPane est null.");
        }
        /**PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(iconPane.getScene().getWindow())) {
                boolean success = job.printPage(iconPane);
                if (success) {
                    job.endJob();
                }
            }
        } else {
        System.out.println("La scène de iconPane est null.");
        }**/
    }


    private void refreshTable() {
        try {
            consulList.clear();


            query = "SELECT c.id_patient, p.nom AS nom_patient, c.date, c.temperature, c.poids, c.pression_arterielle, c.frequence_cardiaque, c.frequence_respiratoire " +
                    "FROM constante c " +
                    "JOIN patient p ON c.id_patient = p.id_patient";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                consulList.add(new Consultation(
                        resultSet.getInt("id_patient"),
                        resultSet.getString("nom_patient"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getDouble("temperature"),
                        resultSet.getDouble("poids"),
                        resultSet.getDouble("pression_arterielle"),
                        resultSet.getInt("frequence_cardiaque"),
                        resultSet.getInt("frequence_respiratoire")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    private void loadDate() {

        connection = MySQLConnector.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nom_patient"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        SpecialiteCol.setCellValueFactory(new PropertyValueFactory<>("poids"));
        SpecialiteCol1.setCellValueFactory(new PropertyValueFactory<>("pression_arterielle"));
        SpecialiteCol2.setCellValueFactory(new PropertyValueFactory<>("frequence_cardiaque"));
        SpecialiteCol3.setCellValueFactory(new PropertyValueFactory<>("frequence_respiratoire"));

        Callback<TableColumn<Consultation, String>, TableCell<Consultation, String>> cellFoctory = (TableColumn<Consultation, String> param) -> {
            final TableCell<Consultation, String> cell = new TableCell<Consultation, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button consulBtn= new Button("Consulter");

                         consulBtn.setOnMouseClicked(event -> {

                         Consultation consultation = getTableView().getItems().get(getIndex());
                         FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceConsultation.fxml"));

                         try {
                         loader.load();
                         } catch (IOException ex) {
                         Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                         }

                         InterfaceConsultationController consultationController = loader.getController();
                         consultationController.setPatient(consultation.getId(), consultation.getNom_patient());

                         Parent parent = loader.getRoot();
                         Stage stage = new Stage();
                         stage.setScene(new Scene(parent));
                         stage.show();
                         /** Fermer l'interface actuelle
                         ((Node)(event.getSource())).getScene().getWindow().hide();**/
                         });


                        HBox manageBtn = new HBox(consulBtn);
                        manageBtn.setStyle("-fx-alignment:center");
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        editCol.setCellFactory(cellFoctory);
        studentsTable.setItems(consulList);

    }

}
