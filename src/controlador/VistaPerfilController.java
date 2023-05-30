/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
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
    private ChoiceBox<ImagenItem> imageBox;
    @FXML
    private Button buttonExit;
    @FXML
    private Button buttonImage;
    
    private static final String PASSCHECK = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$";
    
    private static final Pattern PASSPATTERN= Pattern.compile(PASSCHECK);
    
    private Club clubV;
    public ObservableList<ImagenItem> listaObservable = null;
    private final Circle clip = new Circle(90);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordLabel.setText("··········");
        
        List<ImagenItem> avatares = new ArrayList<>();
        
        avatares.add(new ImagenItem(new Image("imagenes/avatars/default.png"), "default"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/men.png"), "hombre1"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/men2.png"), "hombre2"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/men3.png"), "hombre3"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/men4.png"), "hombre4"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/men5.png"), "hombre5"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/woman.png"), "mujer1"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/woman2.png"), "mujer2"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/woman3.png"), "mujer3"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/woman4.png"), "mujer4"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/woman5.png"), "mujer5"));
        avatares.add(new ImagenItem(new Image("imagenes/avatars/woman6.png"), "mujer6"));
        
        listaObservable = FXCollections.observableArrayList(avatares);
        
        
        //Clip es el circulo que rodea la imagen para que no se vea en forma de cuadrado.
//        clip.setCenterX(imageAvatar.getFitWidth()/2 - 5);
//        clip.setCenterY(imageAvatar.getFitHeight()/2);
        clip.setId("clip-b");
        clip.getStyleClass().add("clip-b");
        clip.setStyle("-fx-border-color: grey;");
        clip.setStyle("-fx-border-width: 4px;");
        
        clip.setLayoutX( imageAvatar.getX() + (imageAvatar.getFitWidth()/2) - 20);
        clip.setLayoutY(imageAvatar.getY() + imageAvatar.getFitHeight()/2);
        
        imageBox.setItems(listaObservable);
        
        imageBox.setLayoutX(imageAvatar.getFitWidth());
        imageBox.setLayoutY(imageAvatar.getFitHeight());
        imageAvatar.setClip(clip);
        
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        imageBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ImagenItem> obs, ImagenItem oldVal, ImagenItem newVal) -> {
            imageAvatar.setImage(imageBox.getSelectionModel().getSelectedItem().getImagen());
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
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Nueva Contraseña");
        dialog.showAndWait();
        if (dialog.getResult() == null) return;
        if(!PASSPATTERN.matcher(password).matches()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al actualizar la contraseña");
            alert.setHeaderText("Ha introducido una contraseña incorrecta");
            alert.setContentText("La contraseña ha de tener 6 carácteres, incluyendo minimo una mayúscula y un número");
            alert.showAndWait();
            return;
        }
        password = dialog.getResult();
    }

    @FXML
    private void handleChangeTarjeta(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar Tarjeta de Crédito");
        dialog.setHeaderText("Introduce o actualiza tu tarjeta de crédito");
        dialog.setContentText("Tarjeta:");
        dialog.showAndWait();
        if (dialog.getResult() == null) return;
        if (dialog.getResult().equals("")) return;
        else if(dialog.getResult().length() != 16 && dialog.getResult().matches("\\d+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al actualizar la tarjeta");
            alert.setHeaderText("Ha introducido una tarjeta no válida");
            alert.setContentText("Por favor, introduzca una tarjeta válida");
            alert.showAndWait();
            return;
        }
        tLabel.setText(dialog.getResult());
        TextInputDialog dialog2 = new TextInputDialog("");
        dialog2.setTitle("Actualizar CSV");
        dialog2.setHeaderText("Introduce o actualiza tu CSV:");
        dialog2.setContentText("CSV:");
        dialog2.showAndWait();
        if (dialog2.getResult() == null) return;
        if (dialog2.getResult().equals("")) return; 
        if(dialog2.getResult().length() != 3 && dialog2.getResult().matches("\\d+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al actualizar la tarjeta");
            alert.setHeaderText("Ha introducido un CSV no valido");
            alert.setContentText("Por favor, introduzca un CSV valido");
            alert.showAndWait();
            return;
        }
        csvLabel.setText(dialog2.getResult());
    }

    @FXML
    private void handleChangeName(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar nombre");
        dialog.setHeaderText("Introduce tu nombre");
        dialog.setContentText("Nombre:");
        dialog.showAndWait();
        if (null == dialog.getResult()) return;
        else switch (dialog.getResult()) {
            case "":
                return;
            default:
                nombre = dialog.getResult();
                nombreLabel.setText(nombre + " "+ apellidos);
                break;
        }
        TextInputDialog dialog2 = new TextInputDialog("");
        dialog2.setTitle("Actualizar apellidos");
        dialog2.setHeaderText("Introduce tus apellidos");
        dialog2.setContentText("Apellidos:");
        dialog2.showAndWait();
        if (null == dialog2.getResult()) return;
        else switch (dialog2.getResult()) {
            case "":
                return;
            default:
                apellidos = dialog2.getResult();
                break;
        }
        nombreLabel.setText(nombre + " "+ apellidos);
    }

    @FXML
    private void handleChangeTelefono(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Actualizar número de teléfono");
        dialog.setHeaderText("Introduce tu número de teléfono");
        dialog.setContentText("Teléfono:");
        dialog.showAndWait();
        if (dialog.getResult() == null) return;
        else if (dialog.getResult().equals("")) return;
        else if(dialog.getResult().length() != 9 && dialog.getResult().matches("\\d+")){
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
        if (!csvLabel.getText().equals("")){member.setSvc(parseInt(csvLabel.getText()));}
        else{member.setSvc(0);}
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        String userHomeDir = System.getProperty("user.home");
        String downloadsDirPath = userHomeDir + File.separator + "Downloads";
        File downloadsDir = new File(downloadsDirPath);
        fileChooser.setInitialDirectory(downloadsDir);
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", ".jpg", ".jpeg", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(buttonImage.getScene().getWindow());
        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            imageAvatar.setImage(selectedImage);
        }
    }
    
    public class ImagenItem {
        private Image imagen;
        private String nombre;

        public ImagenItem(Image imagen, String nombre) {
            this.imagen = imagen;
            this.nombre = nombre;
        }

        public Image getImagen() {
            return imagen;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}
