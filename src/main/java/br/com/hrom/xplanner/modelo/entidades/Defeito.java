package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * Classe que representa a entidade(tabela) defeito (Bug) do banco de dados
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@Entity
@Table(name = "defeito")
@NamedQueries({	@NamedQuery(name="Defeito.buscaDefeitosPorUserStory",
							query="SELECT d FROM Defeito d WHERE estoria = :estoria")})

public class Defeito implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String BUSCA_DEFEITOS_POR_ESTORIA_DE_USUARIO = "Defeito.buscaDefeitosPorUserStory";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length=50, nullable=false)
	String titulo;
	
	@Column(columnDefinition="text")
	private String descricao;
	
	public Defeito(){
		
	}
	
	@OneToOne
	@JoinColumn(name = "id_estoria", foreignKey = @ForeignKey(name = "defeito_story_fkey"))	
	private UserStory estoria;
	
	@OneToOne
	@JoinColumn(name = "id_responsavel", foreignKey = @ForeignKey(name = "defeito_person_fkey"))	
	private Person responsavel;
	
	@OneToOne
	@JoinColumn(name = "criticidade", foreignKey = @ForeignKey(name = "defeito_criticidade_fkey"))
	private CriticidadeDefeito criticidade;
	
	@OneToOne
	@JoinColumn(name = "tipo", foreignKey = @ForeignKey(name = "defeito_tipo_fkey"))
	private TipoDefeito tipo;
	
	@OneToOne
	@JoinColumn(name = "classificacao", foreignKey = @ForeignKey(name = "defeito_classificacao_fkey"))
	private ClassificacaoDefeito classificacao;
		
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Person getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(Person responsavel) {
		this.responsavel = responsavel;
	}
	public CriticidadeDefeito getCriticidade() {
		return criticidade;
	}
	public void setCriticidade(CriticidadeDefeito criticidade) {
		this.criticidade = criticidade;
	}
	public TipoDefeito getTipo() {
		return tipo;
	}
	public void setTipo(TipoDefeito tipo) {
		this.tipo = tipo;
	}
	public ClassificacaoDefeito getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(ClassificacaoDefeito classificacao) {
		this.classificacao = classificacao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	

}
