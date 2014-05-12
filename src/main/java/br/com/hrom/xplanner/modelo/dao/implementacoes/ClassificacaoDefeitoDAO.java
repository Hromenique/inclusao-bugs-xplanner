package br.com.hrom.xplanner.modelo.dao.implementacoes;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.IClassificacaoDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.ClassificacaoDefeito;

/**
 * 
 * Classe com as funções de DAO para a entidade (tabela) classificacao
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class ClassificacaoDefeitoDAO extends AbstractDAO<ClassificacaoDefeito> implements IClassificacaoDefeitoDAO{

	@Inject
	public ClassificacaoDefeitoDAO(EntityManager entityManager) {
		super(entityManager);
	}
}
