package Control;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import Model.DaoFilme;
import Model.Filme;
import Model.FormataHoras;
import Model.Genero;
import View.NotificationManager;
import View.NotificationManager.NotificationType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;

public class ControllerEditarFilmesEmBreve {
	private VBox regiaoCentral;

	@FXML
	private VBox vboxEditarFather;

	@FXML
	private TextField inputNomeFilme, inputClassificacao, inputAutor, inputDuracao, inputAnoLancamento;

	@FXML
	private ChoiceBox<String> inputGenero;

	@FXML
	private TextArea inputSinopse;

	@FXML
	private Button btnSalvar, btnCancelar;

	private Filme filme;

	private Genero generoSelecionado = null;
	
	@FXML
	private CheckBox ativaFilme;

	DaoFilme<Filme> daoFilme = new DaoFilme<>();

	FormataHoras horaFormt = new FormataHoras();

	public Filme getFilme() {
		return filme;
	}

	public void setFilme(Filme filme) {
		this.filme = filme;
	}

	public VBox getVboxFilmesCartaz() {
		return regiaoCentral;
	}

	public void setVboxFilmesCartaz(VBox vboxFilmesCartaz) {
		regiaoCentral = vboxFilmesCartaz;
	}

	public void initialize(Filme filme) {
		this.filme = filme;
		
		olocarDadosNosInputs();
	}

	public void olocarDadosNosInputs() {
		inputNomeFilme.setText(filme.getNome());
		formataInputClassificao(inputClassificacao);
		inputClassificacao.setText(filme.getClassificacao());
		inputAutor.setText(filme.getAutor());
		inputDuracao.setText(filme.getDuracao().toString());
		horaFormt.formataHora(inputDuracao);
		inputAnoLancamento.setText(filme.getAnoLancamento().toString());
		inputSinopse.setText(filme.getSinopse());
		daoFilme.inputGeneroOpcoes(inputGenero);
		inputGenero.setValue(filme.getGenero().getNome_genero());

		String nomeGeneroSelecionado = inputGenero.getValue();

		generoSelecionado = daoFilme.obterGeneroPorNome(nomeGeneroSelecionado);

		inputGenero.getSelectionModel().selectedItemProperty().addListener((obs, oldGenero, newGenero) -> {
			String nomeGeneroSelecionado2 = inputGenero.getValue();
			generoSelecionado = daoFilme.obterGeneroPorNome(nomeGeneroSelecionado2);
		});
	}

	public void salvar() {
	
		daoFilme.updateFilme(filme, inputNomeFilme.getText(), inputClassificacao.getText(), generoSelecionado,
				inputSinopse.getText(), inputAutor.getText(),
				(Date) convertString(inputAnoLancamento.getText(), Date.class),
				(Time) convertString(inputDuracao.getText(), Time.class), ativaFilme);
		carregaTelaAnterior();
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
				// Validação simples do formato de tempo
				if (!input.matches("\\d{2}:\\d{2}:\\d{2}")) {
					NotificationManager.showNotification("Erro", "Formato de hora inválido. Use HH:mm:ss.",
							NotificationType.ERROR);
					return null;
				}

				return Time.valueOf(input);

			} else {
				throw new IllegalArgumentException("Tipo de conversão não suportado: " + targetType.getName());
			}
		} catch (ParseException e) {
			NotificationManager.showNotification("Erro", "Formato de data inválido. Use o formato yyyy-MM-dd.",
					NotificationType.ERROR);
			return null;
		} catch (IllegalArgumentException e) {
			NotificationManager.showNotification("Erro", "Erro de conversão: " + e.getMessage(),
					NotificationType.ERROR);
			return null;
		}
	}

	public void carregaTelaAnterior() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FilmesEmBreve.fxml"));
			Parent secondPage = loader.load();

			ControllerFilmesEmBreve ControllerFilmesEmBreve = loader.getController();
			ControllerFilmesEmBreve.setRegiaoCentral(regiaoCentral);

			regiaoCentral.getChildren().clear();

			regiaoCentral.getChildren().add(secondPage);

		} catch (IOException ex) {
			ex.printStackTrace();
			NotificationManager.showNotification("Erro", "Falha ao carregar a tela de filmes em cartaz.",
					NotificationType.ERROR);
		}
	}
	
	public void formataInputClassificao(TextField textField) {
		textField.setTextFormatter(new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			// Permite apenas números (dígitos de 0 a 9)
			if (newText.matches("\\d*")) {
				return change; 
			}

			NotificationManager.showNotification("Erro", "O Campo CLASSIFICAÇÃO não aceita caracteres não numéricos!",
					NotificationType.ERROR);
			return null; 
		}));
	}
}
