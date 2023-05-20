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
import model.Club;
import static model.Club.*;
import model.ClubDAOException;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class RegistroController implements Initializable {

    @FXML
    private TextField textNombreUsuario;
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textApellido;
    @FXML
    private PasswordField passContra;
    @FXML
    private PasswordField passRepContra;
    @FXML
    private TextField textTelefono;
    @FXML
    private TextField textCredito;
    @FXML
    private TextField textCSV;
    @FXML
    private Button regButton;
    @FXML
    private Button iniButton;
    
    private static final String PASSCHECK = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$";
    
    private static final Pattern PASSPATTERN= Pattern.compile(PASSCHECK);
    @FXML
    private Text warning;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException, InterruptedException, ClubDAOException{
        String nombre = textNombre.getText();
        String apellidos = textApellido.getText();
        String nickname = textNombreUsuario.getText();
        String password = passContra.getText();
        if (!PASSPATTERN.matcher(password).matches()) {
            warning.setText("La contraseña debe tener al menos una mayuscula y un numero, y 6 carácteres mínimos de longitud");
            return;
        }
        if(!password.equals(passRepContra.getText())){
            warning.setText("Las contraseñas no coinciden");
            
            return;
        }
        String telefono = textTelefono.getText();
        if(telefono.length() != 9){
            warning.setText("Este telefono no es válido");
            return;
        }
        try {
            Integer.parseInt(telefono);
        } catch (NumberFormatException excepcion) {
            warning.setText("Este telefono no es válido");
            return;
        }
        String tarjetacredito = textCredito.getText();
        if(tarjetacredito.length() != 16 && tarjetacredito.length() != 0){
            warning.setText("Este número de tarjeta no es válido");
            return;
        }
        try {
            Integer.parseInt(tarjetacredito);
        } catch (NumberFormatException excepcion) {
            if (tarjetacredito.length() != 0){
                warning.setText("Este número de tarjeta no es válido");
                return;
            }
        }
        String csvT = textCSV.getText();
        int csv = 0;
        try {
            csv = parseInt(textCSV.getText());
        } 
        catch (NumberFormatException excepcion){
            if (csvT.length() != 3 && csvT.length() != 0){
                warning.setText("El CSV no es correcto");
                return;
            }
        }
        if (csvT.length() == 3 && tarjetacredito.length() == 0){
            warning.setText("Por favor proporcione un numero de tarjeta de credito");
            return;
        }
        
        if(nombre.length() == 0 || apellidos.length() == 0 || nickname.length() == 0){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Los datos proporcionados no son correctos o contienen errores");
            alert.showAndWait();
            return;
        }
        
        try {
            Club club = getInstance();
            club.registerMember(nombre, apellidos, telefono, nickname, password, tarjetacredito, csv, null);
        }
        catch (ClubDAOException exception){
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("AVISO");
        alert.setHeaderText("¿Quieres seleccionar tu foto de avatar ahora?");
        alert.setContentText("Puedes cambiarlo en cualquier momento en la seccion 'perfil'.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
               /* FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/VistaPerfil.fxml"));
                Parent root = cargador.load();
                VistaPerfilController perfil = cargador.getController();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Vista del perfil");
                stage.showAndWait();
                stage.setResizable(false); */
        }
        else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
            Parent root = cargador.load();
            PaginaPrincipal inicial = cargador.getController();
            
            Club club = getInstance();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Image image = new Image("/imagenes/Icono.png");
            stage.getIcons().add(image);
            stage.setTitle("Club de Tenis " + club.getName());
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
        stage.setTitle("Inicio de Sesión");
        stage.show();
        stage.setResizable(false);
        regButton.getScene().getWindow().hide();
    }
}
