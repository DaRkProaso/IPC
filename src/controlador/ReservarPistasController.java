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
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Club;
import model.*;

/**
 * FXML Controller class
 *
 * @author Andreu
 */
public class ReservarPistasController implements Initializable {

    @FXML
    private Button volver;
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
    private DatePicker day;
    @FXML
    private Label fecha;
    
    private Club clubR;
    
    private Member member;
    
    private String nickname, password;
    
    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(60);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);
    
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<TimeSlot> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>

    private ObjectProperty<TimeSlot> timeSlotSelected;
    
    private LocalDate daySelected;
    @FXML
    private TabPane tabulador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        day.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0 );
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
                registerHandlers(timeSlot);
                grid.add(timeSlot.getView(), 1, slotIndex);
            }
            slotIndex++;
        }
    }
    
    private void registerHandlers(TimeSlot timeSlot) {
        timeSlot.getView().setOnMousePressed((MouseEvent event) -> {
            timeSlots.forEach(slot -> {
                slot.setSelected(slot == timeSlot);
            });
            timeSlotSelected.setValue(timeSlot);
            if (event.getClickCount() > 1) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("SlotTime");
                alerta.setHeaderText("Confirma la selecció");
                alerta.setContentText("Has seleccionat: "
                        + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                        + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    ObservableList<String> styles = timeSlot.getView().getStyleClass();
                    if (styles.contains("time-slot")) {
                        styles.remove("time-slot");
                        styles.add("time-slot-libre");
                    } else {
                        styles.remove("time-slot-libre");
                        styles.add("time-slot");
                    }
                }
            }
        });
    }

    @FXML
    private void volverPrincipio(ActionEvent event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
        Parent root = cargador.load();
        PaginaPrincipal pagprin = cargador.getController();
        pagprin.GetProfile(nickname, password, clubR);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Club de Tenis GreenBall");
        stage.setScene(scene);
        Image image = new Image("/imagenes/Icono.png");
        stage.getIcons().add(image);
        volver.getScene().getWindow().hide();
        stage.show();
    }
    
    public class TimeSlot {

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
            view.getStyleClass().add("time-slot");
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
    private void GetDatos(){
        nickname = member.getNickName();
        password = member.getPassword();
    }
    
    public void GetDatos(String nickname, String password, Club club){
        member = club.getMemberByCredentials(nickname, password);
        clubR = club;
        GetDatos();
    }
}
