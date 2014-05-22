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
	private String titulo;
	
	@Column(columnDefinition="text")
	private String descricao;
	
	public Defeito(){
		
	}	
	
	public Defeito(long id, String titulo, String descricao, UserStory estoria,
			Person responsavel, CriticidadeDefeito criticidade,
			TipoDefeito tipo, ClassificacaoDefeito classificacao,
			StatusDefeito status) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.estoria = estoria;
		this.responsavel = responsavel;
		this.criticidade = criticidade;
		this.tipo = tipo;
		this.classificacao = classificacao;
		this.status = status;
	}

	@OneToOne(orphanRemoval=true, optional=false)
	@JoinColumn(name = "id_estoria", foreignKey = @ForeignKey(name = "defeito_story_fkey"))	
	private UserStory estoria;
	
	@OneToOne(optional=true)
	@JoinColumn(name = "id_responsavel", foreignKey = @ForeignKey(name = "defeito_person_fkey"))	
	private Person responsavel;
	
	@OneToOne(optional=false)
	@JoinColumn(name = "id_criticidade", foreignKey = @ForeignKey(name = "defeito_criticidade_fkey"))
	private CriticidadeDefeito criticidade;
	
	@OneToOne(optional=false)
	@JoinColumn(name = "id_tipo", foreignKey = @ForeignKey(name = "defeito_tipo_fkey"))
	private TipoDefeito tipo;
	
	@OneToOne(optional=false)
	@JoinColumn(name = "id_classificacao", foreignKey = @ForeignKey(name = "defeito_classificacao_fkey"))
	private ClassificacaoDefeito classificacao;
	
	@OneToOne(optional=false)
	@JoinColumn(name = "id_status", foreignKey = @ForeignKey(name = "defeito_status_fkey"))
	private StatusDefeito status;
		
	public String getDescricao() {
		return descricao;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public UserStory getEstoria() {
		return estoria;
	}
	public void setEstoria(UserStory estoria) {
		this.estoria = estoria;
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
	public StatusDefeito getStatus() {
		return status;
	}
	public void setStatus(StatusDefeito status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Defeito [id=" + id + ", titulo=" + titulo + ", descricao="
				+ descricao + ", estoria=" + estoria + ", responsavel="
				+ responsavel + ", criticidade=" + criticidade + ", tipo="
				+ tipo + ", classificacao=" + classificacao + ", status="
				+ status + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classificacao == null) ? 0 : classificacao.hashCode());
		result = prime * result
				+ ((criticidade == null) ? 0 : criticidade.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((estoria == null) ? 0 : estoria.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((responsavel == null) ? 0 : responsavel.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		Defeito other = (Defeito) obj;
		if (classificacao == null) {
			if (other.classificacao != null)
				return false;
		} else if (!classificacao.equals(other.classificacao))
			return false;
		if (criticidade == null) {
			if (other.criticidade != null)
				return false;
		} else if (!criticidade.equals(other.criticidade))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (estoria == null) {
			if (other.estoria != null)
				return false;
		} else if (!estoria.equals(other.estoria))
			return false;
		if (id != other.id)
			return false;
		if (responsavel == null) {
			if (other.responsavel != null)
				return false;
		} else if (!responsavel.equals(other.responsavel))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}
}
