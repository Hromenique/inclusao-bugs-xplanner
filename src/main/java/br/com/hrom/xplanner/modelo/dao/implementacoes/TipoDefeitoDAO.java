package br.com.hrom.xplanner.modelo.dao.implementacoes;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.ITipoDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;

/**
 * 
 * Classe com as funções de DAO para a entidade (tabela) tipo_defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

public class TipoDefeitoDAO extends AbstractDAO<TipoDefeito> implements ITipoDefeitoDAO{

	@Inject
	public TipoDefeitoDAO(EntityManager entityManager) {
		super(entityManager);		
	}
}
