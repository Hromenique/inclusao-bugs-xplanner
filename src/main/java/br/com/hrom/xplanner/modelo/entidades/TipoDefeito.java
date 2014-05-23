package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe que representa a entidade (tabela) tipo_defeito do banco de dados. Ex: Requisitos, Análise, Codificação
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@Entity
@Table(name = "tipo_defeito")
public class TipoDefeito implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	@Column(name = "nome_tipo", length = 30, unique = true, nullable = false)
	private String nome;
	
	public TipoDefeito(){
		
	}
	
	public TipoDefeito(long id, String tipo) {
		super();
		this.id = id;
		this.nome = tipo;		
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
	public void setNome(String tipo) {
		this.nome = tipo;
	}
	
	@Override
	public String toString() {
		return "TipoDefeito [id=" + id + ", tipo=" + nome + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		TipoDefeito other = (TipoDefeito) obj;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
