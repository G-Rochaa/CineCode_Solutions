package Control;

import java.sql.Time;

import Model.DaoSessao;
import Model.FormataHoras;
import Model.Sessao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ControllerFormsSessao {
	
	FormataHoras horaFormat = new FormataHoras();
	
	DaoSessao<Sessao> sessao = new DaoSessao<>(Sessao.class);

	@FXML
	private VBox regiaoCentralFormsSessao;

	@FXML
	private TextField inputHorarioInicio;

	@FXML
	private TextField inputHorarioFim;

	@FXML
	private DatePicker inputDataFilme;
	
	@FXML
	private Button salvarSessao;
	
	@FXML 
	private Button cancelarSessao;

	@FXML
	public void initialize() {
		horaFormat.formataHora(inputHorarioInicio);

		horaFormat.formataHora(inputHorarioFim);
	}
	
	public void enviaDados() {
		sessao.capturaHorarioInicio((Time) horaFormat.convertStringToTime(inputHorarioInicio.getText(), Time.class));
		
		sessao.capturaHorarioFim((Time) horaFormat.convertStringToTime(inputHorarioFim.getText(), Time.class));
		
		sessao.capturaDataSessao(horaFormat.convertLocalDateToSqlDate(inputDataFilme.getValue()));
		
		sessao.insertSessao();
	}
	
}
