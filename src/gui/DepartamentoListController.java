package gui;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidades.Departamento;
import model.service.DepartamentoService;

public class DepartamentoListController implements Initializable {
	
	private DepartamentoService service;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departamento, String> tableColumnName;
	
	@FXML
	private Button  btNew;
	
	private ObservableList<Departamento> obsList;
	
	public void onBtNewAction(ActionEvent event){
		
		Stage parentStage = Utils.currentStage(event);
		Departamento obj = new Departamento();
		creadteDialogForm(obj,"/gui/DepartamentoForm.fxml", parentStage);
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		inicializeNodes();
		
		
	}
	// ingeçao de depêndencia com a classe departamentoService.
	public void setDepartamentoService (DepartamentoService service) {
		this.service = service;
	}
	// usando o serviço 
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Serviço Nulo");
		}
		List<Departamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}
		
	private void inicializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		// macete para tabela acompnhar a janela.
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	// ação de formulário de cadastro de derpatamentos
	private void creadteDialogForm (Departamento obj, String absoltename, Stage parentStage) {
		try {
			// carregando a view de novo de partamento.
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoltename));
			Pane pane = loader.load();
			// ler o controlador da tela
			DepartamentoFormController controller = loader.getController();
			controller.setDepartamento(obj);
			controller.setDepartamentoService(new DepartamentoService());
			controller.updateFormData();
			//um palco na frente do palco
			
			Stage dialogStage = new Stage();
			// chamada da nova janela
			dialogStage.setTitle("Dados do Novo Departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		}catch (IOException e) {
			Alerts.showAlert("IOException", "Erro londging View", e.getMessage(), AlertType.ERROR);
		}
	}

}
