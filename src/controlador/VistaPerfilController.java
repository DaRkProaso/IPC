/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
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
    private ImageView imageAvatar;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Button buttonNameSurname;
    @FXML
    private ChoiceBox<Image> imageBox;
    @FXML
    private Button buttonExit;
    @FXML
    private Button buttonImage;
    
    private Stage stage;
    
    private Club clubV;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordLabel.setText("··········");
    }    

    @FXML
    private void handleChangePassword(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar contraseña");
        dialog.setHeaderText("Introduce la nueva contraseña");
        dialog.setContentText("Nueva Contraseña:");
        dialog.showAndWait();
        password = dialog.getResult();
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
            return;
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
            return;
        }
        tarjeta = dialog.getResult();
        csv = parseInt(dialog2.getResult());
        tLabel.setText(tarjeta);
        csvLabel.setText(Integer.toString(csv));
    }

    @FXML
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
        nombre = dialog.getResult();
        apellidos = dialog2.getResult();
        nombreLabel.setText(nombre + " "+ apellidos);
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
        telefono = dialog.getResult();
        telefonoLabel.setText(telefono);
    }

    @FXML
    private void handleImgChange(MouseEvent event) {
        
    }
    
    private void SetPerfil(){
        nombreLabel.setText(nombre + " " + apellidos);
        nicknameLabel.setText(member.getNickName());
        telefonoLabel.setText(member.getTelephone());
        tLabel.setText(member.getCreditCard());
        csvLabel.setText(Integer.toString(member.getSvc()));
        if (member.getImage() == null){
            imageAvatar.setImage(new Image ("/imagenes/avatars/default.png"));
        }
        else{imageAvatar.setImage(member.getImage());
        }
    } 
    public void SetPerfil(String nickname, String password, Club club){
        member = club.getMemberByCredentials(nickname, password);
        clubV = club;
        SetPerfil();
    }

    @FXML
    private void handleExit(ActionEvent event) throws IOException{
        member.setTelephone(telefonoLabel.getText());
        member.setName(nombre);
        member.setSurname(apellidos);
        member.setCreditCard(tarjeta);
        member.setSvc(csv);
        member.setPassword(password);
        member.setImage(image);
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
        Parent root = cargador.load();
        PaginaPrincipal pagprin = cargador.getController();
        pagprin.GetProfile(nickname, password);
        Stage stage2 = new Stage();
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        Image image2 = new Image("/imagenes/Icono.png");
        stage2.getIcons().add(image2);
        stage2.setTitle("Club de Tenis GreenBall");
        stage2.setResizable(false);
        buttonExit.getScene().getWindow().hide();
        stage2.show();
    }

    @FXML
    private void handleImgChange(ActionEvent event) {
    }
}
