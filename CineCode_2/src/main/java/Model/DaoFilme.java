package Model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;
import javafx.util.Duration;

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

		return query.getResultList(); // Resultado do SELECT
	}

	public List<E> obterTodosEmCartaz() {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e WHERE e.statusFilme  = true";

		TypedQuery<E> query = em.createQuery(jpql, classe);

		return query.getResultList(); // Resultado do SELECT
	}
	
	public List<E> obterTodosEmBreve() {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e WHERE e.statusFilme  = false";

		TypedQuery<E> query = em.createQuery(jpql, classe);

		return query.getResultList(); // Resultado do SELECT
	}

	public List<String> obterGeneros() {
		String jpql = "SELECT DISTINCT e.genero FROM " + classe.getName() + " e";

		TypedQuery<String> query = em.createQuery(jpql, String.class);

		return query.getResultList();
	}

	public void removerFilme(Filme filme) {
		abrirT(); // Inicia a transação

		try {
			// Verifica se o filme está sendo gerenciado pelo EntityManager
			if (!em.contains(filme)) {
				filme = em.merge(filme); // Anexa o filme ao contexto de persistência
			}

			em.remove(filme); // Remove o filme
			fecharT(); // Comita a transação

			// Verifica se o filme foi removido com sucesso
			if (!em.contains(filme)) {
				showNotification("Sucesso", "Filme removido com sucesso.", NotificationType.SUCCESS);
			} else {
				showNotification("Erro", "A remoção do filme falhou.", NotificationType.ERROR);
			}
		} catch (Exception e) {
			em.getTransaction().rollback(); // Desfaz a transação em caso de erro
			showNotification("Erro", "A remoção do filme falhou devido a um erro inesperado.", NotificationType.ERROR);
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback(); // Garante que a transação seja desfeita em caso de erro
			}
		}
	}

	private void showNotification(String title, String message, NotificationType type) {
		Notifications notification = Notifications.create().title(title).text(message).hideAfter(Duration.seconds(5))
				.position(Pos.BOTTOM_RIGHT); // Define a posição da notificação

		switch (type) {
        case SUCCESS:
            notification.showInformation();  // Mostra a notificação de sucesso
            break;
        case ERROR:
            notification.showError();  // Mostra a notificação de erro
            break;
        case INFORMATION:
            notification.showInformation();  // Mostra a notificação informativa
            break;
        default:
            notification.show();  // Mostra a notificação padrão
            break;
    }
	}

	public enum NotificationType {
	    SUCCESS, ERROR, INFORMATION
	}

	public void updateFilme(Filme filme, String nome, String classificacao, String genero, String sinopse, String autor, Date anoLancamento, Time duracao, Integer horario) {
	    abrirT();
	    try {
	        boolean isModified = false;
	        boolean isNotVazio = true;

	        if (!nome.isEmpty() && !filme.getNome().equals(nome)) {
	            filme.setNome(nome);
	            isModified = true;
	        } else if (nome.isEmpty()) {
	            isNotVazio = false;
	            System.out.println(isNotVazio);
	        }

	        if (!classificacao.isEmpty() && !filme.getClassificacao().equals(classificacao)) {
	            filme.setClassificacao(classificacao);
	            isModified = true;
	        } else if (classificacao.isEmpty()) {
	            isNotVazio = false;
	            System.out.println(isNotVazio);
	        }

	        if (!genero.isEmpty() && !filme.getGenero().equals(genero)) {
	            filme.setGenero(genero);
	            isModified = true;
	        } else if (genero.isEmpty()) {
	            isNotVazio = false;
	            System.out.println(isNotVazio);
	        }

	        if (!sinopse.isEmpty() && !filme.getSinopse().equals(sinopse)) {
	            filme.setSinopse(sinopse);
	            isModified = true;
	        } else if (sinopse.isEmpty()) {
	            isNotVazio = false;
	            System.out.println(isNotVazio);
	        }

	        if (!autor.isEmpty() && !filme.getAutor().equals(autor)) {
	            filme.setAutor(autor);
	            isModified = true;
	        } else if (autor.isEmpty()) {
	            isNotVazio = false;
	            System.out.println(isNotVazio);
	        }
	        
	        System.out.println("a" + anoLancamento);
	        	        
	        if (anoLancamento != null && !filme.getAnoLancamento().equals(anoLancamento)) {
	            filme.setAnoLancamento(anoLancamento);
	            isModified = true;
	        } else if (anoLancamento == null){
	        	isNotVazio = false;
	        }
	        
	        if (duracao != null && !filme.getDuracao().equals(duracao)) {
	            filme.setDuracao(duracao);
	            isModified = true;
	        } else if (duracao == null){
	        	isNotVazio = false;
	        }
	        
	        if (horario != null && !filme.getHorario().equals(horario)) {
	            filme.setHorario(horario);
	            isModified = true;
	        } else if (horario == null){
	        	isNotVazio = false;
	        }

	        if (isModified && isNotVazio) {
	            fecharT();
	            showNotification("Sucesso", "Filme atualizado com sucesso.", NotificationType.SUCCESS);
	        } else if (!isNotVazio) {
	            em.getTransaction().rollback();
	            showNotification("Erro", "Campos vazios não são permitidos.", NotificationType.ERROR);
	        } else {
	            fecharT();
	            showNotification("Nenhuma alteração", "Nenhuma alteração foi realizada.", NotificationType.INFORMATION);
	        }
	    } catch (Exception e) {
	        em.getTransaction().rollback();
	        showNotification("Erro", "A atualização do filme falhou devido a um erro inesperado.", NotificationType.ERROR);
	    }
	}

}
