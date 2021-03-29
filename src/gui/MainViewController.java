package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.DepartamentoService;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItenVendedor;
	@FXML
	private MenuItem menuItenDepartamento;
	@FXML
	private MenuItem menuItenAbout;
	
	@FXML
	public void onMenuItemVendedor() {
		System.out.println("onMenuItemVendedor");
	}
	@FXML
	public void onMenuItemDepartamento() {
		LoadView2("/gui/DepartamentoList.fxml");
	}
	
	@FXML
	public void onMenuItemAbout() {
		LoadView("/gui/about.fxml");
	}
	
	
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	//synchronized garante que não seja interrompido nada.
	private synchronized void LoadView(String absoltename) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoltename));
		VBox newVBox = loader.load();
		
		// referencia para a cena principal
		Scene mainScene = Main.getMainScene();
		//referencia para Vbox principal 
		VBox mainVBOx =(VBox) ((ScrollPane) mainScene.getRoot()).getContent();
		
		Node mainMenu = mainVBOx.getChildren().get(0);
		mainVBOx.getChildren().clear();
		mainVBOx.getChildren().add(mainMenu);
		mainVBOx.getChildren().addAll(newVBox.getChildren());
		
		} catch (IOException e) {
			
			Alerts.showAlert("IOException", "Erro na leitura da tela ", e.getMessage(), AlertType.ERROR);
			
		}
	}


private synchronized void LoadView2(String absoltename) {
	try {
	FXMLLoader loader = new FXMLLoader(getClass().getResource(absoltename));
	VBox newVBox = loader.load();
	
	// referencia para a cena principal
	Scene mainScene = Main.getMainScene();
	//referencia para Vbox principal 
	VBox mainVBOx =(VBox) ((ScrollPane) mainScene.getRoot()).getContent();
	
	Node mainMenu = mainVBOx.getChildren().get(0);
	mainVBOx.getChildren().clear();
	mainVBOx.getChildren().add(mainMenu);
	mainVBOx.getChildren().addAll(newVBox.getChildren());
	
	DepartamentoListController controller = loader.getController();
	controller.setDepartamentoService(new DepartamentoService());
	controller.updateTableView();
	
	} catch (IOException e) {
		
		Alerts.showAlert("IOException", "Erro na leitura da tela ", e.getMessage(), AlertType.ERROR);
		
	}
}
}