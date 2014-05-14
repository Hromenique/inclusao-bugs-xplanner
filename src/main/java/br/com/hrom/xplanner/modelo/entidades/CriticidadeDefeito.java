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
}
