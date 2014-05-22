package br.com.hrom.xplanner.modelo.dao.implementacoes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.IDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.Defeito;
import br.com.hrom.xplanner.modelo.entidades.UserStory;

/**
 * Classe com as funções de DAO para a entidade (tabela) defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class DefeitoDAO extends AbstractDAO<Defeito> implements IDefeitoDAO {

	@Inject
	public DefeitoDAO(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public List<Defeito> buscaDefeitosPorUserStory(UserStory userStory) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("estoria", userStory);
		return criaTypedQuery(Defeito.BUSCA_DEFEITOS_POR_ESTORIA_DE_USUARIO, parametros).getResultList();
	}
}
