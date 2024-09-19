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

		String jpql = "SELECT e FROM " + classe.getName() + " e WHERE e.statusFilme  = 1";

		TypedQuery<E> query = em.createQuery(jpql, classe);

		return query.getResultList(); // Resultado do SELECT
	}

	public List<E> obterTodosEmBreve() {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e WHERE e.statusFilme  = 0";

		TypedQuery<E> query = em.createQuery(jpql, classe);

		return query.getResultList(); // Resultado do SELECT
	}

	public Boolean cadastrarFilme(String nome, Date anoLancamento, Genero genero, String autor, Time duracao,
			int statusFilme, String classificacao, String sinopse, String img) {

		if (verificaPossiveisErrosInsertFilme(nome, anoLancamento, genero, autor, duracao, statusFilme, classificacao, sinopse,				img)) {
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
		abrirT(); // Inicia a transação
		
		try {
			filme.setStatusFilme(2);
			NotificationManager.showNotification("Sucesso", "Filme desativado com sucesso!", NotificationType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		fecharT();
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
		return query.getSingleResult(); // Vai retornar o objeto Genero correspondente
	}

	public void updateFilme(Filme filme, String nome, String classificacao, Genero genero, String sinopse, String autor,
			Date anoLancamento, Time duracao) {
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

			if (!filme.getGenero().equals(genero)) {
				filme.setGenero(genero);
				isModified = true;
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

			if (anoLancamento != null && !filme.getAnoLancamento().equals(anoLancamento)) {
				filme.setAnoLancamento(anoLancamento);
				isModified = true;
			} else if (anoLancamento == null) {
				isNotVazio = false;
			}

			if (duracao != null && !filme.getDuracao().equals(duracao)) {
				filme.setDuracao(duracao);
				isModified = true;
			} else if (duracao == null) {
				isNotVazio = false;
			}

			if (isModified && isNotVazio) {
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
			em.getTransaction().rollback();
			NotificationManager.showNotification("Erro", "A atualização do filme falhou devido a um erro inesperado.",
					NotificationType.ERROR);
		}
	}

	public Boolean verificaPossiveisErrosInsertFilme(String nome, Date anoLancamento, Genero genero, String autor, Time duracao,
	        int statusFilme, String classificacao, String sinopse, String img) {

	    if (nome.isEmpty()) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo NOME está vazio", NotificationType.ERROR);
	        return false;
	    }
	    
	    // Verifica se anoLancamento é nulo
	    if (anoLancamento == null) {
	        em.getTransaction().rollback();
	        NotificationManager.showNotification("Erro", "O Campo ANO DE LANÇAMENTO está vazio", NotificationType.ERROR);
	        return false;
	    }
	    
	    // Adicione mais validações conforme necessário
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

	    if (classificacao.isEmpty()) {
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