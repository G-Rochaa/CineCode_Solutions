package Model;

import java.sql.Date; // Para o tipo DATE
import java.sql.Time; // Para o tipo TIME

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_filmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_filme")
    private int id;

    @Column(name = "titulo")
    private String nome;

    @Column(name = "data_lancamento")
    private Date anoLancamento; // Tipo DATE

    @ManyToOne
    @JoinColumn(name = "Id_genero")
    private Genero genero; 

    @Column(name = "diretor")
    private String autor; // Tipo VARCHAR(45)

    @Column(name = "duracao")
    private Time duracao; // Tipo TIME

    @Column(name = "status_filme")
    private int statusFilme; 
    
    @ManyToOne
    @JoinColumn(name = "idTb_Classificacao")
    private Classificacao classificacao;
    
    @Column(name = "sinopse")
    private String sinopse;
    
    @Column(name = "imagem")
    private String img;

    
    public Filme() {
    }
    
    public Filme(String nome, Date anoLancamento, Genero genero, String autor, Time duracao, int statusFilme,
			Classificacao classificacao, String sinopse, String img) {
		this.nome = nome;
		this.anoLancamento = anoLancamento;
		this.genero = genero;
		this.autor = autor;
		this.duracao = duracao;
		this.statusFilme = statusFilme;
		this.classificacao = classificacao;
		this.sinopse = sinopse;
		this.img = img;
	}



	public Classificacao getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Classificacao classificacao) {
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

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
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

    public int getStatusFilme() {
        return statusFilme;
    }

    public void setStatusFilme(int statusFilme) {
        this.statusFilme = statusFilme;
    }
}