package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Esta classe representa a entidade/tabela status_defeito do banco de dados do xplanner
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
@Entity
@Table(name = "status_defeito")
public class StatusDefeito implements Serializable {	

	private static final long serialVersionUID = 1L;
	
	@Id
	private int id;
	@Column(length = 40, nullable = false)
	private String nome;
	
	public StatusDefeito(){
		
	}
	
	public StatusDefeito(int id, String status) {
		super();
		this.id = id;
		this.nome = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String status) {
		this.nome = status;
	}

	@Override
	public String toString() {
		return "StatusDefeito [id=" + id + ", status=" + nome + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		StatusDefeito other = (StatusDefeito) obj;
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
