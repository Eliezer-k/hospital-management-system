package kangamoyet.djadou.kouassid.applicationcentre2sante;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HelloController{
    @FXML
    private Label welcomeText;
    @FXML
    private Button buttonconnecter;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private JFXButton Buttondeco;
    @FXML
    private JFXButton ButtonPatient;
    @FXML
    private JFXButton ButtonPersonnel;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private JFXButton buttonCon;
    @FXML
    private Label heureLabel;
    @FXML
    private JFXButton ButtonAccueil;
    @FXML
    private JFXButton Button1;
    @FXML
    private JFXButton buttonH;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void initialize() {

        updateTime();

        // Planifier une tâche pour mettre à jour l'heure chaque seconde
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        afficherImageParDefaut(null);
    }
    @FXML
    private void getAddView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("InterfaceInjection.fxml"));

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
    private void getAddView1() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("prelevementInter.fxml"));

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
    private void methode(){
        showAlert(Alert.AlertType.INFORMATION, "Information", "Ce service n'est pas disponible pour le moment.");
    }

    @FXML
    private void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void afficherImageParDefaut(javafx.scene.input.MouseEvent mouseEvent){
        try {
            // Charger le fichier FXML du contenu par défaut
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            if (scrollpane != null) {
                scrollpane.setContent(root);
            }else {
            System.out.println("Ok");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**@FXML
    private void afficherImage(javafx.scene.input.MouseEvent mouseEvent){
        try {

            // Charger le fichier FXML du contenu par défaut
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceInjection.fxml"));
            Parent root = loader.load();
            if (scrollpane != null) {
                scrollpane.setContent(root);
            }else {
                System.out.println("Ok");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }**/


    private void updateTime() {

        if (heureLabel != null) {
            try {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                heureLabel.setText(formatter.format(now));
            } catch (NullPointerException e) {
                System.err.println("NullPointerException caught: " + e.getMessage());
                e.printStackTrace();
                // Ajoutez ici le code pour gérer l'exception, par exemple initialiser heureLabel si nécessaire
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    private void setButtonconnecter(javafx.scene.input.MouseEvent mouseEvent) {
        String utilisateur = username.getText();
        String mot2passe = password.getText();
        // Vérifiez les informations d'identification ici
        if ("admin".equals(utilisateur) && "root".equals(mot2passe)) {
            // Si les informations d'identification sont correctes, affichez l'interface principale
            try {
                // Fermer l'interface de connexion actuelle
                Stage stage = (Stage) username.getScene().getWindow();
                stage.close();

                Parent parent = FXMLLoader.load(getClass().getResource("Principale.fxml"));
                Scene scene = new Scene(parent);
                scene.setFill(Color.TRANSPARENT);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.initStyle(StageStyle.TRANSPARENT);
                primaryStage.show();
            } catch (IOException e) {
                Logger.getLogger(HelloApplication.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if ("medecin".equals(utilisateur) && "motdepasse".equals(mot2passe)) {
            // Si c'est un utilisateur normal, affichez l'interface principale pour l'utilisateur normal
            try {
                // Fermer l'interface de connexion actuelle
                Stage stage = (Stage) username.getScene().getWindow();
                stage.close();

                Parent parent = FXMLLoader.load(getClass().getResource("Principale2.fxml"));
                Scene scene = new Scene(parent);
                scene.setFill(Color.TRANSPARENT);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.initStyle(StageStyle.TRANSPARENT);
                primaryStage.show();
            } catch (IOException e) {
                Logger.getLogger(HelloApplication.class.getName()).log(Level.SEVERE, null, e);
            }
        } else {
            if (utilisateur.isEmpty() || mot2passe.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Attention", "Veuillez remplir tous les champs.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Attention", "Nom d'utilisateur ou mot de passe incorect.");
            }
        }
    }
    @FXML
    private void okokok(javafx.scene.input.MouseEvent mouseEvent) {
        afficherImageParDefaut(mouseEvent);
    }

    @FXML
    private void handleDeconnexionButtonAction(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir vous déconnecté ?");
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) Buttondeco.getScene().getWindow();
            stage.close();
            Parent parent =  FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.show();
        }

    }
    @FXML
    private void clickButtonPatient(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickButtonPatient1(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableView4.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickButtonPatient11(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableViewAccouchement.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void RDV(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Rendez-vous.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void showSuiviPatient(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SuiviPatient.fxml"));
            Parent root = loader.load();
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void Hospitalisation(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Hospitalisation.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickButtonServiceIn(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Servives infirmier.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void clickButtonActivite(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActivitésTableView.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void clickButtonBilan(javafx.scene.input.MouseEvent mouseEvent){

        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StackBarChat.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickButtonInjection(javafx.scene.input.MouseEvent mouseEvent){
        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceInjection.fxml"));
            Parent root = loader.load();
            // Mettre le contenu du fichier FXML dans le ScrollPane
            if (scrollpane != null) {
                scrollpane.setContent(root);
            } else {
                System.out.println("ScrollPane est null !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**@FXML
    private void okokokok(javafx.scene.input.MouseEvent mouseEvent) {
        afficherImage(mouseEvent);
    }**/

    @FXML
    public void clickButtonPersonnel(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Patients.fxml"));
            Parent root = loader.load();
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void clickButtonConsultation(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            // Charge le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableViewConsultation.fxml"));
            Parent root = loader.load();
            scrollpane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /** ////////////////////////////////////////////////////////////////////////////////////////////////////////////**/

}