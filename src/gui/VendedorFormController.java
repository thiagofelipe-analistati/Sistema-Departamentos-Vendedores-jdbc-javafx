package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listenrs.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entidades.Departamento;
import model.entidades.Vendedor;
import model.exceptions.ValidacaoException;
import model.service.DepartamentoService;
import model.service.VendedorService;

public class VendedorFormController implements Initializable {
	// atributo de injeção de dependência
	private Vendedor entidade;
	// atributo de injeção de dependência do dao
	private VendedorService service;
	// atributo de injeção de dependência do deparatamento
	private DepartamentoService departamentoService;
	// atribuição da dependencia do padrão obeserver
	private List<DataChangeListener> dataChangeListerners = new ArrayList<>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpbirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;
	@FXML
	private Label labelErroNome;
	@FXML
	private Label labelErroEmail;
	@FXML
	private Label labelErroBirthDate;
	@FXML
	private Label labelErroBaseSalary;
	@FXML
	private Button tbSave;
	@FXML
	private Button tbCancel;

	private ObservableList<Departamento> obsList;

	@FXML
	public void onBtSalveAction(ActionEvent event) {
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
		} catch (ValidacaoException e) {
			setErrorMessagem(e.getErros());
		}
	}

	private void notificacaoDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListerners) {
			listener.onDataChanged();
		}

	}

	private Vendedor getFormData() {
		// pega o obj ddepartamento e joga no metodo
		Vendedor obj = new Vendedor();
		ValidacaoException excecao = new ValidacaoException("Validação Erro");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.addError("Nome", "O campo nomde deve ser preenchido! ");
		}
		obj.setNome(txtNome.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			excecao.addError("Email", "O campo nomde deve ser preenchido! ");
		}
		obj.setEmail(txtEmail.getText());
		if(dpbirthDate.getValue()==null) {
			excecao.addError("birthDate", "O campo nomde deve ser preenchido! ");
		}else {
		Instant instant = Instant.from(dpbirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataNasc(Date.from(instant));
		}
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			excecao.addError("baseSalary", "O campo nomde deve ser preenchido! ");
		}
		obj.setSalarioBase(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartamento(comboBoxDepartamento.getValue());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setServices(VendedorService service, DepartamentoService departamentoService) {
		this.service = service;
		this.departamentoService = departamentoService;
	}

	// set da dependencia dão
	public void setVendedor(Vendedor entidade) {
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
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpbirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entidade.getSalarioBase()));
		if (entidade.getDataNasc() != null) {
			dpbirthDate.setValue(LocalDate.ofInstant(entidade.getDataNasc().toInstant(), ZoneId.systemDefault()));
		}
		if(entidade.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().getSelectedItem();
		} else {
		comboBoxDepartamento.setValue(entidade.getDepartamento());
		}
	}

	public void LoadAssociacaoObejetos() {
		List<Departamento> list = departamentoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartamento.setItems(obsList);
	}

	private void setErrorMessagem(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		
		labelErroNome.setText(fields.contains("Nome") ? erros.get("Nome") : "" );
		labelErroEmail.setText(fields.contains("Email") ? erros.get("Email") : "" );
		labelErroBaseSalary.setText(fields.contains("baseSalary") ? erros.get("baseSalary") : "" );
		labelErroBirthDate.setText(fields.contains("birthDate") ? erros.get("birthDate") : "" );
		/*if (fields.contains("Nome")) {
			labelErroNome.setText(erros.get("Nome"));
		}
		if (fields.contains("Email")) {
			labelErroEmail.setText(erros.get("Email"));
		}
		if (fields.contains("baseSalary")) {
			labelErroBaseSalary.setText(erros.get("baseSalary"));
		}
		if (fields.contains("birthDate")) {
			labelErroBirthDate.setText(erros.get("birthDate"));
		}*/
		
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}
}
