package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Classe que representa a entidade tipo_defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@Entity
@Table(name = "tipo_defeito")
@SequenceGenerator(name = "tipo_defeito_sequence", sequenceName = "tipo_defeito_sequence", initialValue = 1, allocationSize = 1)
public class TipoDefeito implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	@Column(name = "nome_tipo", length = 30, unique = true, nullable = false)
	private String tipo;
	private boolean ativo;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
