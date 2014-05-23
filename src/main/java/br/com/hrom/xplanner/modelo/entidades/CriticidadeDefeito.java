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
	
	@Column(name = "nome", length = 40, unique = true, nullable = false)
	private String nome;
	
	public CriticidadeDefeito(){
		
	}
	
	public CriticidadeDefeito(long id, String criticidade) {
		super();
		this.id = id;
		this.nome = criticidade;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String criticidade) {
		this.nome = criticidade;
	}

	@Override
	public String toString() {
		return "CriticidadeDefeito [id=" + id + ", nome=" + nome
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nome == null) ? 0 : nome.hashCode());
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
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
