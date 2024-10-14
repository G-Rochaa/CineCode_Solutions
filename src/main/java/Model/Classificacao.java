package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Tb_Classificacao")
public class Classificacao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTb_Classificacao")
    private Long id_Classificao;

    @Column(name = "classificacao")
    private String classificacao;

    public Long getId() {
        return id_Classificao;
    }

    public void setId(Long id) {
        this.id_Classificao = id;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
    
    @Override
    public boolean equals(Object obj) { // Método utilizado para realizar a comperação se a classificação do filme foi alterada
        if (this == obj) {
            return true; 
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; 
        }
        Classificacao classificacaoObj = (Classificacao) obj;
        return id_Classificao != null && id_Classificao.equals(classificacaoObj.id_Classificao) && 
               (classificacao != null ? classificacao.equals(classificacaoObj.classificacao) : classificacaoObj.classificacao == null);
    }

}
