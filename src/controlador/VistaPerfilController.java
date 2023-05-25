/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
    private String nombre, apellidos, nickname, password, telefono,tarjeta;
    
    private int csv;
    
    private Image image;
    
    private Member member;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label tLabel;
    @FXML
    private Label csvLabel;
    @FXML
    private Label nombreLabel;
    @FXML
    private Label telefonoLabel;
    @FXML
    private Button buttonPassword;
    @FXML
    private Button buttonTarjeta;
    @FXML
    private Button buttonTelefono;
    @FXML
    private ChoiceBox<Image> y;
    @FXML
    private ImageView imageAvatar;
    @FXML
    private Label nicknameLabel;
    
    private Club club;
    @FXML
    private Button buttonNameSurname;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SetPerfil();
        try{
            club = getInstance();
            
        } catch(ClubDAOException | IOException e){}
        passwordLabel.setText("··········");
        member = club.getMemberByCredentials(nickname, password);
    }    

    @FXML
    private void handleChangePassword(ActionEvent event) {
        TextInputDialog dialog3 = new TextInputDialog("");
        dialog3.setTitle("Actualizar contraseña");
        dialog3.setHeaderText("Introduzca su contraseña actual");
        dialog3.setContentText("Contraseña:");
        dialog3.showAndWait();
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar contraseña");
        dialog.setHeaderText("Introduce la nueva contraseña");
        dialog.setContentText("Nueva Contraseña:");
        dialog.showAndWait();
        TextInputDialog dialog2 = new TextInputDialog("");
        dialog2.setTitle("Actualizar contraseña");
        dialog2.setHeaderText("Repite la contraseña por favor");
        dialog2.setContentText("Repetir contraseña:");
        dialog2.showAndWait();
        if(!dialog3.getResult().equals(password) || !dialog.getResult().equals(dialog2.getResult())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al actualizar la contraseña");
            alert.setHeaderText("Su antigua contraseña no es correcta o las nuevas no coinciden");
            alert.setContentText("Por favor, vuelva a intentarlo");
            alert.showAndWait();
        }
        member.setPassword(dialog.getResult());
    }

    @FXML
    private void handleChangeTarjeta(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar Tarjeta de Crédito");
        dialog.setHeaderText("Introduce o actualiza tu tarjeta de crédito");
        dialog.setContentText("Tarjeta:");
        dialog.showAndWait();
        if(dialog.getResult().length() != 16 && dialog.getResult().matches("\\d+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al actualizar la tarjeta");
            alert.setHeaderText("Ha introducido una tarjeta no válida");
            alert.setContentText("Por favor, introduzca una tarjeta válida");
            alert.showAndWait();
        }
        TextInputDialog dialog2 = new TextInputDialog("");
        dialog2.setTitle("Actualizar CSV");
        dialog2.setHeaderText("Introduce o actualiza tu CSV:");
        dialog2.setContentText("CSV:");
        dialog2.showAndWait();
        if(dialog2.getResult().length() != 3 && dialog2.getResult().matches("\\d+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al actualizar la tarjeta");
            alert.setHeaderText("Ha introducido un CSV no valido");
            alert.setContentText("Por favor, introduzca un CSV valido");
            alert.showAndWait();
        }
        tLabel.setText(dialog.getResult());
        csvLabel.setText(dialog2.getResult());
        member.setCreditCard(dialog.getResult());
        member.setSvc(parseInt(dialog2.getResult()));
    }

    private void handleChangeName(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar nombre");
        dialog.setHeaderText("Introduce tu nombre");
        dialog.setContentText("Nombre:");
        dialog.showAndWait();
        TextInputDialog dialog2 = new TextInputDialog("");
        dialog2.setTitle("Actualizar apellidos");
        dialog2.setHeaderText("Introduce tus apellidos");
        dialog2.setContentText("Apellidos:");
        dialog2.showAndWait();
        nombreLabel.setText(dialog.getResult() + " "+ dialog2.getResult());
        member.setName(dialog.getResult());
        member.setSurname(dialog2.getResult());
    }

    @FXML
    private void handleChangeTelefono(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar número de teléfono");
        dialog.setHeaderText("Introduce tu número de teléfono");
        dialog.setContentText("Teléfono:");
        dialog.showAndWait();
        if(dialog.getResult().length() != 9 && dialog.getResult().matches("\\d+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al actualizar el teléfono");
            alert.setHeaderText("Ha introducido un número no valido");
            alert.setContentText("Por favor, introduzca un numero valido");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleImgChange(MouseEvent event) {
        
    }
    private void SetPerfil(){
        nombreLabel.setText(nombre + " " + apellidos);
        nicknameLabel.setText(nickname);
        telefonoLabel.setText(telefono);
        tLabel.setText(tarjeta);
        csvLabel.setText(Integer.toString(csv));
        if (image == null){
            Image image2 = new Image("/imagenes/default.png");
            imageAvatar.setImage(image2);
        }
        else{imageAvatar.setImage(image);}
    }    
    public void SetPerfil(String nombre, String apellidos, String nickname, String password, String telefono, String tarjeta, int csv, Image image){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nickname = nickname;
        this.password = password;
        this.telefono = telefono;
        this.tarjeta = tarjeta;
        this.csv = csv;
        this.image = image;
    }
}
