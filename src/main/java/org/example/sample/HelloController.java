package org.example.sample;

import Models.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class HelloController {

    @FXML
    private Label welcomeText;
    @FXML
    private TextField usuarioTextField;
    @FXML
    private TextField usuarioTextReg;
    @FXML
    private PasswordField passwordPassReg;
    @FXML
    private PasswordField passwordPassRepReg;
    @FXML
    private TextField nombreTextReg;
    @FXML
    private TextField apellidosTextReg;
    @FXML
    private TextField emailTextReg;
    @FXML
    private TextField direccionTextReg;
    @FXML
    private PasswordField passwordPassField;
    @FXML
    private AnchorPane loginPane;
    @FXML
    private AnchorPane registerPane;

    @FXML
    protected void onHelloButtonClick() {
        String usuarioText = usuarioTextField.getText();
        String pswText = passwordPassField.getText();

        Connection connection = null;
        String url = "jdbc:mysql://localhost/wucedb";
        String user = "root";
        String pwd = "";

        try {
            connection = DriverManager.getConnection(url, user, pwd);

            String sql = "SELECT * FROM usuarios WHERE usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuarioText);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String hashedPassDB = rs.getString("pass");
                if(BCrypt.verifyer().verify(pswText.toCharArray(), hashedPassDB).verified){

                    Stage stage = (Stage) usuarioTextField.getScene().getWindow();
                    stage.close();

                    Usuario usuario = new Usuario(rs);

                    abrirMainView(usuario);


                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de autenticación");
                    alert.setHeaderText("Usuario o contraseña incorrectos");
                    alert.setContentText("Por favor, asegúrate de ingresar el usuario y la contraseña correctos.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de autenticación");
                alert.setHeaderText("Usuario o contraseña incorrectos");
                alert.setContentText("Por favor, asegúrate de ingresar el usuario y la contraseña correctos.");
                alert.showAndWait();
            }
        } catch (SQLException e) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de conexión");
            alert.setHeaderText("Error al conectar con la base de datos");
            alert.setContentText("Se produjo un error al intentar establecer la conexión con la base de datos:\n" + e.getMessage());

            alert.showAndWait();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void abrirMainView(Usuario usuario) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = fxmlLoader.load();
            MainController mainController = fxmlLoader.getController();
            mainController.setUsuarioActivo(usuario);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Warhammer Unit Card Editor");

            stage.setMinWidth(1225);
            stage.setMinHeight(780);
            stage.setMaxWidth(1225);
            stage.setMaxHeight(780);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onRegisterLinkClick() {

        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    protected void onBackLinkClick() {

        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    @FXML
    protected void onRegButtonClick() {

        String usuarioText = usuarioTextReg.getText();
        String pswText = passwordPassReg.getText();
        String pswRep = passwordPassRepReg.getText();
        String nombreReg = nombreTextReg.getText();
        String apellidosReg = apellidosTextReg.getText();
        String emailReg = emailTextReg.getText();
        String direccionReg = direccionTextReg.getText();

        if (!pswText.equals(pswRep)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Las contraseñas no coinciden");
            alert.setContentText("Por favor, asegurate de que las contraseñas sean iguales.");
            alert.showAndWait();
            return;
        }

        if (!pswText.matches("^(?=.*[A-Z])(?=.*[!@#$&*.,]).{8,}$")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Contraseña no valida");
            alert.setContentText("La contraseña debe tener al menos una mayúscula y un símbolo ademas de tener una longitud de al menos 8 caracteres");
            alert.showAndWait();
            return;
        }

        String hashedPass = hashPassword(pswText);

        Connection connection = null;
        String url = "jdbc:mysql://localhost/wucedb";
        String user = "root";
        String pwd = "";

        try {
            connection = DriverManager.getConnection(url, user, pwd);

            String sql = "INSERT INTO usuarios (usuario, pass, nombre, apellidos, email, direccion) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuarioText);
            preparedStatement.setString(2, hashedPass);
            preparedStatement.setString(3, nombreReg);
            preparedStatement.setString(4, apellidosReg);
            preparedStatement.setString(5, emailReg);
            preparedStatement.setString(6, direccionReg);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Te has registrado");
                alert.setHeaderText(null);
                alert.setContentText("Bienvenido " + usuarioText +"!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Error al registrar el usuario.");
                alert.showAndWait();
            }
        } catch(SQLException e) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de conexión");
            alert.setHeaderText("Error al conectar con la base de datos");
            alert.setContentText("Se produjo un error al intentar establecer la conexión con la base de datos:\n" + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private String hashPassword(String password) {
        char[] passChars = password.toCharArray();
        return BCrypt.withDefaults().hashToString(12, passChars);
    }
}