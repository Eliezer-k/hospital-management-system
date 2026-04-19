package kangamoyet.djadou.kouassid.applicationcentre2sante;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.sql.*;

public class Controller {
    @FXML
    private Label label5;
    @FXML
    private Label label52;
    @FXML
    private Label label522;
    @FXML
    private Label label5221;

    public void initialize() {


        int nombrePatients = recupererNombrePatients();
        label5.setText("" + nombrePatients);

        int nombrePersonnel = recupererNombrePersonnel();
        label52.setText("" + nombrePersonnel);

        int nombreConsultation = recupererNombreConsultation();
        label522.setText("" + nombreConsultation);

        int nombreRDV = recupererNombreRDV();
        label5221.setText("" + nombreRDV);


    }

    private int recupererNombrePatients() {
        // Code pour se connecter à la base de données et récupérer le nombre de patients
        // Exemple simplifié :
        int nombrePatients = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/centre2sante", "root", "");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM patient");
            if (resultSet.next()) {
                nombrePatients = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombrePatients;
    }

    private int recupererNombrePersonnel() {
        // Code pour se connecter à la base de données et récupérer le nombre de patients
        // Exemple simplifié :
        int nombrePersonnel = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/centre2sante", "root", "");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM personnel");
            if (resultSet.next()) {
                nombrePersonnel = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombrePersonnel;
    }

    private int recupererNombreConsultation() {
        // Code pour se connecter à la base de données et récupérer le nombre de patients
        // Exemple simplifié :
        int nombreConsultation = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/centre2sante", "root", "");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM resultat");
            if (resultSet.next()) {
                nombreConsultation = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreConsultation;
    }

    private int recupererNombreRDV() {
        // Code pour se connecter à la base de données et récupérer le nombre de patients
        // Exemple simplifié :
        int nombreRDV = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/centre2sante", "root", "");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM rendez_vous");
            if (resultSet.next()) {
                nombreRDV = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreRDV;
    }
}