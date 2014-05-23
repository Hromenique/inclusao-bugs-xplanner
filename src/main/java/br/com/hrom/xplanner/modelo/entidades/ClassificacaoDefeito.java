package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe que representa a entidade (tabela) classificacao do banco de dados. Ex: Impeditivo, Funcional, Interface...
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
@Entity
@Table(name="classificacao_defeito")
public class ClassificacaoDefeito implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	
	@Column(name="nome_classificacao", unique = true, length = 40, nullable = false)
	private String nome;
	
	public ClassificacaoDefeito() {
		
	}
	
	public ClassificacaoDefeito(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;		
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
	public void setNome(String classificacao) {
		this.nome = classificacao;
	}
	
	@Override
	public String toString() {
		return "ClassificacaoDefeito [id=" + id + ", classificacao="
				+ nome  + "]";
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
		ClassificacaoDefeito other = (ClassificacaoDefeito) obj;
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
