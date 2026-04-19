package kangamoyet.djadou.kouassid.applicationcentre2sante;

import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChartController {

    @FXML
    private StackedBarChart<String, Number> stackedBarChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    public void initialize() {
        xAxis.setLabel("Catégories");
        yAxis.setLabel("Nombre");

        loadData();
    }

    private void loadData() {
        int nombrePatients = recupererNombrePatients();
        int nombrePersonnel = recupererNombrePersonnel();
        int nombreConsultation = recupererNombreConsultation();
        int nombreRDV = recupererNombreRDV();

        XYChart.Series<String, Number> seriesPatients = new XYChart.Series<>();
        seriesPatients.setName("Patients");
        seriesPatients.getData().add(new XYChart.Data<>("Patients", nombrePatients));

        XYChart.Series<String, Number> seriesPersonnel = new XYChart.Series<>();
        seriesPersonnel.setName("Personnel");
        seriesPersonnel.getData().add(new XYChart.Data<>("Personnel", nombrePersonnel));

        XYChart.Series<String, Number> seriesConsultation = new XYChart.Series<>();
        seriesConsultation.setName("Consultations");
        seriesConsultation.getData().add(new XYChart.Data<>("Consultations", nombreConsultation));

        XYChart.Series<String, Number> seriesRDV = new XYChart.Series<>();
        seriesRDV.setName("Rendez-vous");
        seriesRDV.getData().add(new XYChart.Data<>("Rendez-vous", nombreRDV));

        stackedBarChart.getData().addAll(seriesPatients, seriesPersonnel, seriesConsultation, seriesRDV);
    }

    private int recupererNombrePatients() {
        return recupererNombre("SELECT COUNT(*) FROM patient");
    }

    private int recupererNombrePersonnel() {
        return recupererNombre("SELECT COUNT(*) FROM personnel");
    }

    private int recupererNombreConsultation() {
        return recupererNombre("SELECT COUNT(*) FROM resultat");
    }

    private int recupererNombreRDV() {
        return recupererNombre("SELECT COUNT(*) FROM rendez_vous");
    }

    private int recupererNombre(String query) {
        int nombre = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/centre2sante", "root", "");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                nombre = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombre;
    }
}
