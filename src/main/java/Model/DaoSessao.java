package Model;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import View.NotificationManager;
import View.NotificationManager.NotificationType;

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

	public List<java.util.Date> obterHorarioInicio(Salas sala) {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT h.horarioIni FROM " + classe.getName()
				+ " h WHERE h.sala.numeroSala = :numeroSala order by h.horarioIni";

		TypedQuery<java.util.Date> query = em.createQuery(jpql, java.util.Date.class);

		query.setParameter("numeroSala", sala.getNumeroSala());

		return query.getResultList();

	}

	public List<java.util.Date> obterHorarioFim(Salas sala) {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT h.horarioFim FROM " + classe.getName()
				+ " h WHERE h.sala.numeroSala = :numeroSala order by h.horarioFim";

		TypedQuery<java.util.Date> query = em.createQuery(jpql, java.util.Date.class);

		query.setParameter("numeroSala", sala.getNumeroSala());

		return query.getResultList();

	}

	public List<java.util.Date> obterHorarioIniPorDia(Salas sala, Date dataSessao) {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT h.horarioIni FROM " + classe.getName()
				+ " h WHERE h.sala.numeroSala = :numeroSala and h.data = :dataSessao order by h.horarioFim";

		TypedQuery<java.util.Date> query = em.createQuery(jpql, java.util.Date.class);

		query.setParameter("numeroSala", sala.getNumeroSala());
		query.setParameter("dataSessao", dataSessao);

		return query.getResultList();

	}

	public List<java.util.Date> obterHorarioFimPorDia(Salas sala, Date dataSessao) {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe Nula");
		}

		String jpql = "SELECT h.horarioFim FROM " + classe.getName()
				+ " h WHERE h.sala.numeroSala = :numeroSala and h.data = :dataSessao order by h.horarioFim";

		TypedQuery<java.util.Date> query = em.createQuery(jpql, java.util.Date.class);

		query.setParameter("numeroSala", sala.getNumeroSala());
		query.setParameter("dataSessao", dataSessao);

	
		return query.getResultList();

	}

	public Salas capturaSala(Salas sala) {
		this.sessao.setSala(sala);
		return this.salaEscolhida = sala;
	}

	public Filme capturaFilme(Filme filme) {
		this.sessao.setFilme(filme);
		return this.filmeEscolhido = filme;
	}

	public Time capturaHorarioInicio(Time horarioIni) {
		this.sessao.setHorarioIni(horarioIni);
		return this.horarioInicio = horarioIni;
	}

	public Time capturaHorarioFim(Time HorarioFim) {
		this.sessao.setHorarioFim(HorarioFim);
		return this.horarioFim = HorarioFim;
	}

	public Date capturaDataSessao(Date dataMarcada) {
		this.sessao.setData(dataMarcada);
		return this.dataSessao = dataMarcada;
	}
	
	public Boolean insertSessao(Sessao sessao) {
	    if (validaHorario()) {
	        try {
	            abrirT();
	            em.persist(sessao);
	            fecharT();
	            NotificationManager.showNotification("Sucesso!", "Sessão inserida com sucesso!",
	                    NotificationType.SUCCESS);
	            return true;
	        } catch (Exception e) {
	            NotificationManager.showNotification("Erro!", "Ocorreu um erro inesperado!", NotificationType.ERROR);
	            return false;
	        }
	    }

	    return false;
	}

	public Boolean validaHorario() {

		LocalTime inicio = horarioInicio.toLocalTime();
		LocalTime fim = horarioFim.toLocalTime();

		if (fim.isBefore(inicio)) {
			NotificationManager.showNotification("Erro!", "Horário de inicio não compatível não compatível",
					NotificationType.ERROR);
			return false;
		}

		Duration duracao = Duration.between(inicio, fim);

		LocalTime duracaoFilme = filmeEscolhido.getDuracao().toLocalTime();

		Duration duracaoFilmeDuration = Duration.ofHours(duracaoFilme.getHour()).plusMinutes(duracaoFilme.getMinute())
				.plusSeconds(duracaoFilme.getSecond());

		if (duracao.compareTo(duracaoFilmeDuration) < 0) {
			NotificationManager.showNotification(
					"Erro!", "A duração da sessão é menor que a duração do filme. "
							+ "(O filme escolhido tem a duração de " + filmeEscolhido.getDuracao() + "hrs)",
					NotificationType.ERROR);
			return false;
		}

		List<java.util.Date> horarioIni = obterHorarioIniPorDia(salaEscolhida, dataSessao);
		List<java.util.Date> horarioFinal = obterHorarioFimPorDia(salaEscolhida, dataSessao);
		
		for (int i = 0; i < horarioIni.size(); i++) {
			System.out.println(horarioIni.get(i));
		}

		for (java.util.Date horarioIniOutrasSessoes : horarioIni) {
			System.out.println(horarioInicio + "==" + horarioIniOutrasSessoes);
			
			if (horarioInicio.equals(horarioIniOutrasSessoes)) {
				NotificationManager.showNotification("Erro!",
						"Horário de inicio da sessão inválido, já consta outra sessão cadastrada nesse horário",
						NotificationType.ERROR);
				return false;
			}
		}

		Map<java.util.Date, java.util.Date> horariosFilmes = new HashMap<>();

		for (int i = 0; i < horarioIni.size(); i++) {
			horariosFilmes.put(horarioIni.get(i), horarioFinal.get(i));
		}

		// Para cada horário de início, iteramos sobre o Map de horários de filmes
		for (Entry<java.util.Date, java.util.Date> entry : horariosFilmes.entrySet()) {
			
			java.util.Date horarioInicioMap = entry.getKey();
			java.util.Date horarioFimMap = entry.getValue();
			
			// Verifica se o horário está entre o horário de início e fim do Map
			if (this.horarioInicio.after(horarioInicioMap) && this.horarioInicio.before(horarioFimMap)) {
				NotificationManager.showNotification("Erro!",
						"Horário da sessão inválido, já consta outra sessão cadastrada nesse horário",
						NotificationType.ERROR);
				return false;
			}

			long intervaloMinimo = 30 * 60 * 1000; // 30 minutos em milissegundos
			long fusoHorario = 10800000;
			long horarioInicioCorrigido = (horarioInicioMap.getTime() - fusoHorario) - intervaloMinimo;
			long horarioFimCorrigido = (horarioFimMap.getTime() - fusoHorario) + intervaloMinimo;

			
			System.out.println((horarioFim.getTime() - fusoHorario) > horarioInicioCorrigido && (horarioInicio.getTime() - fusoHorario) < horarioFimCorrigido);

			if ((horarioFim.getTime() - fusoHorario) > horarioInicioCorrigido && (horarioInicio.getTime() - fusoHorario) < horarioFimCorrigido) {
				NotificationManager.showNotification("Erro!",
						"Horário inválido: O início do próximo filme deve ser igual ou após o fim da sessão anterior + 30 minutos.",
						NotificationType.ERROR);
				return false;
			}

		}

		return true;
	}
}
