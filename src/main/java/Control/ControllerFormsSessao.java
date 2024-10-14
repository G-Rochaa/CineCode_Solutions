package Control;

import java.sql.Date;
import java.sql.Time;

import Model.DaoSessao;
import Model.Filme;
import Model.FormataHoras;
import Model.Salas;
import Model.Sessao;
import View.NotificationManager;
import View.NotificationManager.NotificationType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ControllerFormsSessao {
	
	FormataHoras horaFormat = new FormataHoras();
	
	DaoSessao<Sessao> sessaoDao = new DaoSessao<>(Sessao.class);

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
	
    private Salas salaEscolhida;
    private Filme filmeEscolhido;

	@FXML
	public void initialize() {
		
		inputHorarioInicio.setPromptText("HH:MM:SS");

		horaFormat.formataHora(inputHorarioInicio);

		inputHorarioFim.setPromptText("HH:MM:SS");
		
		horaFormat.formataHora(inputHorarioFim);
	}
		
	public Boolean enviaDados() {
        
        Time horarioInicio = (Time) horaFormat.convertStringToTime(inputHorarioInicio.getText(), Time.class);
        Time horarioFim = (Time) horaFormat.convertStringToTime(inputHorarioFim.getText(), Time.class);
        Date dataSessao = horaFormat.convertLocalDateToSqlDate(inputDataFilme.getValue());
        
        if (horarioInicio == null || horarioFim == null || dataSessao == null) {
            NotificationManager.showNotification("Erro!", "Campos Vazios não são permitidos!", NotificationType.ERROR);
			return false;
		}

        Sessao sessao = new Sessao();
        
        sessao.setSala(salaEscolhida);
        sessao.setFilme(filmeEscolhido);
        sessao.setHorarioIni(horarioInicio);
        sessao.setHorarioFim(horarioFim);
        sessao.setData(dataSessao);

        sessaoDao.capturaSala(salaEscolhida);
        sessaoDao.capturaFilme(filmeEscolhido);
        sessaoDao.capturaHorarioInicio(horarioInicio);
        sessaoDao.capturaHorarioFim(horarioFim);
        sessaoDao.capturaDataSessao(dataSessao);
        
        Boolean result = sessaoDao.insertSessao(sessao);
        
        if (result) {
        	limpaCampos();
		}
        return true;
    }

    public void setDaoSessao(DaoSessao<Sessao> daoSessao) {
        this.sessaoDao = daoSessao;
    }

    public void setSalaEscolhida(Salas salaEscolhida) {
        this.salaEscolhida = salaEscolhida;
    }

    public void setFilmeEscolhido(Filme filmeEscolhido) {
        this.filmeEscolhido = filmeEscolhido;
    }
    
    public void limpaCampos() {
    	this.inputHorarioInicio.clear();
    	this.inputHorarioFim.clear();
    }
}
