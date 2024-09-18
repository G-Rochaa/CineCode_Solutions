package Control;

import java.io.IOException;
import java.util.List;

import Model.DaoFilme;
import Model.DaoSessao;
import Model.Filme;
import Model.Salas;
import Model.Sessao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControllerFilmesXSala { // Classe criar para mostrar os filmes em cartaz para vincular com uma sala

	@FXML
	private VBox regiaoCentralFilmeXSala;

	DaoFilme<Filme> filmes = new DaoFilme<>(Filme.class);
	
    DaoSessao<Sessao> daoSessao;
    Salas salaEscolhida;

	List<Filme> listFilme = filmes.obterTodosEmCartaz();

	@FXML
	public void initialize() {
	    if (regiaoCentralFilmeXSala == null) {
	        return;
	    }
	    
	    for (Filme filme : listFilme) {
	        HBox HboxChild = new HBox();
	        HboxChild.getStyleClass().add("HboxChildSessao");

	        HBox HboxButton = new HBox();
	        HboxButton.getStyleClass().add("HboxButtonsSessao");

	        Button buttonFilme = new Button(filme.getNome());
	        buttonFilme.getStyleClass().add("buttonFilme");
	        
	        buttonFilme.setOnAction(e -> {
	            daoSessao.capturaFilme(filme);
	            
	            try {
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FormsSessao.fxml"));
	                Parent subPage = loader.load();
	                
	                String css = this.getClass().getResource("/View/FormsSessao.css").toExternalForm();
	                subPage.getStylesheets().add(css);

	                // Passar o daoSessao para o próximo controlador
	             // Passar o daoSessao e a salaEscolhida para o próximo controlador
                    ControllerFormsSessao controller = loader.getController();
                    controller.setDaoSessao(daoSessao);
                    controller.setSalaEscolhida(salaEscolhida);
                    controller.setFilmeEscolhido(filme);

	                regiaoCentralFilmeXSala.getChildren().clear();
	                regiaoCentralFilmeXSala.getChildren().add(subPage);
	                                    
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	        });
	        
	        HboxButton.getChildren().addAll(buttonFilme);
	        
	        HboxChild.getChildren().addAll(HboxButton);
	        
	        regiaoCentralFilmeXSala.getChildren().add(HboxChild);

	    }
	}
	
    public void setDaoSessao(DaoSessao<Sessao> daoSessao) {
        this.daoSessao = daoSessao;
    }

    public void setSalaEscolhida(Salas salaEscolhida) {
        this.salaEscolhida = salaEscolhida;
    }
}
