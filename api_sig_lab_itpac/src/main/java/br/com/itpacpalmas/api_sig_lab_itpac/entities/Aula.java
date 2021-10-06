package br.com.itpacpalmas.api_sig_lab_itpac.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import java.util.List;

@Entity(name="aula")
public class Aula{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, name = "codigo")
    private String codigo;
    @Column(nullable = false, name = "nome_atividade")
    private String nomeAtividade;
    @Column(nullable = false, name = "horas_aprendizagem")
    private Double horasAprendizagem;
    @ManyToOne
    @JoinColumn(name = "Agendamento")
    private Agendamento agendamento;
    // @OneToMany
    // @JoinTable
    // private List<Aluno> alunosPresentes;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNomeAtividade() {
        return nomeAtividade;
    }
    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }
    public Double getHorasAprendizagem() {
        return horasAprendizagem;
    }
    public void setHorasAprendizagem(Double horasAprendizagem) {
        this.horasAprendizagem = horasAprendizagem;
    }
    public Agendamento getAgendamento() {
        return agendamento;
    }
    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
    // public List<Aluno> getAlunosPresentes() {
    //     return alunosPresentes;
    // }
    // public void setAlunosPresentes(List<Aluno> alunosPresentes) {
    //     this.alunosPresentes = alunosPresentes;
    // }

    
    
    
}
