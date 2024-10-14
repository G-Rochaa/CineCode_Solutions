package View;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PaginaPrincipal extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL arquivoFXML = getClass().getResource("/View/TelaPrincipal.fxml");
		String arquivoCss = getClass().getResource("/View/TelaPrincipal.css").toExternalForm();
		if (arquivoFXML == null) {
	        throw new RuntimeException("Arquivo FXML não encontrado.");
	    }
		
		AnchorPane raiz = FXMLLoader.load(arquivoFXML); // Pega o fxml e informa um layout
		
		Scene cenaP = new Scene(raiz);
		
		cenaP.getStylesheets().add(arquivoCss);
		primaryStage.setScene(cenaP);
        primaryStage.setFullScreen(true);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
