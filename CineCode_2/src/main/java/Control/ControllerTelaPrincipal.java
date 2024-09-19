package Control;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import Model.DaoFilme;
import Model.Filme;
import Model.Genero;
import View.NotificationManager;
import View.NotificationManager.NotificationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerTelaPrincipal {

	DaoFilme<Filme> daoFilme = new DaoFilme<>(Filme.class);

	List<Filme> filmes;

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

	private String titulo = "Filmes em Cartaz";

	private String titulo2 = "Filmes em Breve";

	private Genero generoSelecionado = null;

	private LocalDate anoLancamento;

	// MÉTODOS PRINCIPAIS

	@FXML
	private void initialize() {
		filmesCartaz.setOnAction(e -> {
			tipoFilme(e);
			mostrarFilmes(titulo);
		});

		filmesEmBreve.setOnAction(e -> {
			tipoFilme(e);
			mostrarFilmes(titulo2);
		});

		InserirFilme.setOnAction(e -> {
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
		        // Exibir uma notificação de erro ou mensagem
		    }
		});

		Sessao.setOnAction(e -> {
			mostrarSalas();
		});
	}


	@FXML
	private void mostrarFilmes(String titulo) {

		regiaoCentral.getChildren().clear();

		VBox VboxFilmesCartaz = new VBox();
		VboxFilmesCartaz.getStyleClass().add("VboxFilmesCartaz");

		HBox HBoxTituloConteiner = new HBox();
		HBoxTituloConteiner.getStyleClass().add("HBoxTituloConteiner");
		HBoxTituloConteiner.setMaxHeight(100);

		Label HBoxTituloLabel = new Label(titulo);
		HBoxTituloLabel.getStyleClass().add("HBoxTituloLabel");

		HBoxTituloConteiner.getChildren().add(HBoxTituloLabel);

		VBox VboxFilmes = new VBox();
		VboxFilmes.getStyleClass().add("VboxFilmes");

		for (Filme filme2 : filmes) {

			HBox HboxChild = new HBox();
			HboxChild.getStyleClass().add("HboxChild");

			HBox HboxLabel = new HBox();
			HboxLabel.getStyleClass().add("HboxLabel");

			Label labelFilme = new Label(filme2.getNome());
			labelFilme.getStyleClass().add("labelFilme");
			HboxLabel.getChildren().add(labelFilme);

			HBox HboxButtons = new HBox();
			HboxButtons.getStyleClass().add("HboxButtons");

			Button buttonUpdate = new Button("Editar");
			buttonUpdate.setOnAction(e -> editar(filme2));
			Button buttonDelete = new Button("Remover");
			buttonDelete.setOnAction(e -> {
				Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém o Stage atual
				confirmaDelete(stage, filme2);
			});
			HboxButtons.getChildren().addAll(buttonUpdate, buttonDelete);

			if (filmes.size() > 10) {
				HboxLabel.setStyle("-fx-pref-width: 580px;");
				labelFilme.setStyle("-fx-pref-width: 580px;");
				HboxButtons.setStyle("-fx-spacing: 15px;");
			}

			HboxChild.getChildren().addAll(HboxLabel, HboxButtons);
			VboxFilmes.getChildren().add(HboxChild); // Adiciona HboxChild ao VboxFilmes
		}

		if (filmes.size() > 10) {
			ScrollPane scrollPane = new ScrollPane(VboxFilmes);
			scrollPane.setFitToWidth(true); // Ajusta a largura do ScrollPane
			scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
			VboxFilmesCartaz.getChildren().addAll(HBoxTituloConteiner, scrollPane); // Adiciona o ScrollPane ao VBox
																					// principal
		} else {
			VboxFilmesCartaz.getChildren().addAll(HBoxTituloConteiner, VboxFilmes); // Adiciona os filmes diretamente
		}

		regiaoCentral.getChildren().add(VboxFilmesCartaz); // Adiciona VboxFilmesCartaz a regiaoCentral
	}

	private void editar(Filme filme) {

		VBox accessedHBox = (VBox) regiaoCentral.getChildren().get(0);
		accessedHBox.getChildren().clear();

		VBox vboxEditarFather = new VBox();
		vboxEditarFather.getStyleClass().add("vboxEditarFather");

		accessedHBox.getChildren().add(vboxEditarFather);

		HBox forms1 = new HBox();
		Label nome_filme = new Label("Nome do filme: ");
		TextField inputNomeFilme = new TextField(filme.getNome()); // Nome
		forms1.getChildren().addAll(nome_filme, inputNomeFilme);
		forms1.getStyleClass().add("forms");
		nome_filme.getStyleClass().add("LabelEditar");
		inputNomeFilme.getStyleClass().add("inputLabel");

		HBox forms2 = new HBox();
		Label classificacao = new Label("Classificacao: "); // Classificação
		TextField inputClassificacao = new TextField(filme.getClassificacao());
		forms2.getChildren().addAll(classificacao, inputClassificacao);
		forms2.getStyleClass().add("forms");
		classificacao.getStyleClass().add("LabelEditar");
		inputClassificacao.getStyleClass().add("inputLabel");

		HBox forms3 = new HBox();
		Label genero = new Label("Gênero ");
		ChoiceBox<String> inputGenero = new ChoiceBox<>();
		inputGeneroOpcoes(inputGenero);
		inputGenero.setValue(filme.getGenero().getNome_genero());
		forms3.getChildren().addAll(genero, inputGenero);
		forms3.getStyleClass().add("forms");
		genero.getStyleClass().add("LabelEditar");
		inputGenero.getStyleClass().add("inputLabel");

		String nomeGeneroSelecionado = inputGenero.getValue();

		generoSelecionado = daoFilme.obterGeneroPorNome(nomeGeneroSelecionado);

		inputGenero.getSelectionModel().selectedItemProperty().addListener((obs, oldGenero, newGenero) -> {
			String nomeGeneroSelecionado2 = inputGenero.getValue();
			generoSelecionado = daoFilme.obterGeneroPorNome(nomeGeneroSelecionado2);
		});

		HBox forms4 = new HBox();
		Label sinopse = new Label("Sinopse: ");
		TextArea inputSinopse = new TextArea(filme.getSinopse()); // sinopse
		forms4.getChildren().addAll(sinopse, inputSinopse);
		forms4.getStyleClass().add("forms");
		sinopse.getStyleClass().add("LabelEditar");
		inputSinopse.getStyleClass().add("inputLabel");

		HBox forms5 = new HBox();
		Label autor = new Label("Autor: ");
		TextField inputAutor = new TextField(filme.getAutor()); // Autor
		forms5.getChildren().addAll(autor, inputAutor);
		forms5.getStyleClass().add("forms");
		autor.getStyleClass().add("LabelEditar");
		inputAutor.getStyleClass().add("inputLabel");

		HBox forms6 = new HBox();
		Label duracao = new Label("Duração: ");
		TextField inputDuracao = new TextField(filme.getDuracao().toString()); // Duração
		forms6.getChildren().addAll(duracao, inputDuracao);
		forms6.getStyleClass().add("forms");
		duracao.getStyleClass().add("LabelEditar");
		inputDuracao.getStyleClass().add("inputLabel");

		HBox forms7 = new HBox();
		Label anoLancamento = new Label("Ano de Lançamento: ");
		TextField inputAnoLancamento = new TextField(filme.getAnoLancamento().toString());
		forms7.getChildren().addAll(anoLancamento, inputAnoLancamento);
		forms7.getStyleClass().add("forms"); // Ano de Lançamento
		anoLancamento.getStyleClass().add("LabelEditar");
		inputAnoLancamento.getStyleClass().add("inputLabel");

		formataInputClassificao(inputClassificacao);

		HBox fomrs8 = new HBox();
		Button salvar = new Button("Salvar");
		salvar.setOnAction(e -> {
			daoFilme.updateFilme(filme, inputNomeFilme.getText(), inputClassificacao.getText(), generoSelecionado,
					inputSinopse.getText(), inputAutor.getText(),
					(Date) convertString(inputAnoLancamento.getText(), Date.class),
					(Time) convertString(inputDuracao.getText(), Time.class));
			mostrarFilmes(titulo);
		});
		Button cancelar = new Button("Cancelar");
		cancelar.setOnAction(e -> {
			mostrarFilmes(titulo);
		});
		fomrs8.getStyleClass().add("forms");
		fomrs8.getChildren().addAll(salvar, cancelar);
		fomrs8.getStyleClass().add("forms5");

		vboxEditarFather.getChildren().addAll(forms1, forms2, forms3, forms4, forms5, forms6, forms7, fomrs8);
		// Adicionar os novos HBox ao vboxEditarFather

	}
	
	private void mostrarSalas() {
	    try {
	        // Carregar o novo FXML
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TelaSalas.fxml"));
	        Parent subPage = loader.load();
	        
	        String css = this.getClass().getResource("/View/TelaSalas.css").toExternalForm();
	        subPage.getStylesheets().add(css);
	        
	        // Obter o controlador, se necessário		        
	        regiaoCentral.getChildren().clear(); 
	        regiaoCentral.getChildren().add(subPage); 

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
}

	// MÉTODOS SECUNDÁRIOS

	private void tipoFilme(ActionEvent event) {
		Button sourceButton = (Button) event.getSource();

		if (sourceButton == filmesCartaz) {
			filmes = daoFilme.obterTodosEmCartaz();
		} else if (sourceButton == filmesEmBreve) {
			filmes = daoFilme.obterTodosEmBreve();
		}
	}

	private void confirmaDelete(Stage owner, Filme filme) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.initOwner(owner); // Associa o alerta à janela principal
		alert.setTitle("Confirmar Exclusão");
		alert.setHeaderText("ATENÇÃO! Esse filme será desativado confirma a operação?");

		ButtonType buttonYes = new ButtonType("Sim");
		ButtonType buttonNo = new ButtonType("Não");

		alert.getButtonTypes().setAll(buttonYes, buttonNo);

		alert.showAndWait().ifPresent(response -> {
			int status_filme = filme.getStatusFilme();

			if (response == buttonYes) {
				daoFilme.removerFilme(filme);
				atualizarListaEInterface(status_filme);
			}
		});
	}

	private void atualizarListaEInterface(int status) {

		if (status == 1) {
			tipoFilme(new ActionEvent(filmesCartaz, null)); // Atualiza a lista de filmes em cartaz
		} else {
			tipoFilme(new ActionEvent(filmesEmBreve, null)); // Atualiza a lista de filmes em breve
		}
		mostrarFilmes(titulo);
	}


	// MÉTODOS DE TRATAMENTO DOS INPUTS

	public void inputGeneroOpcoes(ChoiceBox<String> choiceBox) {
		List<String> listaGeneros = daoFilme.obterNomesGeneros();
		ObservableList<String> observableGeneros = FXCollections.observableArrayList(listaGeneros);
		choiceBox.setItems(observableGeneros);
	}

	public Object convertString(String input, Class<?> targetType) {
		if (input == null || input.isEmpty()) {
			return null;
		}

		try {
			if (targetType == Date.class) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date utilDate = formatter.parse(input);

				return new java.sql.Date(utilDate.getTime());

			} else if (targetType == Time.class) {
				return Time.valueOf(input);

			} else {
				throw new IllegalArgumentException("Tipo de conversão não suportado: " + targetType.getName());
			}
		} catch (ParseException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public void formataInputClassificao(TextField textField) {
		textField.setTextFormatter(new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			// Permite apenas números (dígitos de 0 a 9)
			if (newText.matches("\\d*")) {
				return change; // Aceita a mudança
			}

			NotificationManager.showNotification("Erro", "O Campo CLASSIFICAÇÃO não aceita caracteres não numéricos!",
					NotificationType.ERROR);
			return null; // Rejeita a mudança se não for número
		}));
	}

	public static Date convertLocalDateToSqlDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return Date.valueOf(localDate);
	}
}