package Model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DaoSala<E>  {
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

	public DaoSala() { // Para gerar um DAO generico sem uma classe especifica
		this(null);
	}

	public DaoSala(Class<E> classe) {
		this.classe = classe;
		em = emf.createEntityManager();
	}

	// Método que retornam a própia classe gera a possibilidade de um encadeamento
	// de métodos

	public DaoSala<E> abrirT() {
		em.getTransaction().begin();
		return this;
	}

	public DaoSala<E> fecharT() {
		em.getTransaction().commit();
		return this;
	}
	
	
	public List<E> obterSalas() {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e";
		TypedQuery<E> query = em.createQuery(jpql, classe);
		
		return query.getResultList();
	}
}
