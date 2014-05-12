package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Classe que representa a entidade (tabela) Criticidade do banco de dados. Ex: Baixa, Média, Alta
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
@Entity
@Table(name = "criticidade")
@SequenceGenerator(name = "criticidade_sequence", sequenceName = "criticidade_sequence", initialValue = 1, allocationSize = 1)
public class CriticidadeDefeito implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "criticidade_sequence")
	private long id;
	
	@Column(name = "nome_criticidade", length = 30, unique = true, nullable = false)
	private String criticidade;
	private boolean ativo;
	
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
