package Control;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import Model.Classificacao;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;

public class ControllerEditarFilmeEmCartaz {

	private VBox regiaoCentral;

	@FXML
	private VBox vboxEditarFather;

	@FXML
	private TextField inputNomeFilme, inputDiretor, inputDuracao, inputDataLancamento;

	@FXML
	private ChoiceBox<String> inputGenero;

	@FXML
	private ChoiceBox<String> inputClassificacao;

	@FXML
	private TextArea inputSinopse;

	@FXML
	private Button btnSalvar, btnCancelar;

	private Filme filme;

	private Genero generoSelecionado = null;

	DaoFilme<Filme> daoFilme = new DaoFilme<>();

	FormataHoras horaFormt = new FormataHoras();

	private Classificacao classificacaoSelecionado = null;

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

		daoFilme.inputClassificaoOpcoes(inputClassificacao);
		inputClassificacao.setValue(filme.getClassificacao().getClassificacao());

		inputDiretor.setText(filme.getAutor());

		inputDuracao.setText(filme.getDuracao().toString());
		
		inputDuracao.setPromptText("HH:MM:SS");

		horaFormt.formataHora(inputDuracao);

		inputDataLancamento.setText(filme.getAnoLancamento().toString());

		inputSinopse.setText(filme.getSinopse());

		daoFilme.inputGeneroOpcoes(inputGenero);
		inputGenero.setValue(filme.getGenero().getNome_genero());
		
		String nomeGeneroSelecionado = inputGenero.getValue();

		generoSelecionado = daoFilme.obterGeneroPorNome(nomeGeneroSelecionado);

		inputGenero.getSelectionModel().selectedItemProperty().addListener((obs, oldGenero, newGenero) -> {
			String nomeGeneroSelecionado2 = inputGenero.getValue();
			generoSelecionado = daoFilme.obterGeneroPorNome(nomeGeneroSelecionado2);
		});
		
		String nomeClassificaoSelecionado = inputClassificacao.getValue();

		classificacaoSelecionado  = daoFilme.obterClassificaoPorNome(nomeClassificaoSelecionado);

		System.out.println(classificacaoSelecionado.getClassificacao());

		inputClassificacao.getSelectionModel().selectedItemProperty().addListener((obs, oldGenero, newGenero) -> {
			String nomeClassificaoSelecionado2 = inputClassificacao.getValue();
			classificacaoSelecionado = daoFilme.obterClassificaoPorNome(nomeClassificaoSelecionado2);
		});
	}

	public void salvar() {
		daoFilme.updateFilme(filme, inputNomeFilme.getText(), classificacaoSelecionado, generoSelecionado,
				inputSinopse.getText(), inputDiretor.getText(),
				(Date) convertString(inputDataLancamento.getText(), Date.class),
				(Time) convertString(inputDuracao.getText(), Time.class), null);

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

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FilmesEmCartaz.fxml"));
			Parent secondPage = loader.load();

			ControllerFilmesEmCartaz controllerFilmesEmCartaz = loader.getController();
			controllerFilmesEmCartaz.setRegiaoCentral(regiaoCentral);

			regiaoCentral.getChildren().clear();

			regiaoCentral.getChildren().add(secondPage);

		} catch (IOException ex) {
			ex.printStackTrace();
			NotificationManager.showNotification("Erro", "Falha ao carregar a tela de filmes em cartaz.",
					NotificationType.ERROR);
		}
	}
}
