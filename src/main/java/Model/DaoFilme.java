package Model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import View.NotificationManager;
import View.NotificationManager.NotificationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

public class DaoFilme<E> {
	private static EntityManagerFactory emf;
	// Por se tratar de um att static o mesmo só pode ser inicializado ou
	// diretamente no att ou em um bloco static
	private EntityManager em;
	private Class<E> classe;

	static { // Inicializa somente uma vez qnd o arquivo é lido
		try {
			emf = Persistence.createEntityManagerFactory("cinecode");
		} catch (Exception e) {
		}
	}

	public DaoFilme() { // Para gerar um DAO generico sem uma classe especifica
		this(null);
	}

	public DaoFilme(Class<E> classe) {
		this.classe = classe;
		em = emf.createEntityManager();
	}

	// Método que retornam a própia classe gera a possibilidade de um encadeamento
	// de métodos

	public DaoFilme<E> abrirT() {
		em.getTransaction().begin();
		return this;
	}

	public DaoFilme<E> fecharT() {
		em.getTransaction().commit();
		return this;
	}

	public DaoFilme<E> incluir(E entidade) {
		em.persist(entidade);
		return this;
	}

	public DaoFilme<E> remover(E entidade) {
		em.remove(entidade);
		return this;
	}

	public List<E> obterTodos() {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e";
		TypedQuery<E> query = em.createQuery(jpql, classe);

		return query.getResultList(); 
	}

	public List<E> obterTodosEmCartaz() {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e WHERE e.statusFilme  = 1";

		TypedQuery<E> query = em.createQuery(jpql, classe);

		return query.getResultList(); 
	}

	public List<E> obterTodosEmBreve() {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e WHERE e.statusFilme  = 0";

		TypedQuery<E> query = em.createQuery(jpql, classe);

		return query.getResultList();
	}
	
	public void inputClassificaoOpcoes(ChoiceBox<String> choiceBox) {
		List<String> listaClassificacao = obterClassificao();
		ObservableList<String> observableClassificacao = FXCollections.observableArrayList(listaClassificacao);
		choiceBox.setItems(observableClassificacao);
	}
	
	public void inputGeneroOpcoes(ChoiceBox<String> choiceBox) {
		List<String> listaGeneros = obterNomesGeneros();
		ObservableList<String> observableGeneros = FXCollections.observableArrayList(listaGeneros);
		choiceBox.setItems(observableGeneros);
	}

	public Boolean cadastrarFilme(String nome, Date anoLancamento, Genero genero, String autor, Time duracao,
			int statusFilme, Classificacao classificacao, String sinopse, String img) {
				
		if (verificaPossiveisErrosInsertFilme(nome, anoLancamento, genero, autor, duracao, statusFilme, classificacao, sinopse,	img)) {
			abrirT();
			Filme filme = new Filme(nome, anoLancamento, genero, autor, duracao, statusFilme, classificacao, sinopse,img);
			
			em.persist(filme);
			fecharT();
			
		    NotificationManager.showNotification("Sucesso!", "Filme inserido com sucesso", NotificationType.SUCCESS);
			
			return true;
		}
		
		return false;
	}

