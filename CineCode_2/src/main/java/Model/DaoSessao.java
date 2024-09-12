package Model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DaoSessao<E> {
		private static EntityManagerFactory emf;
		// Por se tratar de um att static o mesmo só pode ser inicializado ou
		// diretamente no att ou em um bloco static
		private EntityManager em;
		private Class<E> classe;
		
		private Salas salaEscolhida;
		private Filme filmeEscolhido;
		private Time horarioInicio;
		private Time horarioFim;
		private Date dataSessao;
		
		private Sessao sessao = new Sessao();
		
		static { // Inicializa somente uma vez qnd o arquivo é lido
			try {
				emf = Persistence.createEntityManagerFactory("cinecode");
			} catch (Exception e) {
			}
		}

		public DaoSessao() { // Para gerar um DAO generico sem uma classe especifica
			this(null);
		}

		public DaoSessao(Class<E> classe) {
			this.classe = classe;
			em = emf.createEntityManager();
		}

		// Método que retornam a própia classe gera a possibilidade de um encadeamento
		// de métodos

		public DaoSessao<E> abrirT() {
			em.getTransaction().begin();
			return this;
		}

		public DaoSessao<E> fecharT() {
			em.getTransaction().commit();
			return this;
		}
		
		public List<E> obterSessoes() {
			if (classe == null) {
				throw new UnsupportedOperationException("Classe Nula");
			}

			String jpql = "SELECT e FROM " + classe.getName() + " e";
			TypedQuery<E> query = em.createQuery(jpql, classe);
			
			return query.getResultList();
		}
		
		public Salas capturaSala(Salas sala) {
			System.out.println(sala.getNumeroSala());
			this.sessao.setSala(sala);
			return this.salaEscolhida = sala;
		}
		
		public Filme capturaFilme(Filme filme) {
			System.out.println(filme.getNome());
			this.sessao.setFilme(filme);
			return this.filmeEscolhido = filme;
		}
		
		public Time capturaHorarioInicio(Time horarioIni) {
			System.out.println(horarioIni);
			this.sessao.setHorarioIni(horarioIni);
			return this.horarioInicio = horarioIni;
		}
		
		public Time capturaHorarioFim(Time HorarioFim) {
			System.out.println(HorarioFim);
			this.sessao.setHorarioFim(HorarioFim);
			return this.horarioFim = HorarioFim;
		}
		
		public Date capturaDataSessao(Date dataMarcada) {
			System.out.println(dataMarcada);
			this.sessao.setData(dataMarcada);
			return this.dataSessao = dataMarcada;
		}
		
		public void insertSessao() {
			
			System.out.println(sessao.getFilme().getNome());
			System.out.println(sessao.getHorarioIni());
			System.out.println(sessao.getHorarioFim());
			System.out.println(sessao.getData());
			System.out.println(sessao.getSala().getNumeroSala());
		}
}
