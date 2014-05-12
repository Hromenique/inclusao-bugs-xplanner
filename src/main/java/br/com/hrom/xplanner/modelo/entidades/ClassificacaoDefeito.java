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
 * Classe que representa a entidade (tabela) classificacao do banco de dados. Ex: Impeditivo, Funcional, Interface...
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
@Entity
@Table(name="classificacao")
@SequenceGenerator(name = "classificacao_sequence", sequenceName = "classificacao_sequence", initialValue = 1, allocationSize = 1)
public class ClassificacaoDefeito implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="classificacao_sequence")
	private long id;
	
	@Column(name="nome_classificacao", unique = true, length = 30, nullable = false)
	private String classificacao;
	private boolean ativo;
	
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
}
