package Model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Tb_Salas")
public class Salas {
	
	@Id
    @Column(name = "Nr_Sala")
    private int numeroSala;

    @Column(name = "Qnt_Assentos")
    private int quantidadeAssentos;
    
    @OneToMany(mappedBy = "sala")
    private List<Sessao> sessoes;

    public Salas() {}

    public Salas(int numeroSala, int quantidadeAssentos) {
        this.numeroSala = numeroSala;
        this.quantidadeAssentos = quantidadeAssentos;
    }

    public int getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(int numeroSala) {
        this.numeroSala = numeroSala;
    }

    public int getQuantidadeAssentos() {
        return quantidadeAssentos;
    }

    public void setQuantidadeAssentos(int quantidadeAssentos) {
        this.quantidadeAssentos = quantidadeAssentos;
    }

    public List<Sessao> getSessoes() {
        return sessoes;
    }

    public void setSessoes(List<Sessao> sessoes) {
        this.sessoes = sessoes;
    }
}
