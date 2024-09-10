package Control;

import java.io.IOException;
import java.util.List;

import Model.DaoSala;
import Model.Salas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControllerSalas { // Classe feita para chamar os botões das salas

	DaoSala<Salas> Daosala = new DaoSala<>(Salas.class);

	@FXML
	private VBox regiaoCentralSalas;
	
	@FXML
	public void initialize() {

		List<Salas> salas = Daosala.obterSalas();

		for (Salas salas2 : salas) {

			HBox HboxChild = new HBox();
			HboxChild.getStyleClass().add("HboxChildSalas");

			Button ButtonSala = new Button("Número da sala: " + Integer.toString(salas2.getNumeroSala()));
			ButtonSala.getStyleClass().add("LabelSalas");

			ButtonSala.setOnAction(e -> {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FilmeXSala.fxml"));
					Parent subPage = loader.load();
					
					String css = this.getClass().getResource("/View/TelaSessao.css").toExternalForm();
			        subPage.getStylesheets().add(css);

					regiaoCentralSalas.getChildren().clear();
					regiaoCentralSalas.getChildren().add(subPage);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});

			HboxChild.getChildren().add(ButtonSala);

			regiaoCentralSalas.getChildren().add(HboxChild);
			
			System.out.println(regiaoCentralSalas.getChildren());
		}
	}
}
