package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.sun.prism.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
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
import java.util.stream.Collectors;

public class TableView3Controller implements Initializable {
    @FXML
    private TableView<Resultat> studentsTable;
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
    private TableColumn<Resultat, String> editCol;
    @FXML
    private TextField searchField;
    @FXML
    private Button Buttondeco;
    @FXML
    private Button button2;



    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Patients personnel = null ;

    ObservableList<Resultat> resultList = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDate();
        // Ajoutez un listener pour détecter les changements dans le champ de recherche
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterResults(newValue);
            }
        });
        /**Image iiimage = new Image(getClass().getResourceAsStream("/Icone/accueil.png"));
        Button Buttondeco= new Button();
        Buttondeco.setGraphic(new ImageView(iiimage));
        iconePane11.getChildren().add(Buttondeco);**/
    }
    private void filterResults(java.lang.String searchText) {
        // Filtrer les résultats en fonction du texte de recherche
        ObservableList<Resultat> filteredList = resultList.stream()
                .filter(resultat -> resultat.getNom_patient().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList));
        studentsTable.setItems(filteredList);
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

    private void refreshTable() {
        try {
            resultList.clear();

            query = "SELECT * FROM `resultat`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                resultList.add(new Resultat(
                        resultSet.getInt("id_resultat"),
                        resultSet.getString("nom_patient"),
                        resultSet.getString("nom_medecin"),
                        resultSet.getDate("date_consultation").toLocalDate(),
                        resultSet.getString("traitement_requis")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void loadDate() {

        connection = MySQLConnector.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id_resultat"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nom_patient"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("nom_medecin"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("date_consultation"));
        SpecialiteCol.setCellValueFactory(new PropertyValueFactory<>("traitement_requis"));

        Callback<TableColumn<Resultat, String>, TableCell<Resultat, String>> cellFoctory = (TableColumn<Resultat, String> param) -> {
            final TableCell<Resultat, String> cell = new TableCell<Resultat, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button printBtn = new Button("Imprimer");

                        printBtn.setOnAction(event -> {
                            PrinterJob job = PrinterJob.createPrinterJob();
                            if (job != null && job.showPrintDialog(studentsTable.getScene().getWindow())) {
                                for (Resultat resultat : studentsTable.getItems()) {
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


                        HBox manageBtn = new HBox(printBtn);
                        manageBtn.setStyle("-fx-alignment:center");
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        editCol.setCellFactory(cellFoctory);
        studentsTable.setItems(resultList);

    }
    private Node createPrintContent(Resultat resultat) {
        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(20));

        // Ajouter les détails du résultat à imprimer
        Label titleLabel = new Label("Résultat");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        Label idLabel = new Label("ID : " + resultat.getId_resultat());
        Label nomPatientLabel = new Label("Nom du patient : " + resultat.getNom_patient());
        Label nomMedecinLabel = new Label("Nom du médecin : " + resultat.getNom_medecin());
        Label dateConsultationLabel = new Label("Date de consultation : " + resultat.getDate_consultation());
        Label traitementLabel = new Label("Traitement requis : " + resultat.getTraitement_requis());
        Label heureLabel = new Label("Heure de l'impression : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        content.getChildren().addAll(titleLabel, idLabel, nomPatientLabel, nomMedecinLabel, dateConsultationLabel, traitementLabel, heureLabel);


        return content;
    }

}
