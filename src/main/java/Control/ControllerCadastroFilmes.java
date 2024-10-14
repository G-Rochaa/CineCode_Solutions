package Control;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import Model.Classificacao;
import Model.DaoFilme;
import Model.Filme;
import Model.FormataHoras;
import Model.Genero;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ControllerCadastroFilmes {
	@FXML
    private TextField inputNomeFilme;

    @FXML
    private ChoiceBox<String> inputClassificacao;
    
	private Classificacao classificacaoSelecionado = null;

    @FXML
    private ChoiceBox<String> inputGenero;
    
	private Genero generoSelecionado = null;

    @FXML
    private TextArea inputSinopse;

    @FXML
    private TextField inputDiretor;

    @FXML
    private TextField inputDuracao;

    @FXML
    private DatePicker inputDataLancamento;
    
	private LocalDate dataLancamento;

    @FXML
    private RadioButton radio1;

    @FXML
    private RadioButton radio2;

    @FXML
    private TextField inputImg;

    @FXML
    private Button salvar;

    @FXML
    private Button cancelar;
    
	DaoFilme<Filme> daoFilme = new DaoFilme<>(Filme.class);
	
	FormataHoras horaFormt = new FormataHoras();
    
    @FXML
    private void initialize() {
    	    	
    	daoFilme.inputGeneroOpcoes(inputGenero);
    	daoFilme.inputClassificaoOpcoes(inputClassificacao);
    			
		inputGenero.getSelectionModel().selectedItemProperty().addListener((obs, oldGenero, newGenero) -> {
			String nomeGeneroSelecionado2 = inputGenero.getValue();
			generoSelecionado = daoFilme.obterGeneroPorNome(nomeGeneroSelecionado2);
		});
		
		inputClassificacao.getSelectionModel().selectedItemProperty().addListener((obs, oldGenero, newGenero) -> {
			String nomeClassificaoSelecionado = inputClassificacao.getValue();
			classificacaoSelecionado = daoFilme.obterClassificaoPorNome(nomeClassificaoSelecionado);
		});
		
		inputDuracao.setPromptText("HH:MM:SS");
    	horaFormt.formataHora(inputDuracao);
    	
    	inputDataLancamento.setOnAction(e -> {
			dataLancamento = inputDataLancamento.getValue();
		});
    	
		ToggleGroup group = new ToggleGroup();
		radio1.setToggleGroup(group);
		radio2.setToggleGroup(group);
		
		salvar.setOnAction(e -> {
			if (daoFilme.cadastrarFilme(inputNomeFilme.getText(), convertLocalDateToSqlDate(dataLancamento),
					generoSelecionado, inputDiretor.getText(), (Time) convertString(inputDuracao.getText(), Time.class),
					getStatusFilmeSelecionado(group, radio1, radio2), classificacaoSelecionado,
					inputSinopse.getText(), inputImg.getText())) {
				limpaCampos();
			}
		});
		
		cancelar.setOnAction(e -> {
			limpaCampos();
		});
	}
    
    public void limpaCampos() {

        inputNomeFilme.clear();
        inputDiretor.clear();
        inputDuracao.clear();
        inputImg.clear();

        inputSinopse.clear();

        inputDataLancamento.setValue(null);

        inputGenero.setValue(null);
        
        inputClassificacao.setValue(null);

        ToggleGroup group = (ToggleGroup) radio1.getToggleGroup();
        if (group != null) {
            group.selectToggle(null);
        }
        dataLancamento = null;
    }
    		
	public static Date convertLocalDateToSqlDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return Date.valueOf(localDate);
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
	
	private int getStatusFilmeSelecionado(ToggleGroup group, RadioButton radio1, RadioButton radio2) {
		if (group.getSelectedToggle() == radio1) {
			return 0;
		} else if (group.getSelectedToggle() == radio2) {
			return 1;
		}
		return -1; // Valor padrão caso nada esteja selecionado
	}
}
