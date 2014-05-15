package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe que representa a entidade (tabela) Criticidade do banco de dados. Ex: Baixa, Média, Alta
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
@Entity
@Table(name = "criticidade_defeito")
public class CriticidadeDefeito implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	
	@Column(name = "nome_criticidade", length = 30, unique = true, nullable = false)
	private String criticidade;
	
	@Column(columnDefinition = "default 1") //ativo
	private boolean ativo;
	
	public CriticidadeDefeito(){
		
	}
	
	public CriticidadeDefeito(long id, String criticidade, boolean ativo) {
		super();
		this.id = id;
		this.criticidade = criticidade;
		this.ativo = ativo;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCriticidade() {
		return criticidade;
	}
	public void setCriticidade(String criticidade) {
		this.criticidade = criticidade;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ativo ? 1231 : 1237);
		result = prime * result
				+ ((criticidade == null) ? 0 : criticidade.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CriticidadeDefeito other = (CriticidadeDefeito) obj;
		if (ativo != other.ativo)
			return false;
		if (criticidade == null) {
			if (other.criticidade != null)
				return false;
		} else if (!criticidade.equals(other.criticidade))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
