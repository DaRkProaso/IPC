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
    private Button iniciar;
    @FXML
    private Button reserva;
    @FXML
    private Button pistas;
      
    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
    }    

    @FXML
    private void IniciarSesion(ActionEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Inicio de Sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        iniciar.getScene().getWindow().hide();
        stage.show();
    }

    @FXML
    private void GestionarReserva(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/ReservarPista.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Reservar/Anular pista");
        stage.setScene(scene);
        stage.setResizable(false);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        iniciar.getScene().getWindow().hide();
        stage.show();
    }

    @FXML
    private void VerPistas(ActionEvent event) throws IOException{
        FXMLLoader cargapistas = new FXMLLoader(getClass().getResource("/vista/VerPistas.fxml"));
        Parent root = cargapistas.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Visiualización de pistas disponibles");
        stage.setScene(scene);
        stage.setResizable(false);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        pistas.getScene().getWindow().hide();
        stage.show();
    }
    
}
