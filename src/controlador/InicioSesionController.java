/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;
import static model.Club.*;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class InicioSesionController implements Initializable {

    @FXML
    private TextField textFieldUsuario;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label warningLabel;
    @FXML
    private PasswordField passFieldPassword;
    @FXML
    private Button verPistas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
            Club club = getInstance();
        }catch(IOException e) {} catch (ClubDAOException ex) {
            Logger.getLogger(InicioSesionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        warningLabel.setText("");
        while(textFieldUsuario == null && passFieldPassword == null){
            loginButton.disableProperty();
        }
        
    }    

    @FXML
    private void handleKeyEntered(KeyEvent event) throws IOException, ClubDAOException {
        if(textFieldUsuario.isFocused() && event.getCode() == KeyCode.ENTER){
            passFieldPassword.requestFocus();
        } else{
        if(passFieldPassword.isFocused() && event.getCode() == KeyCode.ENTER){
            if(!passFieldPassword.getText().equals("") && !textFieldUsuario.getText().equals("")){
                handleSesion();}
        }
        }
    }

    @FXML
    private void handlePopupRegister(ActionEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Registro.fxml"));
        Parent root = cargador.load();
        RegistroController register = cargador.getController();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            passFieldPassword.getScene().getWindow().hide();        
    }

    @FXML
    private void handleSesion(ActionEvent event) throws IOException, ClubDAOException{
        handleSesion();
    }
    private void handleSesion() throws IOException, ClubDAOException {
        Club club = getInstance();
        String usuario = textFieldUsuario.getText();
        String contraseña = passFieldPassword.getText();
        Member cuenta = club.getMemberByCredentials(usuario, contraseña);
        if(cuenta != null){
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
            Parent root = cargador.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Club de Tenis " + club.getName());
            stage.setScene(scene);
            Image image = new Image("/imagenes/Icono.png");
            stage.getIcons().add(image);
            textFieldUsuario.getScene().getWindow().hide();
            stage.show();
        }
        else if(textFieldUsuario.getText().equals("") || passFieldPassword.getText().equals("")) {
            warningLabel.setText("Por favor, rellene toda la información");
        } else {warningLabel.setText("Usuario o contraseña incorecta");}
    }

    @FXML
    private void verPistas(ActionEvent event) throws IOException, ClubDAOException{
        Club club = getInstance();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Club de Tenis " + club.getName());
        stage.setScene(scene);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        textFieldUsuario.getScene().getWindow().hide();
        stage.show();
    }
}
