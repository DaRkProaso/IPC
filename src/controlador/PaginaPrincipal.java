/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

/**
 *
 * @author jsoler
 */
public class PaginaPrincipal implements Initializable {
    private Label nombreClub;
    @FXML
    private ImageView imagePerfil;
    private Label labelPerfil;
    @FXML
    private Button reservas;
    @FXML
    private Button gestionar;
    @FXML
    private ListView<Booking> reservasListView;
    
    private Club clubP;
    
    private Member member;
    
    private Image image;
    
    private String nickname, password;
    
    private ObservableList<Booking> listaObservable = null;
    @FXML
    private Button sesion;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        reservasListView.setVisible(false);
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
        if (reservasListView.isVisible()) {
            reservasListView.setVisible(false);
        } else {
            reservasListView.setVisible(true);
        }    
        List<Booking> reservasUsuario = clubP.getUserBookings(member.getNickName());
        ArrayList<Booking> misReservas = new ArrayList<>(reservasUsuario);
        Collections.sort(misReservas, (Booking booking1, Booking booking2) -> booking2.getBookingDate().compareTo(booking1.getBookingDate()));
        List<Booking> diezPrimeras = misReservas.subList(0, Math.min(10, misReservas.size()));
        listaObservable = FXCollections.observableList(diezPrimeras);
        reservasListView.setItems(listaObservable);
        reservasListView.setCellFactory(c -> new BookingListCell());
    }
    private void ModPerfil(MouseEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/VistaPerfil.fxml"));
        Parent root = cargador.load();
        VistaPerfilController perfil = cargador.getController();
        perfil.SetPerfil(nickname, password, clubP);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        reservas.getScene().getWindow().hide();
        stage.setTitle("Vista del perfil");
        Image image2 = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image2);
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    private void CerrarSesion(ActionEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Club de Tenis GreenBall");
        stage.setScene(scene);
        Image image2 = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image2);
        sesion.getScene().getWindow().hide();
        stage.show();
    }
    
    class BookingListCell extends ListCell<Booking> {
        @Override
        protected void updateItem(Booking b, boolean empty) {
            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E MMM d");
            super.updateItem(b, empty);
            if (b == null || empty) {
                setText(null);
            } else {
                String paidStatus = b.getPaid() ? "pagado" : "no pagado";
                setText("Reserva: " + b.getMadeForDay().format(dayFormatter) + " - " +
                        b.getCourt().getName() + " - (" + b.getFromTime() + "-" + 
                        b.getFromTime().plusHours(1) + ") - " + paidStatus);
            }
        }
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
    
    private Booking getBookingAtDateTime(Court court, LocalDateTime dateTime) {
        for (Booking booking : clubP.getBookings()) {
            if (booking.getCourt().equals(court)
                && booking.getMadeForDay().equals(dateTime.toLocalDate())
                && booking.getFromTime().equals(dateTime.toLocalTime())) {
                return booking;
            }
        } return null;
    }
    private boolean hasTwoConsecutiveBookings(Court court, LocalDateTime dateTime) {
        LocalDateTime previous2Hours = dateTime.minusHours(2);
        LocalDateTime previousHour = dateTime.minusHours(1);
        LocalDateTime nextHour = dateTime.plusHours(1);
        LocalDateTime next2Hours = dateTime.plusHours(2);
        return (getBookingAtDateTime(court, previousHour)!= null && getBookingAtDateTime(court, previous2Hours) != null) || (getBookingAtDateTime(court, nextHour) != null && getBookingAtDateTime(court, next2Hours) != null);
    }
}
