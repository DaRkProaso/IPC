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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;

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

    private final List<TimeSlot> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>

    private ObjectProperty<TimeSlot> timeSlotSelected;

    private LocalDate daySelected;

    @FXML
    private TabPane tabulador;
    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private Tab tab4;
    @FXML
    private Tab tab5;
    @FXML
    private Tab tab6;

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
                registerHandlers(timeSlot);
                grid.add(timeSlot.getView(), 1, slotIndex);
            }
            slotIndex++;
        }
    }

    private void registerHandlers(TimeSlot timeSlot) {
        timeSlot.getView().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (timeSlot.isReserved()) {
                if (timeSlot.getMember().getNickName().equals(member.getNickName())) {
                    showReservationDialogForMember(timeSlot);
                } else {
                    showReservationDialogForOtherMember(timeSlot);
                }
            } else {
                showConfirmationDialog(timeSlot);
            }
        });
    }

    private void showConfirmationDialog(TimeSlot timeSlot) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirmar reserva");
        dialog.setHeaderText("¿Desea reservar esta pista?");
        dialog.setContentText("Pista disponible");

        ButtonType yesButton = new ButtonType("Sí");
        ButtonType noButton = new ButtonType("No");

        dialog.getDialogPane().getButtonTypes().addAll(yesButton, noButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            if (canReserveTwoHoursInARow()) {
                timeSlot.setReserved(true, member);
                if (member.checkHasCreditInfo()) {
                    timeSlot.setPaid(true);
                }
            } else {
                showAlert("Error de reserva", "No se puede reservar más de 2 horas seguidas en la misma pista.");
            }
        }
    }

    private void showReservationDialogForMember(TimeSlot timeSlot) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirmar reserva");
        dialog.setHeaderText("La pista está reservada por " + timeSlot.getMember().getNickName());
        if (canCancelReservation(timeSlot)) {
            dialog.setContentText("¿Desea cancelar la reserva?");
            ButtonType cancelButton = new ButtonType("Cancelar reserva");
            ButtonType closeButton = new ButtonType("Cerrar");
            dialog.getDialogPane().getButtonTypes().addAll(cancelButton, closeButton);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == cancelButton) {
                timeSlot.setReserved(false, null);
            }
        } else {
            dialog.setContentText("La pista no puede ser cancelada.");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        }
    }

    private void showReservationDialogForOtherMember(TimeSlot timeSlot) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirmar reserva");
        dialog.setHeaderText("La pista está reservada por " + timeSlot.getMember().getNickName());
        dialog.setContentText("La pista no está disponible.");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private boolean canCancelReservation(TimeSlot timeSlot) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursAhead = now.plusHours(24);
        return timeSlot.getStart().isAfter(twentyFourHoursAhead);
    }

    private boolean canReserveTwoHoursInARow() {
        int consecutiveSlots = 0;
        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.isReserved() && timeSlot.getMember().getNickName().equals(member.getNickName())) {
                consecutiveSlots++;
                if (consecutiveSlots >= 2) {
                    return false;
                }
            } else {
                consecutiveSlots = 0;
            }
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void volverPrincipio(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PaginaPrincipal.fxml"));
            Parent root = cargador.load();
            PaginaPrincipal pagprin = cargador.getController();
            pagprin.GetProfile(nickname, password, clubR);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Club de Tenis " + clubR.getName());
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

        private final BooleanProperty reserved = new SimpleBooleanProperty(false);
        private Member member;
        private boolean paid;

        public final BooleanProperty reservedProperty() {
            return reserved;
        }

        public final boolean isReserved() {
            return reservedProperty().get();
        }

        public final void setReserved(boolean reserved, Member member) {
            reservedProperty().set(reserved);
            this.member = member;
        }

        public TimeSlot(LocalDateTime start, Duration duration) {
            this.start = start;
            this.duration = duration;
            view = new Pane();
            view.getStyleClass().add("time-slot");
            reservedProperty().addListener((obs, wasReserved, isReserved)
                    -> updateViewStyle());
        }

        private void updateViewStyle() {
            view.getStyleClass().removeAll("available", "reserved");
            if (isReserved()) {
                view.getStyleClass().add("reserved");
            } else {
                view.getStyleClass().add("available");
            }
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

        public Member getMember() {
            return member;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public Node getView() {
            return view;
        }
    }

    private void GetDatos() {
        nickname = member.getNickName();
        password = member.getPassword();
    }

    public void GetDatos(String nickname, String password, Club club) {
        member = club.getMemberByCredentials(nickname, password);
        clubR = club;
        GetDatos();
    }
}