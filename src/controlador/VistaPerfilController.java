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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class VistaPerfilController implements Initializable {

    @FXML
    private Label nicknameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label birthdateLabel;
    @FXML
    private Button buttonPassword;
    @FXML
    private Button buttonEmail;
    @FXML
    private Button buttonBirthdate;
    @FXML
    private ChoiceBox<?> y;
    @FXML
    private ImageView imageAvatar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleChangePassword(ActionEvent event) {
    }

    @FXML
    private void handleChangeEmail(ActionEvent event) {
    }

    @FXML
    private void handleChangeBirthdate(ActionEvent event) {
    }

    @FXML
    private void handleImgChange(MouseEvent event) {
    }
    
}
