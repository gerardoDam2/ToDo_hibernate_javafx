package dad.todo.ui.eventos;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;

import dad.calendario.CalendarioController;
import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.ui.ToDoController;
import dad.todo.ui.model.EventosModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class EventosController implements Initializable {

	@FXML
	private VBox rightPanel;

	@FXML
	private BorderPane eventsPane;

	@FXML
	private JFXListView<EventosModel> eventosListView;

	@FXML
	private JFXDatePicker fechaEventosDatePicker;

	@FXML
	private BorderPane calendarContainer;
	private SplitPane view;

	private ListProperty<EventosModel> eventos;

	private CrearEditarEventosController editarCrearController;

	private CalendarioController calendarioController;

	private SetProperty<LocalDate> diasConEventos;

	public EventosController() {

		diasConEventos = new SimpleSetProperty<>(this, "diasConEventos", FXCollections.observableSet());

		try {
			diasConEventos.addAll(ServiceFactory.getEventosService().getEventos().stream()
					.map(EventosController::itemToLocalDate).collect(Collectors.toList()));
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		eventos = new SimpleListProperty<>(this, "eventos", FXCollections.observableArrayList());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventosView.fxml"));
			loader.setController(this);
			view = loader.load();
			view.getStylesheets().add(getClass().getResource("../todoStyle.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		calendarioController = new CalendarioController();
		calendarContainer.setCenter(calendarioController);
		calendarioController.selectedDayProperty()
				.addListener((obs, oldV, newV) -> fechaEventosDatePicker.setValue(newV));

		calendarioController.specialDaysProperty().bind(diasConEventos);
		fechaEventosDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> onFechaChange(newValue));
		fechaEventosDatePicker.setValue(LocalDate.now());

		eventosListView.itemsProperty().bind(eventos);

		eventos.sizeProperty().addListener(e -> {
			updateDiasConEventosList();
		});
		BorderPane borderpane = new BorderPane();
		ImageView imagev = new ImageView(new Image(getClass().getResource("sinEventos.gif").toExternalForm()));
		borderpane.setBottom(imagev);
		borderpane.setAlignment(imagev, Pos.CENTER);
		eventosListView.setPlaceholder(borderpane);

		editarCrearController = new CrearEditarEventosController(this);

	}

	private void editarEvento(EventosModel evento) {
		editarCrearController.initEdit(evento);
		changeViewToEditEvent();

	}

	private void onFechaChange(LocalDate newValue) {

		Date fecha = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());

		eventos.clear();
		try {
			List<EventoItem> eventosItem = ServiceFactory.getEventosService().buscarEventosPorFecha(fecha);
			eventos.clear();
			eventos.addAll(eventosItem.stream().map(e -> EventosModel.fromItem(e, this)).collect(Collectors.toList()));
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private Button addEventoButton;

	@FXML
	void onAddEventoButtonAction(ActionEvent event) {
		editarCrearController.initCreateEvent(fechaEventosDatePicker.getValue());
		changeViewToEditEvent();
	}

	public SplitPane getView() {
		return view;
	}

	public void changeViewToEventsList(LocalDate f) {
		if (f == null) {
			onFechaChange(fechaEventosDatePicker.getValue());
		} else {
			onFechaChange(f);
		}

		rightPanel.getChildren().remove(0);
		rightPanel.getChildren().add(eventsPane);
		updateDiasConEventosList();
	}

	public void onEditEventAction(EventosModel evento) {
		editarCrearController.initEdit(evento);
		changeViewToEditEvent();
	}

	public void changeViewToEditEvent() {
		rightPanel.getChildren().remove(0);
		rightPanel.getChildren().add(editarCrearController.getView());
	}

	private void updateDiasConEventosList() {
		try {
			diasConEventos.clear();
			diasConEventos.addAll(ServiceFactory.getEventosService().getEventos().stream()
					.map(EventosController::itemToLocalDate).collect(Collectors.toList()));
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}

	}

	private static LocalDate itemToLocalDate(EventoItem eventoItem) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(eventoItem.getFecha());
		LocalDate fechaAux = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		return fechaAux;
	}
}