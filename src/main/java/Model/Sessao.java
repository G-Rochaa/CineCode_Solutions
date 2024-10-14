package Model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_sessao")
public class Sessao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Sessao")
	private int id;
	
    @Column(name = "Horario_Inicio")
	private Time horarioIni;
    
    @Column(name = "Horario_Fim")
	private Time horarioFim;
    
    @Column(name = "Data")
    private Date data;
    
    @ManyToOne
    @JoinColumn(name = "Nr_Sala")
    private Salas sala;
    
    @ManyToOne
    @JoinColumn(name = "id_filme")
    private Filme filme;

    public Sessao() {
    	
    }

    public Sessao(Time horarioIni, Time horarioFim, Date data, Salas sala, Filme filme) {
        this.horarioIni = horarioIni;
        this.horarioFim = horarioFim;
        this.data = data;
        this.sala = sala;
        this.filme = filme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getHorarioIni() {
        return horarioIni;
    }

    public void setHorarioIni(Time horarioIni) {
        this.horarioIni = horarioIni;
    }

    public Time getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(Time horarioFim) {
        this.horarioFim = horarioFim;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Salas getSala() {
        return sala;
    }

    public void setSala(Salas sala) {
        this.sala = sala;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return super.toString();
    }
}
