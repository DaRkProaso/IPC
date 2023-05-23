/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.*;
import static model.Club.getInstance;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class VistaPerfilController implements Initializable {

    @FXML
    private Label nicknameLabel;
    @FXML
    private ImageView imagePerfil;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField creditoField;
    @FXML
    private TextField csvField;
    @FXML
    private PasswordField passField;
    
    private Member member;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            Club club = getInstance();
            member = club.getMemberByCredentials(nicknameLabel.getText(), passField.getText());
            if (member.getImage() == null){
                Image image = new Image("/imagenes/default.png");
                imagePerfil.setImage(image);
            }
        }
        catch(ClubDAOException | IOException e){}
    }    
}
