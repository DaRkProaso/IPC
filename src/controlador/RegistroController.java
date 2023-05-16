/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class RegistroController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private TextField textFieldUsuario;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private Button loginPopupButton;
    @FXML
    private Button RegisterInputButton;
    @FXML
    private Button selectImage;
    @FXML
    private TextField telefonoField;
    @FXML
    private Label checktelefonoText;
    @FXML
    private TextField textFieldTarjeta;
    @FXML
    private TextField textFieldCSV;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldPassword1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handlePopupLogin(ActionEvent event) {
    }

    @FXML
    private void handleRegisterInputButton(ActionEvent event) {
    }

    @FXML
    private void SelectImage(ActionEvent event) {
    }
    
}
