/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.io.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Club;
import model.*;
import model.ClubDAOException;
import static model.Club.*;

/**
 *
 * @author jsoler
 */
public class PaginaPrincipal implements Initializable {
    @FXML
    private Label nombreClub;
    @FXML
    private ImageView imagePerfil;
    @FXML
    private Label labelPerfil;
    @FXML
    private Button reservas;
    @FXML
    private Button gestionar;
    
    private Club clubP;
    
    private Member member;
    
    private Image image;
    
    private String nickname, password;
    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb){
        if (member != null){
            GetProfile();
        }
    } 
    @FXML
    private void GestionarReserva(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/ReservarPistas.fxml"));
        Parent root = cargador.load();
        ReservarPistasController respis = cargador.getController();
        respis.GetDatos(nickname, password, clubP);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Reservar/Anular pista");
        stage.setScene(scene);
        stage.setResizable(false);
        Image image2 = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image2);
        gestionar.getScene().getWindow().hide();
        stage.show();
    }

    @FXML
    private void VerReservas(ActionEvent event) throws IOException{

    }
    public void GetProfile(String nickname, String password, Club club){
        member = club.getMemberByCredentials(nickname, password);
        if(member.getImage() == null){
            image = new Image ("/imagenes/avatars/default.png");
        }
        else {image = member.getImage();}
        this.nickname = nickname;
        this.password = password;
        clubP = club;
        GetProfile();
}
    private void GetProfile(){
        imagePerfil.setImage(image);
        labelPerfil.setText("Hola, " + nickname);
        nombreClub.setText(clubP.getName());
    }

    @FXML
    private void ModPerfil(MouseEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/VistaPerfil.fxml"));
        Parent root = cargador.load();
        VistaPerfilController perfil = cargador.getController();
        perfil.SetPerfil(nickname, password, clubP);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        reservas.getScene().getWindow().hide();
        stage.setTitle("Vista del perfil");
        stage.setResizable(false);
        stage.show();
    }
}
