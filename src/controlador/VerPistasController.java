/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class VerPistasController implements Initializable {

    @FXML
    private Button volver;
    @FXML
    private DatePicker day;
    @FXML
    private GridPane grid1;
    @FXML
    private GridPane grid2;
    @FXML
    private GridPane grid3;
    @FXML
    private GridPane grid4;
    @FXML
    private GridPane grid5;
    @FXML
    private GridPane grid6;
    @FXML
    private Label fecha;
    
    private Club club;
    
    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    
    private final Duration slotLength = Duration.ofMinutes(60);
    
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);

    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private final List<TimeSlot> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>

    private ObjectProperty<TimeSlot> timeSlotSelected;

    private LocalDate daySelected;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        day.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0);
                }
            };
        });
        timeSlotSelected = new SimpleObjectProperty<>();
        day.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c);
        });
        day.setValue(LocalDate.now());
        setTimeSlotsGrid(day.getValue());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E MMM d");
        timeSlotSelected.addListener((a, b, c) -> {
            if (c == null) {
                fecha.setText("");
            } else {
                fecha.setText(c.getDate().format(dayFormatter)
                        + "-"
                        + c.getStart().format(timeFormatter));
            }
        });
    }
    
    private void setTimeSlotsGrid(LocalDate date) {
        timeSlotSelected.setValue(null);
        List<GridPane> grids = Arrays.asList(grid1, grid2, grid3, grid4, grid5, grid6);
        grids.forEach(grid -> {
            grid.getChildren().removeAll(timeSlots.stream()
                    .map(TimeSlot::getView)
                    .collect(Collectors.toList()));
        });
        timeSlots.clear();
        int slotIndex = 1;
        for (LocalDateTime startTime = date.atTime(firstSlotStart); !startTime.isAfter(date.atTime(lastSlotStart)); startTime = startTime.plus(slotLength)) {
            for (GridPane grid : grids) {
                TimeSlot timeSlot = new TimeSlot(startTime, slotLength);
                timeSlots.add(timeSlot);
                VerHandlers(timeSlot);
                grid.add(timeSlot.getView(), 1, slotIndex);
            }
            slotIndex++;
        }
        if (club != null) {
            updateGridColors();
        }
    }
    
    private void VerHandlers(TimeSlot timeSlot) {
        timeSlot.getView().setOnMousePressed((MouseEvent e) -> {
            
            timeSlots.forEach(slot -> {
                slot.setSelected(slot == timeSlot);
            });
            
            timeSlotSelected.setValue(timeSlot);
            if (e.getClickCount() > 1) {
                timeSlots.forEach(ts -> ts.setSelected(false));
                timeSlot.setSelected(true);
                timeSlotSelected.set(timeSlot);
                LocalDate selectedDate = day.getValue();
                LocalTime selectedTime = timeSlot.getTime();
                LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedTime);
                Court selectedCourt = getCourtFromGrid(timeSlot.getView().getParent());
                if (selectedCourt != null) {
                    Booking existingBooking = getBookingAtDateTime(selectedCourt, selectedDateTime);
                    if (existingBooking != null){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Reserva");
                        alert.setHeaderText("La pista está reservada por " + existingBooking.getMember().getNickName());
                        alert.showAndWait();
                    }else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Reservar pista");
                        alert.setHeaderText("La pista está libre");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    @FXML
    private void volverPrincipio(ActionEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Club de Tenis GreenBall");
        stage.setScene(scene);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        volver.getScene().getWindow().hide();
        stage.show();
    }
    
    private class TimeSlot {
        
        private final LocalDateTime start;
        private final Duration duration;
        protected final Pane view;

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public final BooleanProperty selectedProperty() {
            return selected;
        }

        public final boolean isSelected() {
            return selectedProperty().get();
        }

        public final void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

        public TimeSlot(LocalDateTime start, Duration duration) {
            this.start = start;
            this.duration = duration;
            view = new Pane();
            view.getStyleClass().add("time-slot-libre");
            selectedProperty().addListener((obs, wasSelected, isSelected)
                    -> view.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, isSelected));
        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalTime getTime() {
            return start.toLocalTime();
        }

        public LocalDate getDate() {
            return start.toLocalDate();
        }

        public DayOfWeek getDayOfWeek() {
            return start.getDayOfWeek();
        }

        public Duration getDuration() {
            return duration;
        }

        public Node getView() {
            return view;
        }
    }
    
    public void GetClub(Club club){
        this.club = club;
    }
    
    private Court getCourtFromGrid(Parent grid) {
        int gridIndex = Integer.parseInt(grid.getId().substring(grid.getId().length() - 1)) - 1;
        return club.getCourts().get(gridIndex);
    }
    
    private Booking getBookingAtDateTime(Court court, LocalDateTime dateTime) {
        for (Booking booking : club.getBookings()) {
            if (booking.getCourt().equals(court)
                && booking.getMadeForDay().equals(dateTime.toLocalDate())
                && booking.getFromTime().equals(dateTime.toLocalTime())) {
                return booking;
            }
        } return null;
    }
    
    private void updateGridColors() {
        List<GridPane> grids = Arrays.asList(grid1, grid2, grid3, grid4, grid5, grid6);
        LocalDate date = day.getValue();
        List<Booking> bookingsForDay = club.getForDayBookings(date);
        grids.forEach(grid -> {
            grid.getChildren().stream().filter(node -> (node instanceof Pane)).map(node -> (Pane) node).forEachOrdered(pane -> {
                Integer row = GridPane.getRowIndex(pane);
                if (row != null) {
                    LocalTime time = firstSlotStart.plusMinutes((row - 1) * slotLength.toMinutes());
                    Court court = getCourtFromGrid(pane.getParent());
                    Booking booking = bookingsForDay.stream()
                            .filter(b -> b.getCourt().equals(court)
                                    && b.getMadeForDay().equals(date)
                                    && b.getFromTime().equals(time))
                            .findFirst()
                            .orElse(null);
                    if (booking != null) {
                        pane.getStyleClass().removeAll("time-slot-libre", "time-slot");
                        pane.getStyleClass().add("time-slot");
                    } else {
                        pane.getStyleClass().removeAll("time-slot-libre", "time-slot");
                        pane.getStyleClass().add("time-slot-libre");
                    }
                }
            });
        });
    }
}