package br.com.itpacpalmas.api_sig_lab_itpac.model;

import java.sql.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import br.com.itpacpalmas.api_sig_lab_itpac.entities.Subgrupo;
import javax.persistence.GenerationType;
@Entity
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String motivo;
    @ManyToOne
    @JoinColumn(name = "manual")
    private Manual manual;
    @ManyToOne
    @JoinColumn(name = "professor")
    private Professor professor;
    @ManyToOne
    @JoinColumn(name = "sala")
    private Sala sala;
    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;
    @ManyToOne
    @JoinColumn(name = "subgrupo") 
    private Subgrupo subgrupo; 
    private Date data;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public Manual getManual() {
        return manual;
    }
    public void setManual(Manual manual) {
        this.manual = manual;
    }
    public Professor getProfessor() {
        return professor;
    }
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
    public Sala getSala() {
        return sala;
    }
    public void setSala(Sala sala) {
        this.sala = sala;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Subgrupo getSubgrupo() {
        return subgrupo;
    }
    public void setSubgrupo(Subgrupo subgrupo) {
        this.subgrupo = subgrupo;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    } 
    
}
