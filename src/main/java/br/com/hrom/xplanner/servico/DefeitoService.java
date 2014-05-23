package br.com.hrom.xplanner.servico;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.hrom.xplanner.modelo.dao.interfaces.IDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.Defeito;
import br.com.hrom.xplanner.modelo.entidades.UserStory;

/**
 * Esta classe contém todas as funções/regras de negócio relacionadas a entidade Defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class DefeitoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private IDefeitoDAO defeitoDAO;
	
	@Inject
	public DefeitoService(IDefeitoDAO dao){
		this.defeitoDAO = dao;
	}
	
	public List<Defeito> listaTodosDefeitos(){
		return this.defeitoDAO.listaTodos();
	}
	
	public List<Defeito> listaDefeitosPorEstoria(UserStory estoria){
		return this.defeitoDAO.buscaDefeitosPorUserStory(estoria);
	}
	
	public void salvaDefeito(Defeito defeito){		
		if(defeito.getId() > 0){
			this.defeitoDAO.atualiza(defeito);
			return;
		}
		
		this.defeitoDAO.salva(defeito);
	}
	
	public void excluirDefeito(Defeito defeito){
		this.defeitoDAO.exclui(defeito);
	} 
}