	public void removerFilme(Filme filme) {
		abrirT(); 
		
		try {
			filme.setStatusFilme(2);
			NotificationManager.showNotification("Sucesso", "Filme desativado com sucesso!", NotificationType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		fecharT();
	}
	
	public List<String> obterClassificao() {
		String jpql = "SELECT e.classificacao FROM Classificacao e";

		TypedQuery<String> query = em.createQuery(jpql, String.class);

		return query.getResultList();
	}

	public Classificacao obterClassificaoPorNome(String nomeClassificao) {	
		if (nomeClassificao == null) {
			return null;
		} // Qnd o usuário clica no botão cancelar e já cadastrou um filme evita erros
		
		String jpql = "SELECT c FROM Classificacao c WHERE c.classificacao = :nomeClassificao";
		TypedQuery<Classificacao> query = em.createQuery(jpql, Classificacao.class);
		query.setParameter("nomeClassificao", nomeClassificao);
		return query.getSingleResult(); 
	}
	
	public List<String> obterNomesGeneros() {
		String jpql = "SELECT DISTINCT e.nome_genero FROM Genero e";

		TypedQuery<String> query = em.createQuery(jpql, String.class);

		return query.getResultList();
	}

	public Genero obterGeneroPorNome(String nomeGenero) {	
		if (nomeGenero == null) {
			return null;
		} // Qnd o usuário clica no botão cancelar e já cadastrou um filme evita erros
		
		String jpql = "SELECT g FROM Genero g WHERE g.nome_genero = :nomeGenero";
		TypedQuery<Genero> query = em.createQuery(jpql, Genero.class);
		query.setParameter("nomeGenero", nomeGenero);
		return query.getSingleResult(); 
	}
	
	public void updateFilme(Filme filme, String nome, Classificacao classificacao, Genero genero, String sinopse, String autor,
			Date anoLancamento, Time duracao, CheckBox statusFilme) {
		
		abrirT();
		try {
			boolean isModified = false;
			boolean isNotVazio = true;

			if (!nome.isEmpty() && !filme.getNome().equals(nome)) {
				filme.setNome(nome);
				isModified = true;
				System.out.println("Nome foi modificado");
			} else if (nome.isEmpty()) {
				isNotVazio = false;
			}

			if (!filme.getClassificacao().equals(classificacao)) {
				filme.setClassificacao(classificacao);
				isModified = true;
				System.out.println("Classificacao foi modificado");
			} 

			if (!filme.getGenero().equals(genero)) {
				filme.setGenero(genero);
				isModified = true;
				System.out.println("genero foi modificado");

			}

			if (!sinopse.isEmpty() && !filme.getSinopse().equals(sinopse)) {
				filme.setSinopse(sinopse);
				isModified = true;
				System.out.println("sinopse foi modificado");

			} else if (sinopse.isEmpty()) {
				isNotVazio = false;
			}

			if (!autor.isEmpty() && !filme.getAutor().equals(autor)) {
				filme.setAutor(autor);
				isModified = true;
				System.out.println("autor foi modificado");
			} else if (autor.isEmpty()) {
				isNotVazio = false;
			}

			if (anoLancamento != null && !filme.getAnoLancamento().equals(anoLancamento)) {
				filme.setAnoLancamento(anoLancamento);
				isModified = true;
				System.out.println("anoLancamento foi modificado");
			} else if (anoLancamento == null) {
				isNotVazio = false;
			}

			if (duracao != null && !filme.getDuracao().equals(duracao)) {
				filme.setDuracao(duracao);
				isModified = true;
				System.out.println("duracao foi modificado");
			} else if (duracao == null) {
				isNotVazio = false;
			}
			
			if (statusFilme == null) {
				System.out.println("statusFilme == null");
			} else if (statusFilme.isSelected()) {
				filme.setStatusFilme(1);
				isModified = true;
				System.out.println("statusFilme foi marcado");
			}

			if (isModified && isNotVazio) {
				em.merge(filme); // Salva as alterações
				fecharT();
				NotificationManager.showNotification("Sucesso", "Filme atualizado com sucesso.",
						NotificationType.SUCCESS);
			} else if (!isNotVazio) {
				em.getTransaction().rollback();
				NotificationManager.showNotification("Erro", "Campos vazios não são permitidos.",
						NotificationType.ERROR);
			} else {
				fecharT();
				NotificationManager.showNotification("Nenhuma alteração", "Nenhuma alteração foi realizada.",
						NotificationType.INFORMATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			NotificationManager.showNotification("Erro", "A atualização do filme falhou devido a um erro inesperado.",
					NotificationType.ERROR);
		}
	}

	public Boolean verificaPossiveisErrosInsertFilme(String nome, Date anoLancamento, Genero genero, String autor, Time duracao,
	        int statusFilme, Classificacao classificacao, String sinopse, String img) {

	    if (nome.isEmpty()) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo NOME está vazio", NotificationType.ERROR);
	        return false;
	    }
	    
	    if (anoLancamento == null) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo ANO DE LANÇAMENTO está vazio", NotificationType.ERROR);
	        return false;
	    }
	    
	    if (genero == null) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo GÊNERO está vazio", NotificationType.ERROR);
	        return false;
	    }

	    if (autor.isEmpty()) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo AUTOR está vazio", NotificationType.ERROR);
	        return false;
	    }

	    if (duracao == null) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo DURAÇÃO está vazio", NotificationType.ERROR);
	        return false;
	    }

	    if (classificacao == null) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo CLASSIFICAÇÃO está vazio", NotificationType.ERROR);
	        return false;
	    }

	    if (sinopse.isEmpty()) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo SINOPSE está vazio", NotificationType.ERROR);
	        return false;
	    }
	    
	    if(statusFilme == -1) {
	    	em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo STATUS FILME não foi selecionado", NotificationType.ERROR);
	        return false;
	    }

	    if (img.isEmpty()) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo IMAGEM está vazio", NotificationType.ERROR);
	        return false;
	    }
	    
	    return true;
	}

}