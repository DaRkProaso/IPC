/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author jsoler
 */
public class PaginaPrincipal implements Initializable {
    private Label labelMessage;
    @FXML
    private Button iniciar;
    @FXML
    private Button reserva;
    @FXML
    private Button pistas;
    
    //=========================================================
    // event handler, fired when button is clicked or 
    //                      when the button has the focus and enter is pressed
    private void handleButtonAction(ActionEvent event) {
        labelMessage.setText("Hello, this is your first JavaFX project - IPC");
    }
    
    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void IniciarSesion(ActionEvent event) {
    }

    @FXML
    private void GestionarReserva(ActionEvent event) {
    }

    @FXML
    private void VerPistas(ActionEvent event) {
    }
    
}
