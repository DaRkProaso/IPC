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
import javafx.beans.binding.Bindings;
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
import static model.Club.getInstance;

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
    
    private Member member;
    
    private Club club;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            club = getInstance();
        }catch(IOException | ClubDAOException e) {}
        warningLabel.setText(""); 
        loginButton.disableProperty().bind(Bindings.isEmpty(textFieldUsuario.textProperty()).or(Bindings.isEmpty(passFieldPassword.textProperty())));
    }    

    @FXML
    private void handleKeyEntered(KeyEvent event) throws IOException, ClubDAOException {
        if(textFieldUsuario.isFocused() && event.getCode() == KeyCode.ENTER){
            passFieldPassword.requestFocus();
        } else{
            if(passFieldPassword.isFocused() && event.getCode() == KeyCode.ENTER){
                if(!passFieldPassword.getText().equals("") && !textFieldUsuario.getText().equals("")){
                    handleSesion();
                }
            }
        }
    }

    @FXML
    private void handlePopupRegister(ActionEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Registro.fxml"));
        Parent root = cargador.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Crear nueva cuenta");
        stage.setScene(scene);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        stage.setResizable(false);
        registerButton.getScene().getWindow().hide();
        stage.show();
    }

    @FXML
    private void handleSesion(ActionEvent event) throws IOException, ClubDAOException{
        handleSesion();
    }
    private void handleSesion() throws IOException, ClubDAOException {
        if (!club.existsLogin(textFieldUsuario.getText())){
            warningLabel.setText("Usuario no existente");
            return;
        }
        member = club.getMemberByCredentials(textFieldUsuario.getText(), passFieldPassword.getText());
        if(member != null){
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
            Parent root = cargador.load();
            PaginaPrincipal pagprin = cargador.getController();
            pagprin.GetProfile(textFieldUsuario.getText(), passFieldPassword.getText(), club);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Club de Tenis " + club.getName());
            stage.setScene(scene);
            Image image = new Image("/imagenes/Icono.png");
            stage.getIcons().add(image);
            loginButton.getScene().getWindow().hide();
            stage.show();
        }
        else {warningLabel.setText("Usuario o contraseña incorecta");}
    }

    @FXML
    private void verPistas(ActionEvent event) throws IOException, ClubDAOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/VerPistas.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Club de Tenis " + club.getName());
        stage.setScene(scene);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        verPistas.getScene().getWindow().hide();
        stage.show();
    }
}
