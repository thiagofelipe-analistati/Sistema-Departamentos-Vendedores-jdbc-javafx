package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartamentoFormController implements Initializable {

	
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
	public void onBtSalveAction(){
		System.out.println("salve");
	}
	@FXML
	public void onBtCancelAction(){
		System.out.println("Cancel");
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

}
