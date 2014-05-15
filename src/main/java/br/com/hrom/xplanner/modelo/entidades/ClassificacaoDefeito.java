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
	
	@Column(name="nome_classificacao", unique = true, length = 30, nullable = false)
	private String classificacao;
	@Column(columnDefinition = "default 1")
	private boolean ativo;
	
	public ClassificacaoDefeito() {
		
	}
	
	public ClassificacaoDefeito(long id, String classificacao, boolean ativo) {
		super();
		this.id = id;
		this.classificacao = classificacao;
		this.ativo = ativo;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	@Override
	public String toString() {
		return "ClassificacaoDefeito [id=" + id + ", classificacao="
				+ classificacao + ", ativo=" + ativo + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ativo ? 1231 : 1237);
		result = prime * result
				+ ((classificacao == null) ? 0 : classificacao.hashCode());
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
		if (ativo != other.ativo)
			return false;
		if (classificacao == null) {
			if (other.classificacao != null)
				return false;
		} else if (!classificacao.equals(other.classificacao))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
