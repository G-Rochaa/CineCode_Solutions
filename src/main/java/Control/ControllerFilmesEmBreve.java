package Control;

import java.io.IOException;
import java.util.List;

import Model.DaoFilme;
import Model.Filme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerFilmesEmBreve {

	private VBox regiaoCentral;

	@FXML
	private VBox VboxFilmesCartaz;

	@FXML
	private HBox HBoxTituloConteiner;

	@FXML
	private Label HBoxTituloLabel;

	@FXML
	private VBox VboxFilmes;

	DaoFilme<Filme> daoFilme = new DaoFilme<>(Filme.class);

	List<Filme> filmes = daoFilme.obterTodosEmBreve();

	public VBox getRegiaoCentral() {
		return regiaoCentral;
	}

	public void setRegiaoCentral(VBox regiaoCentral) {
		this.regiaoCentral = regiaoCentral;
	}

	@FXML
	public void initialize() {
		HBoxTituloLabel.setText("Filmes em Breve");

		for (Filme filme : filmes) {
			HBox HboxChild = new HBox();
			HboxChild.getStyleClass().add("HboxChild");

			HBox HboxLabel = new HBox();
			HboxLabel.getStyleClass().add("HboxLabel");

			Label labelFilme = new Label(filme.getNome());
			labelFilme.getStyleClass().add("labelFilme");
			HboxLabel.getChildren().add(labelFilme);

			HBox HboxButtons = new HBox();
			HboxButtons.getStyleClass().add("HboxButtons");

			Button buttonUpdate = new Button("Editar");
			buttonUpdate.setOnAction(e -> {
				try {

					FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditarFilmesEmBreve.fxml"));
					Parent subPage = loader.load();

					ControllerEditarFilmesEmBreve controller = loader.getController();
					controller.initialize(filme);
					controller.setVboxFilmesCartaz(regiaoCentral);

					String css = this.getClass().getResource("/View/EditarFilmesEmBreve.css").toExternalForm();
					subPage.getStylesheets().add(css);

					regiaoCentral.getChildren().clear();
					regiaoCentral.getChildren().add(subPage);

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
			Button buttonDelete = new Button("Remover");
			buttonDelete.setOnAction(e -> {
				Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
				confirmaDelete(stage, filme);
			});

			HboxButtons.getChildren().addAll(buttonUpdate, buttonDelete);

			if (filmes.size() > 10) {
				HboxLabel.setStyle("-fx-pref-width: 580px;");
				labelFilme.setStyle("-fx-pref-width: 580px;");
				HboxButtons.setStyle("-fx-spacing: 15px;");
			}

			HboxChild.getChildren().addAll(HboxLabel, HboxButtons);
			VboxFilmes.getChildren().add(HboxChild);
		}

		if (filmes.size() > 10) {
			ScrollPane scrollPane = new ScrollPane(VboxFilmes);
			scrollPane.setFitToWidth(true);
			scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
			VboxFilmesCartaz.getChildren().addAll(HBoxTituloConteiner, scrollPane);
		} else {
			VboxFilmesCartaz.getChildren().addAll(HBoxTituloConteiner, VboxFilmes);
		}
	}

	private void confirmaDelete(Stage owner, Filme filme) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.initOwner(owner);
		alert.setTitle("Confirmar Exclusão");
		alert.setHeaderText("ATENÇÃO! Esse filme será desativado confirma a operação?");

		ButtonType buttonYes = new ButtonType("Sim");
		ButtonType buttonNo = new ButtonType("Não");

		alert.getButtonTypes().setAll(buttonYes, buttonNo);

		alert.showAndWait().ifPresent(response -> {

			if (response == buttonYes) {
				daoFilme.removerFilme(filme);

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
		});
	}

}
