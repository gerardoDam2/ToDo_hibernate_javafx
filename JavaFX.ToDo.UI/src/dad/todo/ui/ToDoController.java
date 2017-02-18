package dad.todo.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.ui.eventos.EventosController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ToDoController implements Initializable {
	
	
    @FXML
    private AnchorPane rootView;
    
	@FXML
	private JFXDrawer drawer;

	@FXML
	private JFXHamburger opcionesButton;

	private HamburgerBackArrowBasicTransition transition;

	@FXML
	private BorderPane contenidoPane;

	private AnchorPane view;

	private Stage stage;

	private static SimpleStringProperty styleSelected;
	

	private EventosController eventosController;

	private MenuController menuController;

	public static JFXSnackbar notificator;

	@FXML
	void onOpcionesMousePressed(MouseEvent event) {

		transition.setRate(transition.getRate() * -1);
		transition.play();

		if (drawer.isShown()) {
			drawer.close();
		} else
			drawer.open();
	}

	public ToDoController() {
		try {
			
			
			
			styleSelected = new SimpleStringProperty(this, "styleSelected",
					App.gestorDePropiedades.getPropiedades().getProperty("style"));
			styleSelected.addListener((obs, oldV, newV) -> onStyleChange(newV));

			FXMLLoader loader = new FXMLLoader(getClass().getResource("ToDoView.fxml"));
			loader.setController(this);
			view = loader.load();

			

			stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			JFXDecorator decorator = new JFXDecorator(stage, view);
			decorator.setCustomMaximize(false);
			Scene scene = new Scene(decorator);
			
			HBox d=(HBox)decorator.getChildren().get(0);
			d.getChildren().remove(0);
			d.getChildren().remove(0);
			d.getChildren().remove(0);
			Button LogoutButton = new Button();
			LogoutButton.setOnAction(oa->desloguear());
			LogoutButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("./images/logout-24.png"))));
			LogoutButton.getStyleClass().add("windowbutton");
			HBox leftContentUndecorator = new HBox(LogoutButton);
			leftContentUndecorator.setLayoutX(0);
			d.getChildren().add(0,leftContentUndecorator);
			HBox.setHgrow(leftContentUndecorator, Priority.ALWAYS);
			
			stage.setX(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageX")));
			stage.setY(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageY")));
			stage.setWidth(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageWidth")));
			stage.setHeight(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageHeight")));
			
			stage.getIcons().add(new Image(getClass().getResourceAsStream("./images/TodoList-64.png")));

			stage.setOnCloseRequest(close -> guardarConfig());

			App.gestorDePropiedades.ocupatedelCssPorMi(scene.getRoot());
			stage.setScene(scene);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void desloguear() {
		try {
			drawer.close();
			stage.close();
			ServiceFactory.getUsuariosService().logout();
			App.login.show();
		} catch (ServiceException e) {
		}
	}

	private void onStyleChange(String newV) {

		App.gestorDePropiedades.getPropiedades().put("style", newV);

		switch (newV) {
		case "light":
			break;
		case "dark":
			break;
		default:
			break;
		}

		
	}

	private void guardarConfig() {
		App.gestorDePropiedades.getPropiedades().setProperty("stageX", stage.getX() + "");
		App.gestorDePropiedades.getPropiedades().setProperty("stageY", stage.getY() + "");
		App.gestorDePropiedades.getPropiedades().setProperty("stageWidth", stage.getWidth() + "");
		App.gestorDePropiedades.getPropiedades().setProperty("stageHeight", stage.getHeight() + "");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		transition = new HamburgerBackArrowBasicTransition(opcionesButton);
		transition.setRate(-1);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
			BorderPane MenuView = loader.load();
			menuController =(MenuController)loader.getController();
			drawer.setSidePane(MenuView);
			eventosController = new EventosController();
			contenidoPane.setCenter(eventosController.getView());
			notificator = new JFXSnackbar();
			notificator.registerSnackbarContainer(rootView);
			AnchorPane.getTopAnchor(notificator);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BorderPane getContenidoPane() {
		return contenidoPane;
	}

	public AnchorPane getView() {
		return view;
	}

	public void show() {
		eventosController.load();
		menuController.load();
		stage.show();
	}

	public static SimpleStringProperty styleSelectedProperty() {
		return styleSelected;
	}

	public String getStyleSelected() {
		return styleSelectedProperty().get();
	}

	public void setStyleSelected(final String styleSelected) {
		styleSelectedProperty().set(styleSelected);
	}
	


}
