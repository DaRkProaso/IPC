/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.*;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class VistaPerfilController implements Initializable {
    private String nombre, apellidos, nickname, password;
    
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
    
    private Club clubV;
    public ObservableList<Image> listaObservable = null;
    private Circle clip = new Circle(60);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordLabel.setText("··········");
        
        List<Image> avatares = new ArrayList<>();
        
        avatares.add(new Image("imagenes/avatars/men.png"));
        avatares.add(new Image("imagenes/avatars/men2.png"));
        avatares.add(new Image("imagenes/avatars/men3.png"));
        avatares.add(new Image("imagenes/avatars/men4.png"));
        avatares.add(new Image("imagenes/avatars/men5.png"));
        avatares.add(new Image("imagenes/avatars/woman.png"));
        avatares.add(new Image("imagenes/avatars/woman2.png"));
        avatares.add(new Image("imagenes/avatars/woman3.png"));
        avatares.add(new Image("imagenes/avatars/woman4.png"));
        avatares.add(new Image("imagenes/avatars/woman5.png"));
        avatares.add(new Image("imagenes/avatars/woman6.png"));
        
        
        listaObservable = FXCollections.observableArrayList(avatares);
        
        
        //Clip es el circulo que rodea la imagen para que no se vea en forma de cuadrado.
        clip.setCenterX(imageAvatar.getFitWidth()/3 - 5);
        clip.setCenterY(imageAvatar.getFitHeight()/2);
        clip.setId("clip-b");
        clip.getStyleClass().add("clip-b");
        clip.setStyle("-fx-border-color: grey;");
        clip.setStyle("-fx-border-width: 4px;");
        
        
        imageBox.setItems(listaObservable);
        
        imageBox.setLayoutX(imageAvatar.getFitWidth());
        imageBox.setLayoutY(imageAvatar.getFitHeight());
        imageBox.hide();
        imageAvatar.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageAvatar.snapshot(parameters, null);
        
        imageBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Image> obs, Image oldVal, Image newVal) -> {
            imageAvatar.setImage(imageBox.getSelectionModel().getSelectedItem());
            try {
                member.setImage(imageAvatar.getImage());
                imageAvatar.setEffect(null);
            } catch (Exception ex) {
                
                Logger.getLogger(VistaPerfilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });     
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
        tLabel.setText(dialog.getResult());
        csvLabel.setText(dialog2.getResult());
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
        telefonoLabel.setText(dialog.getResult());
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
        nombre = member.getName();
        apellidos = member.getSurname();
        this.nickname = nickname;
        this.password = password;
        SetPerfil();
    }

    @FXML
    private void handleExit(ActionEvent event) throws IOException{
        member.setTelephone(telefonoLabel.getText());
        member.setName(nombre);
        member.setSurname(apellidos);
        member.setCreditCard(tLabel.getText());
        member.setSvc(parseInt(csvLabel.getText()));
        member.setPassword(password);
        member.setImage(imageAvatar.getImage());
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
        Parent root = cargador.load();
        PaginaPrincipal pagprin = cargador.getController();
        pagprin.GetProfile(nickname, password, clubV);
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
