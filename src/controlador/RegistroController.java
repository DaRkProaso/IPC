/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javax.swing.JScrollPane;
import model.*;
import static model.Club.*;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class RegistroController implements Initializable {
    
    private static final String PASSCHECK = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$";
    
    private static final Pattern PASSPATTERN= Pattern.compile(PASSCHECK);
    @FXML
    private TextField textFieldUsuario;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private Button loginPopupButton;
    @FXML
    private Button registerInputButton;
    @FXML
    private TextField textFieldTarjeta;
    @FXML
    private TextField textFieldCSV;
    @FXML
    private TextField textFieldTF;
    @FXML
    private Label warningPass;
    @FXML
    private Label telefonoWarning;
    @FXML
    private Label warningTarjeta;
    @FXML
    private PasswordField repContra;
    @FXML
    private PasswordField contra;
    @FXML
    private Label warning;
    
    private Member member;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       registerInputButton.disableProperty().bind(Bindings.isEmpty(nombreField.textProperty()).or(Bindings.isEmpty(apellidosField.textProperty())).or(Bindings.isEmpty(contra.textProperty())).or(Bindings.isEmpty(repContra.textProperty())).or(Bindings.isEmpty(textFieldTF.textProperty())));
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException, InterruptedException, ClubDAOException{
        Club club = getInstance();
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();
        String nickname = textFieldUsuario.getText();
        if(club.existsLogin(nickname)){
            warning.setText("Este usuario ya esta registrado");
        }
        String password = contra.getText();
        if (!PASSPATTERN.matcher(password).matches()) {
            warningPass.setText("La contraseña debe contener 6 o más carácteres, formados de letras y números");
            return;
        }
        if(!password.equals(repContra.getText())){
            warningPass.setText("Las contraseñas no coinciden");
            
            return;
        }
        String telefono = textFieldTF.getText();
        if(telefono.length() != 9){
            telefonoWarning.setText("Este telefono no es válido");
            return;
        }
        try {
            Integer.parseInt(telefono);
        } catch (NumberFormatException excepcion) {
            telefonoWarning.setText("Este telefono no es válido");
            return;
        }
        String tarjetacredito = textFieldTarjeta.getText();
        if(tarjetacredito.length() == 16 && tarjetacredito.length() == 0){ 
            warningTarjeta.setText("Este número de tarjeta no es válido");
            return;
        }
        try {
            Integer.parseInt(tarjetacredito);
        } catch (NumberFormatException excepcion) {
            if (tarjetacredito.length() != 0){
                warningTarjeta.setText("Este número de tarjeta no es válido");
                return;
            }
        }
        String csvT = textFieldCSV.getText();
        int csv = 0;
        try {
            csv = parseInt(textFieldCSV.getText());
        } 
        catch (NumberFormatException excepcion){
            if (csvT.length() != 3 && csvT.length() != 0){
                warningTarjeta.setText("El CSV no es correcto");
                return;
            }
        }
        if (csvT.length() == 3 && tarjetacredito.length() == 0){
            warningTarjeta.setText("Por favor proporcione un numero de tarjeta de credito");
            return;
        }
        club.registerMember(nombre, apellidos, telefono, nickname, password, tarjetacredito, csv, null);
        member = club.getMemberByCredentials(nickname, password);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("AVISO");
        alert.setHeaderText("¿Quieres seleccionar tu foto de avatar ahora?");
        alert.setContentText("Puedes cambiarlo en cualquier momento en la seccion 'perfil'.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/VistaPerfil.fxml"));
            Parent root = cargador.load();
            VistaPerfilController perfil = cargador.getController();
            perfil.SetPerfil(nombre, apellidos, nickname, password, telefono, csvT);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            registerInputButton.getScene().getWindow().hide();
            stage.setTitle("Vista del perfil");
            stage.setResizable(false);
            stage.show();
                
        }
        else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
            Parent root = cargador.load();
            PaginaPrincipal inicial = cargador.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Image image = new Image("/imagenes/Icono.png");
            stage.getIcons().add(image);
            stage.setTitle("Club de Tenis " + club.getName());
            registerInputButton.getScene().getWindow().hide();
            stage.show();
            stage.setResizable(false);
        }
    }

    @FXML
    private void handleInicioSesion(ActionEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
        Parent root = cargador.load();
        InicioSesionController login = cargador.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        stage.setTitle("Inicio de Sesión");
        stage.setResizable(false);
        loginPopupButton.getScene().getWindow().hide();
        stage.show();
    }
}
