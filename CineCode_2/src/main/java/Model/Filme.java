package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.sql.Date; // Para o tipo DATE
import java.sql.Time; // Para o tipo TIME

@Entity
@Table(name = "tb_filmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_filme")
    private int id;

    @Column(name = "titulo")
    private String nome;

    @Column(name = "ano_lancamento")
    private Date anoLancamento; // Tipo DATE

    @Column(name = "genero")
    private String genero; // Tipo VARCHAR(45)

    @Column(name = "autor")
    private String autor; // Tipo VARCHAR(45)

    @Column(name = "duracao")
    private Time duracao; // Tipo TIME

    @Column(name = "status_filme")
    private Boolean statusFilme; // Tipo BIT(1). Usando Boolean para mapear BIT(1)

    @Column(name = "horario")
    private Integer horario; // Tipo INT
    
    @Column(name = "classificacao")
    private String classificacao;
    
    @Column(name = "sinopse")
    private String sinopse;
    
    public Filme() {
    }
    
    public Filme(String nome, Date anoLancamento, String genero, String autor, Time duracao, Boolean statusFilme, Integer horario) {
    	this.nome = nome;
    	this.anoLancamento = anoLancamento;
    	this.genero = genero;
    	this.autor = autor;
    	this.duracao = duracao;
    	this.statusFilme = statusFilme;
    	this.horario = horario;
    }

    public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Date anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Time getDuracao() {
        return duracao;
    }

    public void setDuracao(Time duracao) {
        this.duracao = duracao;
    }

    public Boolean getStatusFilme() {
        return statusFilme;
    }

    public void setStatusFilme(Boolean statusFilme) {
        this.statusFilme = statusFilme;
    }

    public Integer getHorario() {
        return horario;
    }

    public void setHorario(Integer horario) {
        this.horario = horario;
    }
}
