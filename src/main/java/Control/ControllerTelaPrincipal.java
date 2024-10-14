package Control;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ControllerTelaPrincipal {

	@FXML
	private VBox regiaoCentral;

	@FXML
	private Button filmesCartaz;

	@FXML
	private Button filmesEmBreve;

	@FXML
	private Button InserirFilme;

	@FXML
	private Button Sessao;
	
	@FXML
	private Button editSessao;

	@FXML
	private void initialize() {
		filmesCartaz.setOnAction(e -> {
			mostrarFilmesEmCartaz();
		});

		filmesEmBreve.setOnAction(e -> {
			mostrarFilmesEmBreve();
		});

		InserirFilme.setOnAction(e -> {
			mostrarCadastroFilmes();
		});

		Sessao.setOnAction(e -> {
			mostrarSalas();
		});
		
		editSessao.setOnAction(e -> {
			mostrarSessoes();
		});
	} 

	private void mostrarFilmesEmCartaz() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FilmesEmCartaz.fxml"));
			Parent subPage = loader.load();

			String css = this.getClass().getResource("/View/FilmesEmCartaz.css").toExternalForm();
			subPage.getStylesheets().add(css);

			ControllerFilmesEmCartaz controller = loader.getController();
			controller.setRegiaoCentral(regiaoCentral);
			
			regiaoCentral.getChildren().clear();
			regiaoCentral.getChildren().add(subPage);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void mostrarFilmesEmBreve() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FilmesEmBreve.fxml"));
			Parent subPage = loader.load();

			String css = this.getClass().getResource("/View/FilmesEmBreve.css").toExternalForm();
			subPage.getStylesheets().add(css);

			ControllerFilmesEmBreve controller = loader.getController();
			controller.setRegiaoCentral(regiaoCentral);

			regiaoCentral.getChildren().clear();
			regiaoCentral.getChildren().add(subPage);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void mostrarCadastroFilmes() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CadastroFilme.fxml"));
			Parent subPage = loader.load();

			String css = this.getClass().getResource("/View/CadastroFilme.css").toExternalForm();
			subPage.getStylesheets().add(css);

			ControllerCadastroFilmes controller = loader.getController();

			regiaoCentral.getChildren().clear();
			regiaoCentral.getChildren().add(subPage);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void mostrarSalas() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TelaSalas.fxml"));
			Parent subPage = loader.load();

			String css = this.getClass().getResource("/View/TelaSalas.css").toExternalForm();
			subPage.getStylesheets().add(css);

			regiaoCentral.getChildren().clear();
			regiaoCentral.getChildren().add(subPage);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void mostrarSessoes() {
		// TODO Auto-generated method stub
		
	}
}