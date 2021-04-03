package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listenrs.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidades.Vendedor;
import model.service.DepartamentoService;
import model.service.VendedorService;

public class VendedorListController implements Initializable, DataChangeListener {

	private VendedorService service;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML
	private TableColumn<Vendedor, String> tableColumnName;
	@FXML
	private TableColumn<Vendedor, String> tableColumnEmail;
	@FXML
	private TableColumn<Vendedor, Date> tableColumnbirthDate;
	@FXML
	private TableColumn<Vendedor, Double> tableColumnBaseSalary;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnEdit;
	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnRemove;

	@FXML
	private Button btNew;

	private ObservableList<Vendedor> obsList;

	public void onBtNewAction(ActionEvent event) {

		Stage parentStage = Utils.currentStage(event);
		Vendedor obj = new Vendedor();
		creadteDialogForm(obj, "/gui/VendedorForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		inicializeNodes();

	}

	// ingeçao de depêndencia com a classe departamentoService.
	public void setVendedorService(VendedorService service) {
		this.service = service;
	}

	// usando o serviço
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço Nulo");
		}
		List<Vendedor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVendedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		
	}

	private void inicializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnbirthDate.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
		Utils.formatTableColumnDate(tableColumnbirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		// macete para tabela acompnhar a janela.
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	// ação de formulário de cadastro de derpatamentos
	private void creadteDialogForm(Vendedor obj, String absoltename, Stage parentStage) {
		try {
			// carregando a view de novo de partamento.
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoltename));
			Pane pane = loader.load();
			// ler o controlador da tela
			VendedorFormController controller = loader.getController();
			controller.setVendedor(obj);
			controller.setServices(new VendedorService(), new DepartamentoService());
			//chamando do combobox de departamento
			controller.LoadAssociacaoObejetos();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			// um palco na frente do palco

			Stage dialogStage = new Stage();
			// chamada da nova janela
			dialogStage.setTitle("Dados do Novo Vendedor");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Erro londging View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	// metodo de edição de cada linha com o departamento existente
	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> creadteDialogForm(obj, "/gui/VendedorForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	// adicioano o botão de remove em cada itens da lista de departamento
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void  removeEntity(Vendedor obj) {
		Optional<ButtonType> resultado = Alerts.showConfirmação("Confirmação", "Tem certeza que quer excluir?");
		if(resultado.get()== ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Serviço nullo");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro na exclusão do obj", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}
}
