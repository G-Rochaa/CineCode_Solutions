
package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_Genero")
public class Genero {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero")
	private int id;
	
    @Column(name = "nome_genero")
	private String nome_genero;

	public String getNome_genero() {
		return nome_genero;
	}

	public void setNome_genero(String nome_genero) {
		this.nome_genero = nome_genero;
	}
    
	@Override
	public String toString() {
	    return nome_genero;
	}

	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true; // Se for o mesmo objeto, retorna true
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false; // Se o objeto for nulo ou de outra classe, retorna false
	    }
	    Genero genero = (Genero) obj;
	    return id == genero.id && (nome_genero != null ? nome_genero.equals(genero.nome_genero) : genero.nome_genero == null);
	}
    
}
