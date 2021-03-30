package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listenrs.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entidades.Departamento;
import model.service.DepartamentoService;

public class DepartamentoFormController implements Initializable {
	// atributo de injeção de dependência
	private Departamento entidade;
	// atributo de injeção de dependência do dao
	private DepartamentoService service;
	// atribuição da dependencia do padrão obeserver
	private List<DataChangeListener> dataChangeListerners = new ArrayList<>();
	
	@FXML 
	private TextField txtId;
	@FXML 
	private TextField txtNome;
	@FXML 
	private Label labelErroNome;
	@FXML 
	private Button tbSave;
	@FXML 
	private Button tbCancel;
	@FXML
	public void onBtSalveAction(ActionEvent event){
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("serviço nula");
		}
		try {
			// pegar o formulário e instanciar com o metado 
			entidade = getFormData();
			// chamad a dependica service para instanciação do obejto.
			service.saveorUpdate(entidade);
			notificacaoDataChangeListeners();
			// fechando a janela 
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro no salvamento do Obj", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private void notificacaoDataChangeListeners() {
		for (DataChangeListener listener: dataChangeListerners) {
			listener.onDataChanged();
		}
		
	}
	private Departamento getFormData() {
		// pega o obj ddepartamento e joga no metodo
		Departamento obj = new Departamento();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		return obj;
	}
	@FXML
	public void onBtCancelAction(ActionEvent event){
		Utils.currentStage(event).close();
	}
	
	
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}
	// set da dependencia dão
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListerners.add(listener);
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initialibleNodes();
		
	}
	// fazendo os restrições dos campos TEXTFIEDS
	private void initialibleNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void updateFormData () {
		if(entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
	}
	
}
