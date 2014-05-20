package br.com.hrom.xplanner.modelo.dao.interfaces;

import java.util.List;

import br.com.hrom.xplanner.modelo.entidades.Defeito;
import br.com.hrom.xplanner.modelo.entidades.UserStory;

/**
 * Interface com as operações básicas de DAO (Data Access Object) para a entidade (tabela) defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public interface IDefeitoDAO extends IDAO<Defeito> {	
	
	List<Defeito> buscaDefeitosPorUserStory(UserStory userStory);
}
