package kangamoyet.djadou.kouassid.applicationcentre2sante;


import java.sql.*;



public class MySQLConnector {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/centre2sante";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Mettez votre mot de passe ici

    private static Connection connection;

    public static Connection getConnect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**private static String HOST = "127.0.0.1:8080";
    private static int PORT = 3306;
    private static String DB_NAME = "centre2sante";
    private static String USERNAME = "root";
    private static String PASSWORD = "";
    private static Connection connection ;


    public static Connection getConnect (){
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST,PORT,DB_NAME),USERNAME,PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return  connection;
    }**/
    /**private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void insertUserData(int id_patient, String nom, String prenom,String date_naissance, String sexe, String telephone, String Numero) throws ClassNotFoundException {
        //1.Charger le driver mysql
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.Creer la connexion
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {


            // Convertir la date de naissance en format java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(date_naissance);
            java.sql.Date dateNaissance = new java.sql.Date(parsedDate.getTime());


            String query = "INSERT INTO utilisateurs (id_patient, nom, prenom, date_naissance, sexe, telephone, Numero) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id_patient);
                statement.setString(2, nom);
                statement.setString(3, prenom);
                statement.setDate(4, Date.valueOf(date_naissance));
                statement.setString(5, sexe);
                statement.setString(6, telephone);
                statement.setString(7, Numero);
                statement.executeUpdate();

                // Afficher la boîte de dialogue de succès
                Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                successDialog.setTitle("Succès");
                successDialog.setHeaderText(null);
                successDialog.setContentText("Les informations ont été enregistrées avec succès.");
                successDialog.showAndWait();

            }
        } catch (SQLException | ParseException e) {
            showAlert("Erreur lors de l'insertion des données : " + e.getMessage());
        }
    }**/
}
