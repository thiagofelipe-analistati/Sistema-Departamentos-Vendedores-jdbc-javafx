package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {

		public static void showAlert(String title, String header, String content, AlertType type) {
			Alert alert = new Alert(type);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(content);
			alert.show();
		}
		
		public static Optional<ButtonType> showConfirma��o(String Titulo, String texto){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(Titulo);
			alert.setHeaderText(null);
			alert.setContentText(texto);
			return alert.showAndWait();
		}
	}

